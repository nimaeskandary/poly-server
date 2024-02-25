(ns nimaeskandary.db.interface.sql
  (:require [nimaeskandary.db.lib.sql-db :as sql-db]))
(defn create-sql-db [config] (sql-db/->SqlDatabase config))
