(ns nimaeskandary.migrations.interface.app-db
  (:require
    [com.stuartsierra.component :as component]
    [nimaeskandary.migrations.interface :as interface]
    [migratus.core :as migratus]))

(defrecord AppDBMigrations [])

(extend-type AppDBMigrations
  component/Lifecycle
  (start [{:keys [app-db] :as this}]
    (let [config {:store                :database
                  :migration-dir        "migrations/app_db/"
                  :init-script          "init.sql"
                  :migration-table-name "migrations"
                  :db                   {:datasource (:datasource app-db)
                                         :managed-connection? true}}]
      (migratus/init config)
      (migratus/migrate config)
      (assoc this :config config)))
  (stop [this] (dissoc this :config)))

(extend-type AppDBMigrations
  interface/Migrations
  (run [{:keys [config]}] (migratus/migrate config))
  (rollback [{:keys [config]}] (migratus/rollback config))
  (create [{:keys [config]} name] (migratus/create config name)))
