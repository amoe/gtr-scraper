(ns gtr-scraper.core-test
  (:require [clojure.test :refer :all]
            [clj-time.coerce :as c]
            [gtr-scraper.data-service :as data-service :refer [person->sql]]))

(def sample-person-datum-from-api
  {:created 1525197344000})

(deftest person-is-loaded-with-date
  (is (= #inst "2018-05-01T17:55:44.000-00:00" (-> (person->sql sample-person-datum-from-api nil)
                                                   :created_date))))
