(ns nimaeskandary.migrations.interface)

(defprotocol Migrations
  (run [this])
  (rollback [this])
  (create [this name]))
