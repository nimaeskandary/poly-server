(ns nimaeskandary.db.interface.postgres
  (:require [nimaeskandary.db.lib.postgres-db :as postgres-db]))
(defn create-postgres-db
  [user password host port dbname]
  (postgres-db/->PostgresDatabase user password host port dbname))
