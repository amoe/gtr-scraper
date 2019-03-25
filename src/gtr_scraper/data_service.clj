(ns gtr-scraper.data-service
  (:require [hugsql.core :as hugsql]
            [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clj-time.coerce :as c]
            [gtr-scraper.projects :as projects]
            [gtr-scraper.database :as database]
            [gtr-scraper.gender-data :as gender-data]
            [cheshire.core :as cheshire]
            [clojure.java.io :as io]))

(hugsql/def-db-fns "sql/main.sql")

(defn set-gender-for-first-name [first-name gender]
  (update-gender-for-name! database/db-spec {:first_name first-name
                                             :gender gender}))

(defn str->uuid [str]
  (java.util.UUID/fromString str))

(defn get-exts [dir-path ext]
  (->> (file-seq (io/file dir-path))
       (filter #(.endsWith (str %) ext))))

(defn get-jsons [dir-path] (get-exts dir-path ".json"))

(defn load-json [path]
  (cheshire/parse-string (slurp path) keyword))

(defn person->sql [datum gender-lookup]
  (let [first-name (:firstName datum)]
    (try
      {:id (str->uuid (:id datum))
       :first_name (:firstName datum)
       :last_name (:surname datum)
       :gender (get gender-lookup first-name)}
      (catch Exception e
        (println "failed to convert a person" datum)))))

(defn get-person-sql []
  (let [gender-lookup (gender-data/get-gender-data)]
    (->> (get-jsons "/home/amoe/dev/gtr-scraper/data/persons")
         (map load-json)
         (map #(person->sql % gender-lookup)))))

(defn insert-all-persons! []
  (doseq [person (get-person-sql)]
    (try
      (insert-person! database/db-spec person)
      (catch Exception e
        (println "this person failed" person e)))))


(defn domain-timestamp-to-sql-timestamp [dt]
  (c/to-sql-time dt))

(defn long-epoch-time-to-datetime [et]
  (c/from-long et))

(defn project->sql [datum]
  {:id (str->uuid (:id datum))
   :created_date (-> datum
                     :created
                     long-epoch-time-to-datetime
                     domain-timestamp-to-sql-timestamp)
   :title (:title datum)
   :abstract (:abstractText datum)})

;; munge the data off the end
(defn link->uuid [url]
  (string/replace url #".*/" ""))

;; remember that project already needs to have been created at this stage
(defn set-up-links-for-project! [project]
  (let [link-list (projects/get-people-from-project project)]
    (doseq [link link-list]
      (let [href (:href link)
            id (:id project)]
        (when (nil? href)
          (throw (ex-info "href was nil for" {:data project})))

        (when (nil? id)
          (throw (ex-info "id was nil for" {:data project})))


        (link-person-to-project! database/db-spec
                                 {:person_id (str->uuid (link->uuid href))
                                  :project_id (str->uuid id)
                                  :role_code (:rel link)})))))

(defn writedata [alldata]
  (with-open [foo (io/writer "/home/amoe/blah.lst")]
    (doseq [x alldata]
      (when (not (empty? x))
        (.write foo (:href (first x)))
        (.write foo "\n")))))

(defn get-all-projects []
  (->> (get-jsons "/home/amoe/dev/gtr-scraper/data/projects")
       (map load-json)))

(defn query-if-person-exists* [string]
  (-> (query-if-person-exists database/db-spec {:id (str->uuid string)})
      first
      :person_exists_p))

(defn write-person-disjunction []
  (with-open [foo (io/writer "/home/amoe/person-disjunction.lst")]
    (doseq [id (line-seq (io/reader "/home/amoe/persons-reffed-from-projects.lst"))]
      (let [exists? (query-if-person-exists* id)]
        (when-not exists?
          (.write foo id)
          (.write foo "\n"))))))
  
(defn spit-persons-from-projects []
  (with-open [foo (io/writer "/home/amoe/persons-reffed-from-projects.lst")]
    (doseq [project (get-all-projects)]
      (let [link-list (projects/get-people-from-project project)]
        (doseq [link link-list]
          (.write foo (link->uuid (:href link)))
          (.write foo "\n"))))))

(defn insert-all-projects! []
  (doseq [project (get-all-projects)]
    (when project
      (let [id (:id project)]
        (when (nil? id)
          (println project)
          (throw (ex-info "project id null" {:data project})))
            (insert-project! database/db-spec (project->sql project))
            (set-up-links-for-project! project)))))

(defn get-all-funds-from-pages []
  (->> (get-exts "/home/amoe/dev/gtr-scraper/data/" ".edn")
       (map slurp)
       (map read-string)
       (map :fund)
       (apply concat)))

(defn get-funded [fund]
  (->> (filter #(= (:rel %) "FUNDED") (get-in fund [:links :link]))
       first
       :href
       link->uuid))

(defn fund->sql [fund]
  {:id (str->uuid (:id fund))
   :funded_id (str->uuid (get-funded fund))
   :value_pounds (-> fund :valuePounds :amount)
   :start_date (-> fund
                   :start
                   long-epoch-time-to-datetime
                   domain-timestamp-to-sql-timestamp)
   :end_date (-> fund
                 :end
                 long-epoch-time-to-datetime
                 domain-timestamp-to-sql-timestamp)})

(defn query-if-project-exists* [string]
  (-> (query-if-project-exists database/db-spec {:id (str->uuid string)})
      first
      :project_exists_p))

;; handle dangling links
(defn insert-all-funds! []
  (doseq [fund (get-all-funds-from-pages)]
    (let [exists? (query-if-project-exists* (get-funded fund))]
      (when exists?
        (insert-fund! database/db-spec (fund->sql fund))))))

;; find broken fund->project links
(defn write-fund-disjunction []
  (with-open [foo (io/writer "/home/amoe/project-disjunction.lst")]
    (doseq [fund (get-all-funds-from-pages)]
      (let [id (get-funded fund)]
        (let [exists? (query-if-project-exists* id)]
          (when-not exists?
            (.write foo id)
            (.write foo "\n")))))))


(defn do-full-import! []
  (println "Importing entity: person.")
  (insert-all-persons!)
  (println "Importing entity: project.")
  (insert-all-projects!)
  (println "Importing entity: fund.")
  (insert-all-funds!))
