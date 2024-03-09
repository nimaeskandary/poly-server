(ns user
  (:require [system :as system]
            [malli.dev :as m.dev]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(comment
  (m.dev/start!)
  (m.dev/stop!)
  (system/restart-dev-system))

(timbre/set-config!
 {:min-level :info,
  :ns-filter #{"*"},
  :middleware [],
  :timestamp-opts timbre/default-timestamp-opts,
  :output-fn timbre/default-output-fn,
  :appenders {:println (appenders/println-appender {:stream :auto}),
              :spit (appenders/spit-appender {:fname "./logs/app.log"})}})

(defonce _start-http-server-system (system/restart-http-server-system))
(defonce _start-malli (m.dev/start!))

(defn spy [x] (clojure.pprint/pprint x) x)
