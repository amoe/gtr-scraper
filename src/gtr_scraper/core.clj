(ns gtr-scraper.core
  (:require [gtr-scraper.util :refer :all]
            [clojure.pprint :as pprint]
            [clj-http.client :as client]))

(def single-person-relationship-types
  #{"PI_PER" "COI_PER" "PM_PER" "FELLOW_PER" "EMPLOYEE"})

(def funds-per-page 100)


(defn get-total-pages []
  (-> (client/get "http://gtr.ukri.org/gtr/api/funds" {:as :json
                                                       :accept :json
                                                       :query-params
                                                       {"s" "100"}})
      :body
      :totalPages))



(defn get-funds-page [page-number]
  (-> (client/get "http://gtr.ukri.org/gtr/api/funds" {:as :json
                                                       :accept :json
                                                       :query-params
                                                       {"s" "100"
                                                        "p" (str page-number)}})
      :body
      :fund))


(defn find-link-by-rel-type [fund rel-type]
  (-> (filter #(= (:rel %) rel-type)
              (-> fund :links :link))
      first
      :href))

(defn get-project [project-url]
  (-> (client/get project-url {:as :json
                               :accept :json})
      :body))


(defn get-project-in-page [n i]
  (-> (get-funds-page n)
      (nth i)
      (find-link-by-rel-type "FUNDED")
      get-project))

(defn get-person-links-in-project [n i]
  (filter #(contains? single-person-relationship-types (:rel %))
          (-> (get-funds-page n)
              (nth i)
              (find-link-by-rel-type "FUNDED")
              get-project
              :links
              :link)))

(defn get-person-names-in-project [n i]
  (->> (get-person-links-in-project n i)
       (map :href)
       (map #(client/get % {:as :json :accept :json}))
       (map :body)
       (map :firstName)))
      


(defn scrape-funds []
  (map get-funds-page (range 1 824)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (debugp "foo is" 42))
