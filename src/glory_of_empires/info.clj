(ns glory-of-empires.info
  (:use glory-of-empires.game-state))

  ; Routines for client to ask specific values from server, not views.
  ; Return strings from these, so will be passes through xml-to-text engine

  (defn view-counter [] (str (game-counter)))

