(ns system
  (:require [nimaeskandary.db.interface.db :as db]
            [nimaeskandary.user.interface.user-repository :as user-repo]
            [nimaeskandary.server.interface.http-kit :as server]
            [nimaeskandary.http.routes.core :as routes.core]
            [com.stuartsierra.component :as component]))

;; See https://github.com/stuartsierra/component, form of dependency injection.
;; Suartsierra system
;; components are not the same thing as a polylith components, i.e. the sub
;; modules in <root>/components
;;
;; To qualify to be a system component an object must implement the Lifecycle
;; interface that comes with a start and stop
;; method. A system component object will have other system components it
;; depends on added on as attributes for easy access

;; This *system* var is meant for debugging purposes during local development

(defonce ^:dynamic *system* nil)

(defn dev-system
  [_]
  (component/system-using
   ;; system components by name
   (component/system-map
    :app-db
    (db/create-sql-db
     {:db-spec {:dbtype "h2:mem", :dbname "app", :MODE "PostgreSQL"},
      :pool-config {:username "", :password ""},
      :migratus-config {:migration-table-name "migrations",
                        :migrations-dir "db/migrations/app_db"}})
    ;; for postgres from docker compose
    #_{:db-spec
       {:dbtype "postgres", :dbname "app", :host "localhost", :port "55432"},
       :pool-config {:username "postgres", :password "password"},
       :migratus-config {:migrations-dir "db/migrations/app_db",
                         :migration-table-name "migrations"}}
    :user-repo (user-repo/create-sql-user-repository)
    :server
    (server/->HttpKitServer #'routes.core/app-ring-handler {:port 9000} true))
   ;; dependency map
   {:app-db [], :user-repo [:app-db], :server [:user-repo]}))

(defn start-system
  []
  (alter-var-root #'*system* (constantly (component/start (dev-system {})))))
(defn stop-system
  []
  (alter-var-root #'*system* #(when % (component/stop-system %) nil)))
(defn restart-dev-system [] (system/stop-system) (system/start-system))
