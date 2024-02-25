(ns nimaeskandary.logging.lib.timbre-logger
  (:require [com.stuartsierra.component :as component]
            [nimaeskandary.logging.interface :as interface]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(defrecord TimbreLogger [config])

(extend-type TimbreLogger
  component/Lifecycle
    (start [{:keys [config], :as this}]
      (let [config (or config
                       {:min-level :debug,
                        :ns-filter #{"*"},
                        :middleware [],
                        :timestamp-opts timbre/default-timestamp-opts,
                        :output-fn timbre/default-output-fn,
                        :appenders {:println (appenders/println-appender
                                               {:stream :auto}),
                                    :spit (appenders/spit-appender
                                            {:fname "./logs"})}})]
        (timbre/set-config! config)
        (assoc this :config config)))
    (stop [this] this))

(extend-type TimbreLogger
  interface/Logger
    (trace
      ([_ message] (timbre/trace message))
      ([_ message extra] (timbre/trace message extra)))
    (debug
      ([_ message] (timbre/debug message))
      ([_ message extra] (timbre/debug message extra)))
    (info
      ([_ message] (timbre/info message))
      ([_ message extra] (timbre/info message extra)))
    (warn
      ([_ message] (timbre/warn message))
      ([_ message ex] (timbre/warn message ex))
      ([_ message ex extra] (timbre/warn message ex extra)))
    (error
      ([_ message] (timbre/error message))
      ([_ message ex] (timbre/error message ex))
      ([_ message ex extra] (timbre/error message ex extra))))
