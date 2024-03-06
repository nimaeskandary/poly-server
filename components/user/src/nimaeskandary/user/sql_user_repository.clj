(ns nimaeskandary.user.sql-user-repository
  (:require [next.jdbc.result-set :as result-set]
            [nimaeskandary.user.interface.types :as types]
            [nimaeskandary.user.interface.user-repository-proto :as user-repo-proto]
            [com.stuartsierra.component :as component]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [next.jdbc :as jdbc]
            [malli.core :as m]))

(defn from-db
  [results]
  (reduce (fn [formatted r]
            (conj formatted
                  {:id (:users/id r),
                   :email (:users/email r),
                   :username (:users/username r)}))
          []
          results))

(defn create-user
  [{:keys [app-db]} user]
  (let [to-db (assoc user :id (random-uuid))
        q (-> (h/insert-into :users)
              (h/values [to-db])
              (sql/format))]
    (jdbc/execute! app-db q)
    to-db))

(m/=> create-user types/create-user)

(defn get-user
  [{:keys [app-db]} user-id]
  (let [q (-> (h/select :id :username :email)
              (h/from :users)
              (h/where [:= :id user-id])
              (sql/format))]
    (-> (jdbc/execute! app-db q {:builder-fn result-set/as-lower-maps})
        from-db
        first)))
(m/=> get-user types/get-user)

(defrecord SqlUserRepository [])

(extend-type SqlUserRepository
  user-repo-proto/UserRepository
   (create-user [this user] (create-user this user))
   (get-user [this user-id] (get-user this user-id)))

(extend-type SqlUserRepository
 component/Lifecycle
   (start [this] this)
   (stop [this] this))
