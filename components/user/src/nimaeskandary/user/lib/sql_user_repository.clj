(ns nimaeskandary.user.lib.sql-user-repository
  (:require
    [nimaeskandary.user.interface.types :as types]
    [nimaeskandary.user.interface :as interface]
    [com.stuartsierra.component :as component]
    [honey.sql :as sql]
    [honey.sql.helpers :as h]
    [next.jdbc :as jdbc]
    [malli.core :as m]))

(defn from-db [results]
  (reduce (fn [formatted r]
            (conj formatted {:id (:users/id r)
                             :email (:users/email r)
                             :username (:users/username r)}))
          []
          results))

(defn save-user [{:keys [app-db]} user] (->> (-> (h/insert-into :users)
                                                 (h/values [user])
                                                 (h/returning :id :email :username)
                                                 (sql/format))
                                             (jdbc/execute! app-db)
                                             from-db
                                             first))
(m/=> save-user types/save-user)

(defn get-user [{:keys [app-db]} user-id] (->> (-> (h/select :id :username :email)
                                                   (h/from :users)
                                                   (h/where [:= :id user-id])
                                                   (sql/format))
                                               (jdbc/execute! app-db)
                                               from-db
                                               first))
(m/=> get-user types/get-user)

(defrecord SqlUserRepository [])

(extend-type SqlUserRepository
  component/Lifecycle
  (start [this] this)
  (stop [this] this))

(extend-type SqlUserRepository
  interface/UserRepository
  (save-user [this user] (save-user this user))
  (get-user [this user-id] (get-user this user-id)))
