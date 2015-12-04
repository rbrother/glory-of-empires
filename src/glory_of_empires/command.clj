(ns glory-of-empires.command
  (:use glory-of-empires.map)
  (:require [glory-of-empires.game-state :as game-state]))

(defn run-command [ command ]
  (swap! game-state/game command)
  "ok")

(defn random-map
  ( [] (random-map { :size 3 } ))
  ( [ opts ]
    (run-command
      (fn [ state ] (assoc state :map (make-random-map (get opts :size 3)))))))
