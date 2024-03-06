(ns nimaeskandary.user.interface.user-repository
  (:require [nimaeskandary.user.sql-user-repository :as sql-user-repository]))

(defn create-sql-user-repository [] (sql-user-repository/->SqlUserRepository))
