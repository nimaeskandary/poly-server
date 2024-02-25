(ns nimaeskandary.db.lib.postgres-db
  (:require
    [nimaeskandary.logging.interface :as log]
    [com.stuartsierra.component :as component]
    [next.jdbc :as jdbc]
    [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord PostgresDatabase [user password host port dbname])

(extend-type PostgresDatabase
  component/Lifecycle
  (start [{:keys [user password host port dbname logger] :as this}]
    (log/info logger "getting datasource to db" {:db dbname})
    (assoc this
      :datasource
      (connection/->pool HikariDataSource {:dbtype "postgres" :username user :password password :host host :port port :dbname dbname})))
  (stop [{:keys [datasource] :as this}]
    (when datasource (.close ^HikariDataSource datasource))
    (dissoc this :datasource)))
