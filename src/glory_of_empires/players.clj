(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships]))

(defn set-players [ player-ids-and-passwords game ]
  (assoc game :players
    (->> player-ids-and-passwords
         (partition 2)
         (map (fn [ [id pwd] ] { :id id :password pwd } ))
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
