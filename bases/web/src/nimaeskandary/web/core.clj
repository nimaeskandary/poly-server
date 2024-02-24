(ns nimaeskandary.web.core
  (:require [nimaeskandary.web.routes.users :refer [user-routes]]
            [nimaeskandary.web.middleware.core :as middleware.core]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :refer [routes]]))

(def all-routes (routes user-routes))

(defn route-handler [system] (-> all-routes
                                 (wrap-json-body {:keywords? true})
                                 wrap-json-response
                                 (middleware.core/wrap-system system)))
