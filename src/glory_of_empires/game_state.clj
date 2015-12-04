(ns glory-of-empires.game-state
  (:use clojure-common.utils))

(def game (atom { } ))

(defn load-game [] (reset! game (load-from-file "game-state.clj")))
