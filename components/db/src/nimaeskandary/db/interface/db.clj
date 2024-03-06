(ns nimaeskandary.db.interface.db
  (:require [nimaeskandary.db.sql-db :as sql-db]))
(defn create-sql-db [config] (sql-db/->SqlDatabase config))
