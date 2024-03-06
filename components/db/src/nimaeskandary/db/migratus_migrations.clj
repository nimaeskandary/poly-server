(ns nimaeskandary.db.migratus-migrations
  (:require [migratus.core :as migratus]))

(defn ->migratus-settings
  [{:keys [migrations-dir migration-table-name]} datasource]
  {:pre [(and migrations-dir migration-table-name datasource)]}
  {:store :database,
   :migration-dir migrations-dir,
   :migration-table-name migration-table-name,
   :db {:datasource datasource, :managed-connection? true}})

(defn create
  [{:keys [migratus-settings], :as _db-component} migration-name]
  (when migratus-settings (migratus/create migratus-settings migration-name)))

(defn migrate
  [{:keys [migratus-settings], :as _db-component}]
  (when migratus-settings (migratus/migrate migratus-settings)))

(defn rollback
  [{:keys [migratus-settings], :as _db-component}]
  (when migratus-settings (migratus/rollback migratus-settings)))
