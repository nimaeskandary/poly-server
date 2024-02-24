(ns nimaeskandary.web.routes.users
  (:require
    [nimaeskandary.user.interface :as user.interface]
    [compojure.coercions :refer [as-uuid]]
    [compojure.core :refer [GET POST defroutes]]
    [ring.util.response :as resp]))

(defn create-user [{{:keys [username email]} :payload
                    {:keys [user-repo]} :application/system}]
  (user.interface/save-user user-repo {:username username :email email :id (random-uuid)}))

(defn get-user [{{:keys [id]} :params
                 {:keys [user-repo]} :application/system}]
  (-> (resp/response (user.interface/get-user user-repo (parse-uuid id)))
      (resp/content-type "application/json")))

(defroutes user-routes
  (POST "/users" [] #'create-user)
  (GET "/users/:id" [id :<< as-uuid] #'get-user))
