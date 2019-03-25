(ns gtr-scraper.database
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [environ.core :as environ]))

(def db-spec 
  {:subprotocol "postgresql"
   :subname "//localhost/gtr"
   :user "gtr"
   :password (:database-password environ/env)})

(defn load-config []
  {:datastore  (jdbc/sql-database db-spec)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

