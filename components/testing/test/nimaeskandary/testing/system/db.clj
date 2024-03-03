(ns nimaeskandary.testing.system.db
  (:require [nimaeskandary.db.interface.sql-db :as sql-db]))

;; not a real postgres db, in memory h2 db with postgres dialect mode, can be
;; wonky with advanced usage
;; of features, most notably CTEs
;; http://www.h2database.com/html/advanced.html?highlight=recursive&search=re#recursive_queries
(defn create-in-memory-db
  ([db-name] (create-in-memory-db db-name nil))
  ([db-name migratus-config]
   (sql-db/create-sql-db
    {:db-spec {:dbtype "h2:mem", :dbname db-name, :MODE "PostgreSQL"},
     :pool-config {:username "", :password ""},
     :migratus-config migratus-config})))
