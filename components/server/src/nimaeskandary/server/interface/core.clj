(ns nimaeskandary.server.interface.core
  (:require [nimaeskandary.server.http-kit :as http-kit]))

(defn create-http-kit-server
  [handler-fn]
  (http-kit/map->HttpKitServer {:handler-fn handler-fn}))
