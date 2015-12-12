(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.game-state :as game-state])
  (:require [glory-of-empires.players :as players]))

;------------ command helpers ---------------

(defn- run-command [ command ]
  (swap! game-state/game command)
  (game-state/save-game-async)
  "ok")

(defn- board-command [ command ]
  (run-command (fn [ game ] (update game :map command))))

(defn- make-board-command [ new-board ]
  (run-command (fn [ game ] (assoc game :map new-board))))

;----------- map commands --------------------

(defn round-board
  ( [] (round-board 3) )
  ( [ size ]
    (make-board-command (board/round-board size))))

(defn rect-board
  ( [] (rect-board 3 3) )
  ( [ width height ]
    (make-board-command (board/rect-board width height))))

(defn set-systems-random [] (board-command board/random-systems))

(defn set-system [ loc-id system-id ]
  (board-command #(board/swap-system loc-id system-id %)))

(defn del-system [ loc-id ]
  (board-command #(dissoc % loc-id)))

;------------- players -----------------

(defn set-players [ & player-ids ]
  (run-command #(players/set-players player-ids %)))

;------------ ship commands ------------------

(defn new-ship [ loc-id owner type ]
  { :pre [ (contains? (game-state/game :players) owner) ] }
  (board-command #(board/new-ship loc-id owner type % )))

(defn del-ship [ ship-id ] nil )
