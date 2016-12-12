(ns glory-of-empires.view
  (:require [glory-of-empires.map-svg :as map-svg])
  (:require [glory-of-empires.players :as players]))

(defn board
  ( [ game ] (board game {}) )
  ( [ game opts ]
    (if (number? opts) (board { :scale opts })
      (let [ m (game :map) ]
        (if (or (nil? m) (empty? m)) "No map" (map-svg/render m opts))))))

(defn players [ game ] (players/players-html game))

(defn vertical [ game & views ]
  `[ :div {}
     ~@(map (fn [view] [ :div view] ) views) ] )

(defn horizontal [ game & views ]
  `[ :table {} [ :tr {}
                 ~@(map (fn [view] [ :td { :style "vertical-align: top;" } view] ) views) ]] )

(defn role-selector [ game ]
  (let [ ids (cons :game-master (players/ids game)) ]
    `[ :select { :id "role" :name "role" }
       ~@(map (fn [player] [ :option {} (str player) ]) ids) ] ))
