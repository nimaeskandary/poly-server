(ns nimaeskandary.logging.lib.timbre-logger
  (:require
    [com.stuartsierra.component :as component]
    [nimaeskandary.logging.interface :as interface]
    [taoensso.timbre :as timbre]
    [taoensso.timbre.appenders.core :as appenders]))

(defrecord TimbreLogger [config])

(extend-type TimbreLogger
  component/Lifecycle
  (start [{:keys [config] :as this}]
    (let [config (or config {:min-level      :debug
                             :ns-filter      #{"*"}
                             :middleware     []
                             :timestamp-opts timbre/default-timestamp-opts
                             :output-fn      timbre/default-output-fn
                             :appenders      {:println (appenders/println-appender {:stream :auto})}})]
      (timbre/set-config! config)
      (assoc this :config config)))
  (stop [this] this))

(extend-type TimbreLogger
  interface/Logger
  (trace [_ message] (timbre/trace message))
  (trace [_ message extra] (timbre/trace message extra))
  (debug [_ message] (timbre/debug message))
  (debug [_ message extra] (timbre/debug message extra))
  (info [_ message] (timbre/info message))
  (info [_ message extra] (timbre/info message extra))
  (warn [_ message] (timbre/warn message))
  (warn [_ message extra] (timbre/warn message extra))
  (error [_ error] (timbre/error error))
  (error [_ error message] (timbre/error error message))
  (error [_ error message extra] (timbre/error error message extra)))
