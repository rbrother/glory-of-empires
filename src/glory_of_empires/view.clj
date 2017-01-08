(ns glory-of-empires.view
  (:require [glory-of-empires.map-svg :as map-svg])
  (:require [glory-of-empires.players :as players])
  (:require [glory-of-empires.systems :as systems]))

; There functions return functions that construct the view for given game.
; By avoiding giving game as direct parameter here we can execute the
; view functions just as given by user eg. (view/board 5 6)

(defn board
  ( [ ] (board {}) )
  ( [ opts ]
    (if (number? opts) (board { :scale opts })
      ^{ :require-role :player }
      (fn [ game ] (map-svg/render game opts))   )))

(defn players [ ]
  ^{ :require-role :player }
  (fn [game] (players/players-html game)))

(defn systems
  ( [] (systems 1.0) )
  ( [ size-ratio ] (fn [game] (systems/all-systems-table size-ratio)))  )

;------------- widgets ------------------

(defn role-selector [ ]
  (fn [ game ]
    (let [ ids (cons :game-master (players/ids game)) ]
      `[ :select { :id "role" :name "role" }
         ~@(map (fn [player] [ :option {} (str player) ]) ids) ] )))

;------------ composite views ----------------

(defn vertical [ & views ]
  ^{ :require-role :player }
  (fn [ game ]
    `[ :div
       ~@(map (fn [view] [ :div (view game)] ) views) ] ))

(defn horizontal [ & views ]
  ^{ :require-role :player }
  (fn [ game ]
    `[ :table [ :tr {}
                   ~@(map (fn [view-fn] [ :td { :style "vertical-align: top;" } (view-fn game)] ) views) ]] ))

