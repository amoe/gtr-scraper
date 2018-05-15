(ns gtr-scraper.projects
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]))

(def single-person-relationship-types #{"COI_PER"
                                        "FELLOW_PER"
                                        "PI_PER"
                                        "PM_PER"
                                        "RESEARCH_COI_PER"
                                        "RESEARCH_PER"
                                        "STUDENT_PER"
                                        "SUPER_PER"
                                        "TGH_PER"})

(defn load-project [path]
  (cheshire/parse-string (slurp path) keyword))

(defn get-people-from-project [project]
  (filter #(contains? single-person-relationship-types (:rel %))
          (get-in project [:links :link])))

(defn stupid-person-scraper [directory]
  (->> (file-seq (io/file directory))
       (filter #(.endsWith (str %) ".json"))
       (map load-project)
       (map get-people-from-project)))

(defn writedata [alldata]
  (with-open [foo (io/writer "/home/amoe/blah.lst")]
    (doseq [x alldata]
      (when (not (empty? x))
        (.write foo (:href (first x)))
        (.write foo "\n")))))


