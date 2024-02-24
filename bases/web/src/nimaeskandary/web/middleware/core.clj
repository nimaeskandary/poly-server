(ns nimaeskandary.web.middleware.core)

(defn wrap-system
  [handler system]
  (fn [req]
    (handler (assoc req :application/system system))))
