(ns glory-of-empires.command
  (:require [clojure-common.utils :as utils])
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.players :as players]) )

; There commands do not actually do anything, but they
; return game-updating func for the specified command

;------------ command helpers ---------------

; The whole planet-list is re-created when board is modified, so board modification
; should not be done after game starts and there is some info on planets
(defn- update-planets [ { board :map :as game } ]
  (let [ all-planets
         (->>  board (vals) (map :planets)
               (map seq) (flatten) (remove nil?)
               (map (fn [id] { :id id :controller nil }))  ) ]
    (assoc game :planets (utils/index-by-id all-planets))  ))

(defn- board-command [ board-func ]
  (fn [ game ] (update-planets (update game :map board-func))))

(defn- make-board-command [ new-board ]
  ^{ :require-role :game-master }
  (fn [ game ] (update-planets (merge game { :map new-board :ship-counters {} } ))))

;----------- map commands --------------------

(defn round-board
  ( [] (round-board 3) )
  ( [ size ]
    (make-board-command (board/round-board size))))

(defn rect-board
  ( [] (rect-board 3 3) )
  ( [ width height ]
    (make-board-command (board/rect-board width height))))

(defn set-systems-random [ & opt ] (board-command #(apply board/random-systems % opt)))

(defn set-system [ loc-id system-id ]
  (board-command #(board/swap-system % [ loc-id system-id ] )))

(defn del-system [ loc-id ]
  (board-command #(dissoc % loc-id)))

;------------- players -----------------

(defn set-players [ & player-ids-and-passwords ]
  ^{ :require-role :game-master }
  (fn [ game ] (players/set-players game player-ids-and-passwords)))

;------------ unit commands ------------------

(defn new-unit [ loc-id owner type ]
  (let [ types (if (sequential? type) type [ type ]) ]
    #(board/new-units loc-id owner types % )))

(defn del-unit [ unit-id ]
  (fn [game] (board/del-unit unit-id game)))

(defn move-unit [ unit-id dest ]
  (let [ unit-ids (if (sequential? unit-id) unit-id [ unit-id ] ) ]
    (fn [game] (board/move-units unit-ids dest game))) )

