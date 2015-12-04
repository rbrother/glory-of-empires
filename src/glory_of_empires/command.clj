(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.game-state :as game-state]))

(defn run-command [ command ]
  (swap! game-state/game command)
  "ok")

(defn board-command [ command ]
  (run-command (fn [ game ] (update game :map command))))

(defn round-board
  ( [] (round-board 3) )
  ( [ size ]
    (run-command
      (fn [ state ] (assoc state :map (board/round-board size))))))

(defn set-systems-random []
  (board-command
    (fn [ board ] (map board/set-random-system board))))

(defn set-system [ loc-id system-id ]
  (board-command #(board/swap-system % loc-id system-id)))
