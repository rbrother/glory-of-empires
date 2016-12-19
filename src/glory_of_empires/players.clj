(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.html :as html]))

(defn create-player [ race password ]
  { :id race :password password :tg 0 :ac [] :pc [] } )

(defn password-valid? [ required-role game { role :role password :password } ]
  (or (and
        (= role :game-master)
        (= password (game :gm-password)))
      (and
        (= required-role :player)
        (= password (get-in game [ :players role :password])))))

(defn players [ game ] (vals (game :players)))

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
    [ :div
      [ :h3
        [ :span {} (str race-id " - " (race :name)) ]
        [ :img { :src fighter-image } ] ] ] ))

(defn- player-row-data [ { race-id :id tg :tg ac :ac pc :pc } ]
  [ race-id
    "Color"
    "Symbol"
    "VP"
    "Systems"
    "Planets"
    "Res"
    "Inf"
    (or tg 0)
    (count ac)
    (count pc) ] )

(defn ids [ game-state ] (keys (game-state :players)))

(defn players-table [ game ]
  (let [ header [ "Race" "Color" "Symbol" "VP" "Systems" "Planets" "Res" "Inf" "TG" "AC" "PC" ]
         rows (map player-row-data (players game))
         pars `[ { :class "data" } ~header ~@rows ] ]
    (apply html/table pars) ))

(defn players-html [ game ]
  `[ :div
     ~(players-table game)
     ~@(map player-html (players game)) ] )
