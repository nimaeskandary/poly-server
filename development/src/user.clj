(ns user
  (:require [system :as system]
            [malli.dev :as m.dev]))

(comment
  (m.dev/start!)
  (m.dev/stop!)
  (system/restart-dev-system))

(defonce _start-system (system/restart-dev-system))
(defonce _start-malli (m.dev/start!))

(defn spy [x] (clojure.pprint/pprint x) x)

(comment
  (nimaeskandary.user.interface/save-user
   (:user-repo system/*system*)
   {:username "jdoe", :email "jdoe@email.com", :id (random-uuid)}))
