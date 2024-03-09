(ns system
  (:require [nimaeskandary.http.system :as http.system]
            [nimaeskandary.config.interface.core :as config]
            [com.stuartsierra.component :as component]))

(defonce ^:dynamic http-server-system nil)

(defn start-http-server-system
  []
  (alter-var-root #'http-server-system
                  (constantly (component/start
                               (http.system/create-http-server-system
                                (config/http-server-config :dev))))))

(defn stop-http-server-system
  []
  (alter-var-root #'http-server-system #(when % (component/stop-system %) nil)))

(defn restart-http-server-system
  []
  (stop-http-server-system)
  (start-http-server-system))
