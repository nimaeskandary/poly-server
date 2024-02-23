(ns system
  (:require [nimaeskandary.db.interface.postgres :as postgres-db]
            [nimaeskandary.user.interface.sql :as user-repo]
            [nimaeskandary.migrations.interface.app-db :as app-db-migrations]
            [nimaeskandary.logging.interface.timbre :as logger]
            [nimaeskandary.server.interface.ring-jetty-server :as server]
            [nimaeskandary.web.core :as web.core]
            [com.stuartsierra.component :as component]))

(defonce ^:dynamic *system* nil)

(defn dev-system-map [config]
  (component/system-map
    :logger (logger/create-timbre-logger)
    :app-db (postgres-db/create-postgres-db "postgres" "password" "localhost" "55432" "app")
    :app-db-migrations (app-db-migrations/create-app-db-migrations)
    :user-repo (user-repo/create-sql-user-repository)
    :server (server/->JettyServer web.core/route-handler 9000 {})))

(def dependency-map {:logger []
                     :app-db [:logger]
                     :app-db-migrations {:logger :logger
                                         :db :app-db}
                     :user-repo [:logger :app-db]
                     :server [:logger
                              :user-repo]})

(defn create-dev-system [] (component/system-using (dev-system-map {}) dependency-map))

(defn init-dev-system [] (alter-var-root #'*system* (constantly (create-dev-system))))
(defn start-system [] (alter-var-root #'*system* component/start))
(defn stop-system [] (alter-var-root #'*system* #(when % (component/stop-system %) nil)))
(defn restart-dev-system [] (system/stop-system) (system/init-dev-system) (system/start-system))
