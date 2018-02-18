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
    :ship-counters {}
    :history [] } )

(def games (atom { :sandbox (new-game-state "") }))

(def game-file-path "game-state.clj")

(defn game [ id ] (@games id))

(defn game-counter [] (fn [game role] (get game :counter 0)))

(defn load-games []
  (if (.exists (as-file game-file-path))
    (do (println "loading games from file")
        (reset! games (load-from-file game-file-path)))
    (println "!! Game file not found")))

(defn get-games [] @games) ; avoid calling this directly, use only from 1 place

(def save-agent (agent nil))

(defn- save-game [ agent-state state ]
  (println "writing games to file...")
  (write-to-file game-file-path state))

(defn- save-games-async [ data ] (send-off save-agent save-game data))

(defn swap-games [ swap-func] (save-games-async (swap! games swap-func)))

(defn- increment-counter [ game ]
  (assoc game :counter (inc (get game :counter 0))))

(defn- rec-history [ history-item ]
  (fn [game]
    (let [ new-counter (inc (get game :counter 0)) ]
      (-> game
          (assoc :counter new-counter)
          (update :history conj (assoc history-item :counter new-counter))   ))))

(defn- game-update-func [ inner-func history-item game-id ]
  (fn [ games ]
    (update games game-id (comp (rec-history history-item) inner-func))))

(defn swap-game [ func history-item game-id ]
  (swap-games (game-update-func func history-item game-id)))

(defn create-game [ game-name gm-password ]
  (fn [ games ] (assoc games game-name (new-game-state gm-password))))
