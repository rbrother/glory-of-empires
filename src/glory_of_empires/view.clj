(ns glory-of-empires.view
  (:use glory-of-empires.map)
  (:use glory-of-empires.game-state))

  (defn board
    ( [] (board {}) )
    ( [ opts ]
      (if (number? opts) (board { :scale opts })
        (let [ m (@game :map) ]
          (if (nil? m) "No map" (map-to-svg m opts))))))
