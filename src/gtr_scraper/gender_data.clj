(ns gtr-scraper.gender-data
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]))


(def genderized-data-path "data/all-genderized.json")

(defn get-gender-data []
  (let [full-data-set (cheshire/parse-stream (io/reader genderized-data-path) true)]
    (->> full-data-set
         (map (fn [datum] [(:name datum) (:gender datum)]))
         (into {}))))

  ;; (doseq [datum the-data]
  ;;   (if-let [gender (:gender datum)]
  ;;     (data-service/set-gender-for-first-name (:name datum) gender))))



