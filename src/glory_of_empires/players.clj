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

(defn- player-html [ { race-id :id } ]
  { :pre [ (not (nil? race-id)) ] }
  (let [ race (races/all-races race-id)
         fighter-image (ships/ship-image-url :fi race-id) ]
    [ :div {}
      [ :h3 {}
        [ :span {} (str race-id " - " (race :name)) ]
        [ :img { :src fighter-image } ] ] ] ))

(defn ids [ game-state ] (keys (game-state :players)))

(defn players-html [ game-state ]
  `[ :div {}
     ~@(map player-html (vals (game-state :players))) ] )
