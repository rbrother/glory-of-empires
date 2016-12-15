(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.players :as players]))

; There commands do not actually do anything, but they
; return game-updating func for the specified command

;------------ command helpers ---------------

(defn- board-command [ command ] (fn [ game ] (update game :map command)))

(defn- make-board-command [ new-board ]
  (fn [ game ] (merge game { :map new-board :ship-counters {} } )))

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
  (fn [ game ] (players/set-players player-ids-and-passwords game)))

;------------ unit commands ------------------

(defn new-unit [ loc-id owner type ]
  (let [ types (if (sequential? type) type [ type ]) ]
    #(board/new-units loc-id owner types % )))

(defn del-unit [ unit-id ]
  (board-command #(board/del-unit % unit-id)))

(defn move-unit [ unit-id dest ]
  (let [ unit-ids (if (sequential? unit-id) unit-id [ unit-id ] ) ]
    (board-command #(board/move-units % unit-ids dest))))

