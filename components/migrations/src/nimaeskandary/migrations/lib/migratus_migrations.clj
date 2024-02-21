(ns nimaeskandary.migrations.lib.migratus-migrations
  (:require
    [com.stuartsierra.component :as component]
    [nimaeskandary.migrations.interface :as interface]
    [migratus.core :as migratus]))

(defrecord MigratusMigrations [migrations-dir])

(extend-type MigratusMigrations
  component/Lifecycle
  (start [{:keys [db migrations-dir] :as this}]
    (let [config {:store                :database
                  :migration-dir        migrations-dir
                  :init-script          "init.sql"
                  :migration-table-name "migrations"
                  :db                   {:datasource (:datasource db)
                                         :managed-connection? true}}]
      (migratus/init config)
      (migratus/migrate config)
      (assoc this :config config)))
  (stop [this] (dissoc this :config)))

(extend-type MigratusMigrations
  interface/Migrations
  (run [{:keys [config]}] (migratus/migrate config))
  (rollback [{:keys [config]}] (migratus/rollback config))
  (create [{:keys [config]} name] (migratus/create config name)))
