(ns nimaeskandary.user.interface)

(defprotocol UserRepository
  (create-user [this user])
  (get-user [this user-id]))
