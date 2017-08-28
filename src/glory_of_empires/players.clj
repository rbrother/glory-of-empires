(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.ac :as ac])
  (:require [glory-of-empires.html :as html]))

(defn create-player [ race password ]
  { :id race :password password :tg 0 :ac [] :pc [] :tech [] } )

(defn password-valid? [ required-role game { role :role password :password } ]
  (or (and
        (= role :game-master)
        (= password (game :gm-password)))
      (and
        (= required-role :player)
        (= password (get-in game [ :players role :password])))))

(defn players [ game ] (vals (game :players)))

(defn player? [ game player ] (contains? (game :players) player))

(defn set-players [ game player-ids-and-passwords ]
  (assoc game :players
    (->> player-ids-and-passwords
         (partition 2)
         (map (fn [ [id pwd] ] { :id id :password pwd :ac [] :pc [] } ))
         (index-by-id))))

(defn- fighter-image [ race-id ] [ :img { :src (ships/ship-image-url :fi race-id) } ] )

(defn- player-flag [ race-id ] [ :img {:src (str html/resources-url "FlagWavy/Flag-Wavy-" (name race-id) ".png")} ] )

(defn- ac-to-html [ id ]
  (let [ { descr :description play :play set :set } (ac/all-ac-types id) ]
    [:span (name id)
          (html/color-span "#909090" (str ": " descr " Play: " play))]  ))

(defn- player-html [ role { race-id :id acs :ac } ]
  { :pre [ (not (nil? race-id)) ] }
  (let [ show-all (or (= role :game-master) (= role race-id))
        race (races/all-races race-id) ]
    [ :div
      [ :h3
        [ :span {} (str (name race-id) " - " (race :name)) ]
       (fighter-image race-id) ]
     (if show-all
       [:div
        [:p " Action Cards"]
        (html/ol (map ac-to-html acs))]
       [ :p "(hidden)" ]   ) ] ))

(defn- player-row-data [ { map :map planets :planets units :units } { race-id :id tg :tg ac :ac pc :pc } ]
  (let [ player-controls (fn [object] (= (:controller object) race-id))
        player-systems (->> map vals (filter player-controls))
        player-planets (->> planets vals (filter player-controls)) ]
    [ (name race-id)
      (fighter-image race-id)
      (player-flag race-id)
      "VP"
      (count player-systems)
      (count player-planets)
      "Res"
      "Inf"
      "Army Res"
      (or tg 0)
      "Techs"
      (count ac)
      (count pc)   ]))

(defn ids [ game-state ] (keys (game-state :players)))

(defn players-table [ game ]
  (let [ header [ "Race" "Color" "Symbol" "VP" "Systems" "Planets" "Res" "Inf" "Army Res" "TG" "Tech" "AC" "PC" ]
         rows (map #(player-row-data game %) (players game))
         pars `[ { :class "data" } ~header ~@rows ] ]
    (apply html/table pars) ))

(defn players-html [ game role ]
  `[ :div
     ~(players-table game)
     ~@(map (partial player-html role) (players game)) ] )
