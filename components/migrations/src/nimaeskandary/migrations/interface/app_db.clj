(ns nimaeskandary.migrations.interface.app-db
  (:require [nimaeskandary.migrations.lib.migratus-migrations :as
             migratus-migrations]))

(defn create-app-db-migrations
  []
  (migratus-migrations/->MigratusMigrations "migrations/app_db"))
