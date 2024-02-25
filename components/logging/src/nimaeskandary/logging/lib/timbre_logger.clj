(ns nimaeskandary.logging.lib.timbre-logger
  (:require [nimaeskandary.logging.interface :as interface]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(defrecord TimbreLogger [config]
  interface/Logger
    (start [{:keys [config], :as this}]
      (let [config (or config
                       {:min-level :info,
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
    (stop [this] this)
    (trace [_ message] (timbre/trace message))
    (trace [_ message extra] (timbre/trace message extra))
    (debug [_ message] (timbre/debug message))
    (debug [_ message extra] (timbre/debug message extra))
    (info [_ message] (timbre/info message))
    (info [_ message extra] (timbre/info message extra))
    (warn [_ message] (timbre/warn message))
    (warn [_ message ex] (timbre/warn message ex))
    (warn [_ message ex extra] (timbre/warn message ex extra))
    (error [_ message] (timbre/error message))
    (error [_ message ex] (timbre/error message ex))
    (error [_ message ex extra] (timbre/error message ex extra)))
