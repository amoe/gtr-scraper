(ns gtr-scraper.core
  (:require [gtr-scraper.util :refer :all]
            [clojure.pprint :as pprint]
            [clojure.java.io :as io]
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

(defn get-funds-page-response [page-number]
  (-> (client/get "http://gtr.ukri.org/gtr/api/funds" {:as :json
                                                       :accept :json
                                                       :query-params
                                                       {"s" "100"
                                                        "p" (str page-number)}})
      :body))


(defn get-funds-page [page-number]
  (-> (get-funds-page page-number)
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

(defn get-person-links-in-project [project]
  (filter #(contains? single-person-relationship-types (:rel %))
          (-> project
              (find-link-by-rel-type "FUNDED")
              get-project
              :links
              :link)))

(defn get-person-names-in-project [person-links]
  (->> person-links
       (map :href)
       (map #(client/get % {:as :json :accept :json}))
       (map :body)
       (map :firstName)))

(defn stupid-scraper []
  (doseq [n (range 1 (+ (get-total-pages) 1))]
    (spit (format "funds-page-%d.json" n) (get-funds-page-response n))
    (Thread/sleep 5000)))

(defn stupid-project-scraper []
  (->> (file-seq (io/file "."))
       (filter #(.endsWith (str %) ".json"))
       (map #(-> (slurp %) read-string))))
        

(defn create-mapping [page-number index]
  (let [project (nth (get-funds-page page-number) index)]
    (let [name-set  (-> (get-person-links-in-project project)
                        get-person-names-in-project)]
      {:value (:valuePounds project)
       :names name-set})))
       
      


(defn scrape-funds []
  (map get-funds-page (range 1 824)))
;(filter #(= (:rel %) "FUNDED")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (doseq [x  (stupid-project-scraper)]
    (let [funds (:fund x)]
      (doseq [fund funds]
        (let [all-links (get-in fund [:links :link])]
          (println (:href (first (filter #(= (:rel %) "FUNDED") all-links)))))))))

