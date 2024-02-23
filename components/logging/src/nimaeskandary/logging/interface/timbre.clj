(ns nimaeskandary.logging.interface.timbre
  (:require [nimaeskandary.logging.lib.timbre-logger :as timbre-logger]))

(defn create-timbre-logger [] (timbre-logger/->TimbreLogger nil))
