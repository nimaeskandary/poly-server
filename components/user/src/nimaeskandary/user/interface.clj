(ns nimaeskandary.user.interface)

(defprotocol UserRepository
  (save-user [this user])
  (get-user [this user-id]))
