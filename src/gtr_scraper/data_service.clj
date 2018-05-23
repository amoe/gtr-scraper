(ns gtr-scraper.data-service
  (:require [hugsql.core :as hugsql]
            [clojure.pprint :as pprint]
            [clojure.string :as string]
            [gtr-scraper.projects :as projects]
            [gtr-scraper.database :as database]
            [cheshire.core :as cheshire]
            [clojure.java.io :as io]))

(hugsql/def-db-fns "sql/main.sql")

(defn str->uuid [str]
  (java.util.UUID/fromString str))

(defn get-exts [dir-path ext]
  (->> (file-seq (io/file dir-path))
       (filter #(.endsWith (str %) ext))))

(defn get-jsons [dir-path] (get-exts dir-path ".json"))

(defn load-json [path]
  (cheshire/parse-string (slurp path) keyword))

(defn person->sql [datum]
  (try
    {:id (str->uuid (:id datum))
     :first_name (:firstName datum)
     :last_name (:surname datum)}
    (catch Exception e
      (println "failed to convert a person" datum))))

(defn get-person-sql []
  (->> (get-jsons "/home/amoe/dev/gtr-scraper/data/persons")
       (map load-json)
       (map person->sql)))

(defn insert-all-persons []
  (doseq [person (get-person-sql)]
    (try
      (insert-person! database/db-spec person)
      (catch Exception e
        (println "this person failed" person)))))

(defn project->sql [datum]
  {:id (str->uuid (:id datum))})

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
                                  :project_id (str->uuid id)})))))

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



(defn insert-all-projects []
  (doseq [project (get-all-projects)]
    (when project
      (let [id (:id project)]
        (when (nil? id)
          (println project)
          (throw (ex-info "project id null" {:data project})))
        (insert-project! database/db-spec {:id (str->uuid (:id project))})
        (set-up-links-for-project! project)))))

(defn get-all-fund-pages []
  (->> (get-exts "/home/amoe/dev/gtr-scraper/data/" ".edn")
       (take 10)
       (map slurp)
       (map read-string)
       (map :fund)
       (apply concat)))
