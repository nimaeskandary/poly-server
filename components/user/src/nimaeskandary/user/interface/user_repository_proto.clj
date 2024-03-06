(ns nimaeskandary.user.interface.user-repository-proto)

(defprotocol UserRepository
  (create-user [this user])
  (get-user [this user-id]))
