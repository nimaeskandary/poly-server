(ns nimaeskandary.server.interface.http-kit
  (:require
    [nimaeskandary.logging.interface :as log]
    [com.stuartsierra.component :as component]
    [org.httpkit.server :as server]))

(defrecord HttpKitServer [handler-fn http-kit-config dev?])

(extend-type HttpKitServer
  component/Lifecycle
  (start [{:keys [handler-fn http-kit-config logger dev?] :as this}]
    (let [http-kit-config (merge
                            {:error-logger (fn [msg ex] (log/error logger msg ex))
                             :warn-logger (fn [msg ex] (log/warn logger msg ex))
                             :event-logger (fn [event] (log/info logger event))}
                            http-kit-config)
          http-server (server/run-server (handler-fn {:system this :dev? dev?})
                                         http-kit-config)]
      (log/info logger (format "started http server on port %d" (:port http-kit-config)))
      (assoc this :http-server http-server)))
  (stop [{:keys [http-server :as this]}]
    (when http-server
      (http-server :timeout 100))
    (dissoc this :http-server)))
