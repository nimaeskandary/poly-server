(ns dev.user
  (:require
    [dev.system :as system]
    [malli.dev :as m.dev]))

(comment
  (m.dev/start!)
  (m.dev/stop!))

(defn restart-dev-system [] (system/stop-system) (system/init-dev-system) (system/start-system))
