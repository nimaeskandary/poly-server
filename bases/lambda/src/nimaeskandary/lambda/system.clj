(ns nimaeskandary.lambda.system
  (:require [nimaeskandary.common.interface.config :as config]
            [nimaeskandary.db.interface.db :as db]
            [nimaeskandary.user.interface.user-repository :as user-repo]
            [com.stuartsierra.component :as component]))

(defn system-map
  []
  (component/system-map :app-db (db/create-sql-db)
                        :user-repo (user-repo/create-sql-user-repository)))

(defn dependency-map [] {:app-db [], :user-repo [:app-db]})

(defn create-system
  [config]
  (-> (system-map)
      ((fn [s] (merge-with merge s config)))
      (component/system-using (dependency-map))))

(defonce system (create-system (config/read-config "lambda/config.edn" :prod)))
