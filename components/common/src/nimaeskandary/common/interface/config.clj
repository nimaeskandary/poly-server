(ns nimaeskandary.common.interface.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn read-config
  [location profile]
  (aero/read-config (io/resource location) {:profile profile}))
