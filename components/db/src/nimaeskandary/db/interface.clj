(ns nimaeskandary.db.interface)

(defprotocol Database
  (execute [this q]))
