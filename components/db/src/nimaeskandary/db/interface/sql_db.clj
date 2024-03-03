(ns nimaeskandary.db.interface.sql-db
  (:require [nimaeskandary.db.lib.sql :as sql]))
(defn create-sql-db [config] (sql/->SqlDatabase config))
