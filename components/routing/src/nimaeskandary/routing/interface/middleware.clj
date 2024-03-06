(ns nimaeskandary.routing.interface.middleware)

(def wrap-system
  {:name ::wrap-system,
   :description "add system map to request",
   :wrap (fn [handler system]
           (fn [req] (handler (assoc req :application/system system))))})
