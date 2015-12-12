(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races]))

(defn set-players [ player-ids game-state ]
  { :pre [ (every? #(contains? races/all-races %) player-ids) ] }
  (assoc game-state :players
    (->> player-ids
         (map (fn [id] { :id id } ))
         (index-by-id))))

(defn- player-html [ { id :id } ]
  { :pre [ (not (nil? id)) ] }
  (let [ race (races/all-races id)]
    [ :div {}
      [ :h2 {} (race :name) ] ] ))

(defn players-html [ players ]
  (let [ content (map player-html (vals players)) ]
  `[ :div {} [ :h1 {} "Players" ] ~@content ] ))
