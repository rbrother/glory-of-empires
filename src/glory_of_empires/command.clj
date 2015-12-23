(ns glory-of-empires.command
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.game-state :as game-state])
  (:require [glory-of-empires.players :as players]))

;------------ command helpers ---------------

(defn- game [] @game-state/game)

(defn- run-command [ command ]
  (swap! game-state/game command)
  (game-state/save-game-async)
  "ok")

(defn- board-command [ command ]
  (run-command (fn [ game ] (update game :map command))))

(defn- make-board-command [ new-board ]
  (run-command
    (fn [ game ]
      (-> game
          (assoc :map new-board)
          (assoc :ship-counters {})))))

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

;------------ unit commands ------------------

(defn new-unit [ loc-id owner type ]
  { :pre [ (contains? (:players (game)) owner) ] }
    (run-command #(board/new-unit loc-id owner type % )))

(defn del-unit [ unit-id ]
  (board-command #(board/del-unit % unit-id)))

(defn move-unit [ unit-id dest ]
  (let [ unit-ids (if (sequential? unit-id) unit-id [ unit-id ] ) ]
    (board-command #(board/move-units % unit-ids dest))))
