(ns nimaeskandary.web.routes.users
  (:require
    [nimaeskandary.user.interface :as user.interface]
    [nimaeskandary.logging.interface :as log]
    [compojure.coercions :refer [as-uuid]]
    [compojure.core :refer [GET POST]]
    [ring.util.response :as resp]))

(defn create-user [{{:keys [username email]} :payload
                    {:keys [user-repo]} :system}]
  (user.interface/save-user user-repo {:username username :email email :id (random-uuid)}))

(defn get-user [{{:keys [id]} :params
                 {:keys [user-repo logger]} :system
                 :as req}]
  (resp/response (user.interface/get-user user-repo (parse-uuid id))))

(def user-routes
  [(POST "/users" [] #'create-user)
   (GET "/users/:id" [id :<< as-uuid] #'get-user)])
