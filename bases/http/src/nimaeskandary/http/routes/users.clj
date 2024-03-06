(ns nimaeskandary.http.routes.users
  (:require [nimaeskandary.user.interface.user-repository-proto :as
             user-repo-proto]
            [nimaeskandary.user.interface.types :as user-types]
            [ring.util.response :as resp]))

(defn create-user
  [{{:keys [body]} :parameters, {:keys [user-repo]} :application/system}]
  (resp/response (user-repo-proto/create-user user-repo body)))

(defn get-user
  [{{{:keys [id]} :path} :parameters, {:keys [user-repo]} :application/system}]
  (resp/response (user-repo-proto/get-user user-repo id)))

(def user-routes
  ["/users"
   [""
    {:post {:name ::create-user,
            :parameters {:body user-types/CreateUser},
            :responses {200 {:body user-types/User}},
            :handler create-user}}]
   ["/users/:id"
    {:get {:name ::get-user,
           :parameters {:path {:id uuid?}},
           :responses {200 {:body user-types/User}},
           :handler get-user}}]])
