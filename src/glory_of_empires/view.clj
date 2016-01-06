(ns glory-of-empires.view
  (:require [glory-of-empires.map-svg :as map-svg])
  (:use glory-of-empires.game-state)
  (:require [glory-of-empires.players :as players]))

  (defn board
    ( [] (board {}) )
    ( [ opts ]
      (if (number? opts) (board { :scale opts })
        (let [ m (@game :map) ]
          (if (or (nil? m) (empty? m)) "No map" (map-svg/render m opts))))))

  (defn players [] (players/players-html (@game :players)))

  (defn vertical [ & views ]
    `[ :div {}
       ~@(map (fn [view] [ :div {} view] ) views) ] )

  (defn horizontal [ & views ]
    `[ :table {} [ :tr {}
        ~@(map (fn [view] [ :td { :style "vertical-align: top;" } view] ) views) ]] )
