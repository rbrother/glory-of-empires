(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.players :as players]))

; There commands do not actually do anything, but they
; return game-updating func for the specified command

;------------ command helpers ---------------

(defn- board-command [ board-func ] (fn [ game ] (update game :map board-func)))

(defn- make-board-command [ new-board ]
  ^{ :require-role :game-master }
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
  ^{ :require-role :game-master }
  (fn [ game ] (players/set-players game player-ids-and-passwords)))

;------------ unit commands ------------------

(defn new-unit [ loc-id owner type ]
  (let [ types (if (sequential? type) type [ type ]) ]
    #(board/new-units loc-id owner types % )))

(defn del-unit [ unit-id ]
  (board-command #(board/del-unit % unit-id)))

(defn move-unit [ unit-id dest ]
  (let [ unit-ids (if (sequential? unit-id) unit-id [ unit-id ] ) ]
    (board-command #(board/move-units % unit-ids dest))))

