(ns glory-of-empires.game-state
  (:use clojure-common.utils))

(def game (atom { } ))

(def game-file-path "game-state.clj")

(defn load-game [] (reset! game (load-from-file game-file-path)))

(defn- save-game [ state ]
  (write-to-file game-file-path state))

(defn save-game-async []
  (let [ state @game ]
    (-> (Thread. (save-game state)) .start)))
