(ns glory-of-empires.game-state
  (:use clojure-common.utils)
  (:use clojure.java.io))

(defn new-game-state [ gm-password ]
  { :counter 0
    :gm-password gm-password
    :map {}
    :planets {}
    :units {}
    :players {}
    :ship-counters {} } )

(def games (atom { :sandbox (new-game-state "") }))

(def game-file-path "game-state.clj")

(defn game [ id ] (@games id))

(defn game-names [] (map str (keys @games)) )

(defn game-counter [] (fn [game] (get game :counter 0)))

(defn load-games []
  (if (.exists (as-file game-file-path))
      (reset! games (load-from-file game-file-path))
      nil))

(def save-agent (agent nil))

(defn- save-game [ agent-state state ]
  (write-to-file game-file-path state))

(defn- save-games-async [ data ] (send-off save-agent save-game data))

(defn swap-games [ swap-func] (save-games-async (swap! games swap-func)))

(defn- increment-counter [ game ]
  (assoc game :counter (inc (get game :counter 0))))

(defn- game-update-func [ game-id inner-func ]
  (fn [ games ]
    (update games game-id (comp increment-counter inner-func))))

(defn swap-game [ func game-id ] (swap-games (game-update-func game-id func)))

(defn create-game [ game-name gm-password ]
  (fn [ games ] (assoc games game-name (new-game-state gm-password))))
