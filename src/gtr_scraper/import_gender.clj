(ns gtr-scraper.import-gender
  (:require [cheshire.core :as cheshire]
            [gtr-scraper.data-service :as data-service]
            [clojure.java.io :as io]))


(def the-data (cheshire/parse-stream (io/reader "genderized.json") true))

(defn import-gender-data []
  (doseq [datum the-data]
    (if-let [gender (:gender datum)]
      (data-service/set-gender-for-first-name (:name datum) gender))))



