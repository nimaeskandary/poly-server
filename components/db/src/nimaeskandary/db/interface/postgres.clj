(ns nimaeskandary.db.interface.postgres
  (:require
    [nimaeskandary.db.interface :as interface]
    [com.stuartsierra.component :as component]
    [next.jdbc :as jdbc]))

(defrecord PostgresDatabase [user password host port dbname])

(extend-type PostgresDatabase
  component/Lifecycle
  (start [{:keys [user password host port dbname] :as this}]
    (assoc this
      :datasource
      (jdbc/get-datasource {:dbtype "postgres" :user user :password password :host host :port port :dbname dbname})))
  (stop [this] (dissoc this :datasource)))

(extend-type PostgresDatabase
  interface/Database
  (execute [{:keys [datasource]} q]
    (jdbc/execute! datasource q)))
