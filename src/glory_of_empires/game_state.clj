(ns glory-of-empires.game-state
  (:use clojure-common.utils)
  (:use clojure.java.io))

(def new-game-state
  { :counter 0
    :gm-password ""
    :map {}
    :players {}
    :ship-counters {} } )

(def games (atom { :sandbox new-game-state }))

(def game-file-path "game-state.clj")

(defn game [ id ] (@games id))

(defn game-counter [ game ] (get game :counter 0))

(defn load-games []
  (if (.exists (as-file game-file-path))
      (reset! games (load-from-file game-file-path))
      nil))

(def save-agent (agent nil))

(defn- save-game [ agent-state state ]
  (write-to-file game-file-path state))

(defn- save-game-async []
  (let [ state @games ]
    (send-off save-agent save-game state)))

(defn- increment-counter [ game ]
  (assoc game :counter (inc (get game :counter 0))))

(defn- game-update-func [ game-id inner-func ]
  (fn [ games ]
    (update games game-id (comp increment-counter inner-func))))

(defn swap-game [ func game-id ]
  (swap! games (game-update-func game-id func))
  (save-game-async))
