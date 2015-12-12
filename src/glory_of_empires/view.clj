(ns glory-of-empires.view
  (:require [glory-of-empires.map :as board])
  (:use glory-of-empires.game-state)
  (:require [glory-of-empires.players :as players]))

  (defn board
    ( [] (board {}) )
    ( [ opts ]
      (if (number? opts) (board { :scale opts })
        (let [ m (@game :map) ]
          (if (or (nil? m) (empty? m)) "No map" (board/map-to-svg m opts))))))

  (defn players [] (players/players-html (@game :players)))
