(ns nimaeskandary.db.interface.db
  (:require [nimaeskandary.db.sql-db :as sql-db]))
(defn create-sql-db [] (sql-db/map->SqlDatabase {}))
