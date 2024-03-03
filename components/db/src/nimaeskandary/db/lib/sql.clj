(ns nimaeskandary.db.lib.sql
  (:require [nimaeskandary.db.lib.migratus-migrations :as migrations]
            [taoensso.timbre :as log]
            [com.stuartsierra.component :as component]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord SqlDatabase [config])

(defn with-jdbc-url
  [{{{:keys [dbtype dbname], :as db-spec} :db-spec} :config, :as this}]
  {:pre [(every? some? [dbtype dbname])]}
  (assoc this :jdbcUrl (connection/jdbc-url db-spec)))

(defn with-connection-pool
  [{:keys [jdbcUrl],
    {{:keys [username password], :as pool-config} :pool-config} :config,
    :as this}]
  {:pre [(every? some? [username password])]}
  (log/info "creating connection pool" {:jdbcUrl jdbcUrl})
  (assoc this
         :datasource
         (connection/->pool HikariDataSource
                            (assoc pool-config :jdbcUrl jdbcUrl))))

(defn with-migratus-settings
  [{:keys [datasource], {:keys [migratus-config]} :config, :as this}]
  (assoc this
         :migratus-settings
         (when migratus-config
           (migrations/->migratus-settings migratus-config datasource))))

;; db-spec
;; see
;; https://github.com/seancorfield/next-jdbc/blob/develop/doc/getting-started.md
;; in the db-spec, provide everything other than :user and :password keys to
;; build the jdbcUrl

;; pool-config
;; the pool-config for hikari requires :username and :password

;; migratus-config
;; if provided, runs migrations on start up
(extend-type SqlDatabase
 component/Lifecycle
   (start [this]
     (let [{:keys [migratus-settings], :as this} (-> (with-jdbc-url this)
                                                     with-connection-pool
                                                     with-migratus-settings)]
       (when migratus-settings
         (log/info "running migrations" {:migratus-settings migratus-settings})
         (migrations/migrate migratus-settings))
       this))
   (stop [{:keys [datasource], :as this}]
     (when datasource (.close ^HikariDataSource datasource))
     (dissoc this :datasource :jdbcUrl :migratus-settings)))
