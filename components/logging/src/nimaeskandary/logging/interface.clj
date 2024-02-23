(ns nimaeskandary.logging.interface)

(defprotocol Logger
  (trace
    [this message]
    [this message extra])
  (debug
    [this message]
    [this message extra])
  (info
    [this message]
    [this message extra])
  (warn
    [this message]
    [this message extra])
  (error
    [this error]
    [this error message]
    [this error message extra]))
