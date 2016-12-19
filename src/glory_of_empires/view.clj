(ns glory-of-empires.view
  (:require [glory-of-empires.map-svg :as map-svg])
  (:require [glory-of-empires.players :as players]))

; There functions return functions that construct the view for given game.
; By avoiding giving game as direct parameter here we can execute the
; view functions just as given by user eg. (view/board 5 6)

(defn board
  ( [ ] (board {}) )
  ( [ opts ]
    (if (number? opts) (board { :scale opts })
      ^{ :require-role :player }
      (fn [ game ]
        (let [ m (game :map) ]
          (if (or (nil? m) (empty? m)) "No map" (map-svg/render m opts))   )))))

(defn players [ ]
  ^{ :require-role :player }
  (fn [game] (players/players-html game)))

(defn vertical [ & views ]
  ^{ :require-role :player }
  (fn [ game ]
    `[ :div {}
       ~@(map (fn [view] [ :div view] ) views) ] ))

(defn horizontal [ & views ]
  ^{ :require-role :player }
  (fn [ game ]
    `[ :table {} [ :tr {}
                   ~@(map (fn [view] [ :td { :style "vertical-align: top;" } view] ) views) ]] ))

(defn role-selector [ ]
  (fn [ game ]
    (let [ ids (cons :game-master (players/ids game)) ]
      `[ :select { :id "role" :name "role" }
         ~@(map (fn [player] [ :option {} (str player) ]) ids) ] )))
