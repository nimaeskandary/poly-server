(ns nimaeskandary.user.interface.postgres
  (:require
    [com.stuartsierra.component :as component]
    [nimaeskandary.user.interface :as interface]
    [nimaeskandary.user.lib.postgres-user-repository :as postgres-user-repository]))

(defrecord PostgresUserRepository [])

(extend-type PostgresUserRepository
  component/Lifecycle
  (start [this] this)
  (stop [this] this))

(extend-type PostgresUserRepository
  interface/UserRepository
  (save-user [this user] (postgres-user-repository/save-user this user))
  (get-user [this user-id] (postgres-user-repository/get-user this user-id)))
