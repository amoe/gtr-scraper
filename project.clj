(defproject gtr-scraper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.8.0"]
                 [clj-http "3.9.0"]
                 [clj-time "0.14.4"]
                 [ragtime "0.7.2"]
                 [fipp "0.6.12"]
                 [org.postgresql/postgresql "9.4.1208.jre7"]
                 [com.layerware/hugsql "0.4.8"]
                 [org.clojure/tools.logging "0.4.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [environ "1.1.0"]]
  :aliases {"migrate"  ["run" "-m" "gtr-scraper.database/migrate"]
            "rollback" ["run" "-m" "gtr-scraper.database/rollback"]}
  :main ^:skip-aot gtr-scraper.core
  :target-path "target/%s"
  :plugins [[lein-environ "1.1.0"]]
  :profiles {:uberjar {:aot :all}})
