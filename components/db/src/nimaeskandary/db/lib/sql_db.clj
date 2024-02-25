(ns nimaeskandary.db.lib.sql-db
  (:require [nimaeskandary.logging.interface :as log]
            [com.stuartsierra.component :as component]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord SqlDatabase [config])

(extend-type SqlDatabase
  component/Lifecycle
    (start [{:keys [logger],
             {:keys [dbname dbtype username password host port]} :config,
             :as this}]
      {:pre [(every? some? [dbtype dbname])]}
      (log/info logger "getting datasource to db" {:db dbname})
      (assoc this
        :datasource (connection/->pool HikariDataSource
                                       {:dbtype dbtype,
                                        :username username,
                                        :password password,
                                        :host host,
                                        :port port,
                                        :dbname dbname})))
    (stop [{:keys [datasource], :as this}]
      (when datasource (.close ^HikariDataSource datasource))
      (dissoc this :datasource)))
