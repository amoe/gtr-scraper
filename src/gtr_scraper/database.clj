(ns gtr-scraper.database
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(def db-spec 
  {:subprotocol "postgresql"
   :subname "//localhost/gtr"
   :user "gtr"
   :password "jz3KTupRvDzf9Io1"})

(defn load-config []
  {:datastore  (jdbc/sql-database db-spec)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

