(ns nimaeskandary.testing.system.db
  (:require [nimaeskandary.db.interface.sql :as sql-db]))

;; not a real postgres db, in memory h2 db with postgres dialect mode, can be
;; wonky with advanced usage
;; of features, most notably CTEs
;; http://www.h2database.com/html/advanced.html?highlight=recursive&search=re#recursive_queries
(defn create-in-memory-postgres-db
  [db-name]
  (sql-db/create-sql-db
    {:db-spec {:dbtype "h2:mem", :dbname db-name, :MODE "PostgreSQL"},
     :pool-config {:username "", :password ""}}))
