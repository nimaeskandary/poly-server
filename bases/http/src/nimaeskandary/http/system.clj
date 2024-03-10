(ns nimaeskandary.http.system
  (:require [com.stuartsierra.component :as component]
            [nimaeskandary.db.interface.db :as db]
            [nimaeskandary.http.routes.core :as routes.core]
            [nimaeskandary.server.interface.core :as server]
            [nimaeskandary.user.interface.user-repository :as user-repo]))

(defn system-map
  []
  (component/system-map :app-db (db/create-sql-db)
                        :user-repo (user-repo/create-sql-user-repository)
                        :server (server/create-http-kit-server
                                 #'routes.core/app-ring-handler)))

(defn dependency-map [] {:app-db [], :user-repo [:app-db], :server []})

(defn create-system
  [config]
  (-> (system-map)
      ((fn [s] (merge-with merge s config)))
      (component/system-using (dependency-map))))
