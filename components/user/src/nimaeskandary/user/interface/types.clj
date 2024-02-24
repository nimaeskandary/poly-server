(ns nimaeskandary.user.interface.types)

(def User
  [:map
   [:id :uuid]
   [:username :string]
   [:email :string]])

(def CreateUser
  [:map
   [:username :string]
   [:email :string]])

(def create-user [:=> [:cat :map CreateUser] User])
(def get-user [:=> [:cat :map :uuid] User])
