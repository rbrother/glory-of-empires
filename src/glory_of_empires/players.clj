(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships]))

(defn set-players [ player-ids game-state ]
  { :pre [ (every? #(contains? races/all-races %) player-ids) ] }
  (assoc game-state :players
    (->> player-ids
         (map (fn [id] { :id id } ))
         (index-by-id))))

(defn- player-html [ { id :id } ]
  { :pre [ (not (nil? id)) ] }
  (let [ race (races/all-races id)
         fighter-image (ships/ship-image-url { :type :fi } id) ]
    [ :div {}
      [ :h2 {}
        [ :span {} (race :name) ]
        [ :img { :src fighter-image } ] ] ] ))


(defn players-html [ players ]
  `[ :div {}
     [ :h1 {} "Players" ]
     ~@(map player-html (vals players)) ] )
