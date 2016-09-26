(ns glory-of-empires.game-state
  (:use clojure-common.utils)
  (:use clojure.java.io))

(def new-game-state
  { :map {}
    :players {}
    :ship-counters {} } )

(def game (atom new-game-state))

(def game-file-path "game-state.clj")

(defn game-counter [] (get @game :counter 0))

(defn load-game []
  (if (.exists (as-file game-file-path))
      (reset! game (load-from-file game-file-path))
      nil))

(def save-agent (agent nil))

(defn- save-game [ agent-state state ]
  (write-to-file game-file-path state))

(defn- save-game-async []
  (let [ state @game ]
    (send-off save-agent save-game state)))

(defn- increment-counter [ game ]
  (assoc game :counter (inc (get game :counter 0))))

(defn- game-update-func [ inner-func ]
  (fn [ game ] (increment-counter (inner-func game))))

(defn swap-game [ func ]
  (swap! game (game-update-func func))
  (save-game-async))
