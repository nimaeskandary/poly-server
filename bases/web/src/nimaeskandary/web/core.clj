(ns nimaeskandary.web.core
  (:require [nimaeskandary.web.routes.users :refer [user-routes]]
            [nimaeskandary.web.middleware.core :as middleware.core]
            [reitit.ring.coercion :as ring.coercion]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as middleware.muuntaja]
            [reitit.coercion.malli :as coercion.malli]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [malli.util :as m.util]
            [muuntaja.core :as muuntaja]))

(defn app
  [{:keys [system dev?]}]
  (let [handler
        #(ring/ring-handler
          (ring/router
           [["/api" [user-routes]]
            ["/swagger.json"
             {:get {:no-doc true,
                    :swagger {:info {:title "Poly Server"}, :basePath "/"},
                    :handler (swagger/create-swagger-handler)}}]]
           {:data
            {;; for json response encoding
             :muuntaja muuntaja/instance,
             :middleware [[middleware.core/wrap-system system]
                          ;; for json response encoding
                          middleware.muuntaja/format-middleware
                          ;; for malli coercions
                          ring.coercion/coerce-exceptions-middleware
                          ring.coercion/coerce-request-middleware
                          ring.coercion/coerce-response-middleware],
             :coercion
             (coercion.malli/create
              {:transformers
               {:body {:default coercion.malli/default-transformer-provider,
                       :formats {"application/json"
                                 coercion.malli/json-transformer-provider}},
                :string {:default coercion.malli/string-transformer-provider},
                :response {:default coercion.malli/default-transformer-provider,
                           :formats
                           {"application/json"
                            coercion.malli/json-transformer-provider}}},
               :error-keys #{:humanized},
               :lite true,
               :compile m.util/closed-schema,
               :validate true,
               :strip-extra-keys true,
               :default-values true,
               :options nil})}})
          (ring/routes (swagger-ui/create-swagger-ui-handler {:path
                                                              "/api-docs"})
                       (ring/create-default-handler)))]
    (if dev? (ring/reloading-ring-handler handler) (handler))))
