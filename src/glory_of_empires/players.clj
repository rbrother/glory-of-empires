(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships]))

(defn password-valid? [ required-role game { role :role password :password } ]
  (or (and
        (= role :game-master)
        (= password (game :gm-password)))
      (and
        (= required-role :player)
        (= password (get-in game [ :players role :password])))))

(defn set-players [ game player-ids-and-passwords ]
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

(defn players-html [ game ]
  `[ :div {}
     ~@(map player-html (vals (game :players))) ] )

