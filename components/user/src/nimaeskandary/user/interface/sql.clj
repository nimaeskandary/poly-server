(ns nimaeskandary.user.interface.sql
  (:require
    [nimaeskandary.user.lib.sql-user-repository :as sql-user-repository]))

(defn create-sql-user-repository []
  (sql-user-repository/->SqlUserRepository))
