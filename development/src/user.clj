(ns user
  (:require
    [system :as system]
    [malli.dev :as m.dev]))

(comment
  (m.dev/stop!)
  (system/restart-dev-system))

(system/restart-dev-system)
(m.dev/start!)

(comment (nimaeskandary.user.interface/save-user (:user-repo system/*system*) {:username "jdoe" :email "jdoe@email.com" :id (random-uuid)}))
