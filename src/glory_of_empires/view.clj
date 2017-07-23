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
      (fn [ game role ] (map-svg/render game opts))   )))

(defn players [ ]
  ^{ :require-role :player }
  (fn [ game role ] (players/players-html game role)))

(defn systems
  ( [] (systems 1.0) )
  ( [ size-ratio ] (fn [ game role ] (systems/all-systems-table size-ratio)))  )

;------------- widgets ------------------

(defn role-selector [ ]
  (fn [ game role ]
    (let [ ids (cons :game-master (players/ids game)) ]
      `[ :select { :id "role" :name "role" }
         ~@(map (fn [ player ] [ :option {} (str player) ]) ids) ] )))

;------------ composite views ----------------

(defn vertical [ & view-funcs ]
  ^{ :require-role :player }
  (fn [ game role ]
    (let [ view-to-html  (fn [view-func] [:div (view-func game role)]) ]
      `[:div ~@(map view-to-html view-funcs)])))

(defn horizontal [ & views ]
  ^{ :require-role :player }
  (fn [ game role ]
    (let [ view-to-html (fn [view-fn] [:td {:style "vertical-align: top;"} (view-fn game role)]) ]
      `[:table [:tr {} ~@(map view-to-html views)]])))

