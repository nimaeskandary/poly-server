(ns nimaeskandary.http.main
  (:require [com.stuartsierra.component :as component]
            [nimaeskandary.http.system :as system]
            [nimaeskandary.config.interface.core :as config])
  (:gen-class))

(defn -main
  []
  (component/start (system/create-http-server-system (config/http-server-config
                                                      :prod))))
