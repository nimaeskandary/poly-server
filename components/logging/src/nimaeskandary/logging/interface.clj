(ns nimaeskandary.logging.interface)

(defprotocol Logger
  (start [this])
  (stop [this])
  (trace [this message]
         [this message extra])
  (debug [this message]
         [this message extra])
  (info [this message]
        [this message extra])
  (warn [this message]
        [this message ex]
        [this message ex extra])
  (error [this message]
         [this message ex]
         [this message ex extra]))
