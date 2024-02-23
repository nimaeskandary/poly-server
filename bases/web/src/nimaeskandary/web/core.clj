(ns nimaeskandary.web.core
  (:require [nimaeskandary.web.routes.users :refer [user-routes]]
            [nimaeskandary.web.middleware.core :as middleware.core]
            [compojure.core :refer [routes]]))

(defn route-handler [system] (middleware.core/wrap-system (apply routes user-routes) system))
