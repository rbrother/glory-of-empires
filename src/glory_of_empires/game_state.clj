(ns glory-of-empires.game-state
  (:use clojure-common.utils)
  (:use clojure.java.io))

(def game (atom { :map {} } ))

(def game-file-path "game-state.clj")

(defn load-game []
  (if (.exists (as-file game-file-path))
      (reset! game (load-from-file game-file-path))
      nil))

(defn- save-game [ state ]
  (write-to-file game-file-path state))

(defn save-game-async []
  (let [ state @game ]
    (-> (Thread. (save-game state)) .start)))
