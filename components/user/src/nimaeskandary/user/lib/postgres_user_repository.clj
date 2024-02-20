(ns nimaeskandary.user.lib.postgres-user-repository
  (:require
    [nimaeskandary.user.interface.spec :as spec]
    [nimaeskandary.db.interface :as db]
    [honey.sql :as sql]
    [honey.sql.helpers :as h]
    [malli.core :as m]))

(defn save-user [{:keys [app-db]} user] (->> (-> (h/insert-into :users)
                                                 (h/values [user])
                                                 (h/returning :id :email :username)
                                                 (sql/format))
                                             (db/execute app-db)
                                             first))
(m/=> save-user spec/save-user)

(defn get-user [{:keys [app-db]} user-id] (->> (-> (h/select :id :username :email)
                                                   (h/from :users)
                                                   (h/where [:= :id user-id])
                                                   (sql/format))
                                               (db/execute app-db)
                                               first))
(m/=> get-user spec/get-user)
