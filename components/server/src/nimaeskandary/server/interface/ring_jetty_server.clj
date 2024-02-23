(ns nimaeskandary.server.interface.ring-jetty-server
  (:require
    [nimaeskandary.logging.interface :as log]
    [com.stuartsierra.component :as component]
    [ring.adapter.jetty :as jetty])
  (:import (org.eclipse.jetty.server Server)))

(defrecord JettyServer [handler-fn port jetty-config])

(extend-type JettyServer
  component/Lifecycle
  (start [{:keys [handler-fn port extra-jetty-config logger] :as this}]
    (let [http-server (jetty/run-jetty (handler-fn this)
                                       (merge {:port port :join? false} extra-jetty-config))]
      (log/info logger (format "started http server on port %d" port))
      (assoc this :http-server http-server)))
  (stop [{:keys [http-server :as this]}]
    (when http-server
      (.stop ^Server http-server))
    (dissoc this :http-server)))
