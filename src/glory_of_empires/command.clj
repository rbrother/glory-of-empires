(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.game-state :as game-state]))

;------------ command helpers ---------------

(defn- run-command [ command ]
  (swap! game-state/game command)
  "ok")

(defn- board-command [ command ]
  (run-command (fn [ game ] (update game :map command))))

(defn- make-board-command [ new-board ]
  (run-command (fn [ state ] (assoc state :map new-board))))

;----------- commands --------------------

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
  (board-command #(board/swap-system % loc-id system-id)))
