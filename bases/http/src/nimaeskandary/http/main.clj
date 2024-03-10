(ns nimaeskandary.http.main
  (:require [com.stuartsierra.component :as component]
            [nimaeskandary.http.system :as system]
            [nimaeskandary.common.interface.config :as config])
  (:gen-class))

(defn -main
  []
  (component/start (system/create-system (config/read-config "web/config.edn"
                                                             :prod))))
