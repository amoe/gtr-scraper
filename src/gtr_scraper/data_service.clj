(ns gtr-scraper.data-service
  (:require [hugsql.core :as hugsql]
            [clojure.pprint :as pprint]
            [gtr-scraper.projects :as projects]
            [gtr-scraper.database :as database]
            [cheshire.core :as cheshire]
            [clojure.java.io :as io]))

(hugsql/def-db-fns "sql/main.sql")


(defn get-jsons [dir-path]
  (->> (file-seq (io/file dir-path))
       (filter #(.endsWith (str %) ".json"))))


(defn load-json [path]
  (cheshire/parse-string (slurp path) keyword))


(defn get-person-sql []
  (->> (get-jsons "/home/amoe/dev/gtr-scraper/data/persons")
       (take 20)
       (map load-json)
       (map person->sql)))

(defn person->sql [datum]
  {:id (java.util.UUID/fromString (:id datum))
   :first_name (:firstName datum)
   :last_name (:surname datum)})


(defn project->sql [datum]
  {:id (java.util.UUID/fromString (:id datum))})


;; munge the data off the end
(defn link->uuid []
  nil)

;; remember that project already needs to have been created at this stage
(defn set-up-links-for-project! [project]
  (let [link-list (projects/get-people-from-project project)]
    (doseq [link link-list]
      (link-person-to-project! database/db-spec
                               {:person_id (link->uuid (:href link))
                                :project_id (:id project)}))))

(defn insert-all-projects []
  (doseq [project (get-all-projects)]
    (insert-project! database/db-spec {:id (:id project)})
    (set-up-links-for-project! project)))

(defn get-all-projects []
  (->> (get-jsons "/home/amoe/dev/gtr-scraper/data/projects")
       (take 20)
       (map load-json)))






