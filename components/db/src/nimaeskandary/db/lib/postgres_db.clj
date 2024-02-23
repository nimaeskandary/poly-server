(ns nimaeskandary.db.lib.postgres-db
  (:require
    [nimaeskandary.logging.interface :as log]
    [com.stuartsierra.component :as component]
    [next.jdbc :as jdbc]))

(defrecord PostgresDatabase [user password host port dbname])

(extend-type PostgresDatabase
  component/Lifecycle
  (start [{:keys [user password host port dbname logger] :as this}]
    (log/info logger "getting datasource to db" {:db dbname})
    (assoc this
      :datasource
      (jdbc/get-datasource {:dbtype "postgres" :user user :password password :host host :port port :dbname dbname})))
  (stop [this] (dissoc this :datasource)))
