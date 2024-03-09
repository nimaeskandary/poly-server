(ns nimaeskandary.config.interface.core
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn http-server-config
  [profile]
  (aero/read-config (io/resource "config/http-server.edn") {:profile profile}))
