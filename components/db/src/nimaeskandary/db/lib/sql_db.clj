(ns nimaeskandary.db.lib.sql-db
  (:require [nimaeskandary.logging.interface :as log]
            [com.stuartsierra.component :as component]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord SqlDatabase [config])

;; see
;; https://github.com/seancorfield/next-jdbc/blob/develop/doc/getting-started.md
;; in the db-spec, provide everything other than :user and :password keys tp
;; build the jdbcUrl
;; the hikari config requires them in the format :username and :password

(extend-type SqlDatabase
  component/Lifecycle
    (start [{:keys [logger],
             {{:keys [dbname dbtype], :as db-spec} :db-spec,
              {:keys [username password], :as pool-config} :pool-config}
               :config,
             :as this}]
      {:pre [(every? some? [dbtype dbname username password])]}
      (let [jdbcUrl (connection/jdbc-url db-spec)]
        (log/info logger "creating connection pool" {:jdbcUrl jdbcUrl})
        (-> this
            (assoc :datasource (connection/->pool HikariDataSource
                                                  (assoc pool-config
                                                    :jdbcUrl jdbcUrl))
                   :jdbcUrl jdbcUrl))))
    (stop [{:keys [datasource], :as this}]
      (when datasource (.close ^HikariDataSource datasource))
      (dissoc this :datasource)))
