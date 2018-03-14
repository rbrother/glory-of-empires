(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [clojure.string :as str])
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.systems :as systems])
  (:require [glory-of-empires.ac :as ac])
  (:require [glory-of-empires.html :as html]))

(defn create-player [ race password ]
  { :id race :password password :command-pool 3 :strategy-alloc 2 :fleet-supply 3 :tg 0 :ac [] :pc [] :tech [] } )

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

(defn ids [ game-state ] (keys (game-state :players)))

(defn- fighter-image [ race-id ] [ :img { :src (ships/ship-image-url :fi race-id) } ] )

(defn- player-flag [ race-id ] [ :img {:src (str html/resources-url "FlagWavy/Flag-Wavy-" (name race-id) ".png")} ] )

(defn- player-controls [race-id] (fn [ { controller :controller } ] (= controller race-id)))

(defn filter-player [ race-id items ] (filter (player-controls race-id) items))

(defn- player-row-data [ { board :map units :units } planets
                        { race-id :id tg :tg ac :ac pc :pc cc :command-pool sa :strategy-alloc fs :fleet-supply } ]
  (let [player-systems (->> board vals (filter-player race-id))
        player-planets (filter-player race-id planets) ]
    [(str/capitalize (name race-id))
     (fighter-image race-id)
     (player-flag race-id)
     "VP"
     cc
     fs
     sa
     (count player-systems)
     (count player-planets)
     (->> player-planets (map :res) (reduce +))
     (->> player-planets (map :inf) (reduce +))
     (or tg 0)
     "Army Res"
     "Techs"
     (count ac)
     (count pc)   ]))

(defn players-table [ game amended-planets ]
  (let [ header [ "Race" "Color" "Symbol" "VP" "CC" "FS" "SA" "Systems" "Planets" "Res" "Inf" "TG" "Army Res" "Tech" "AC" "PC" ]
        rows (map #(player-row-data game amended-planets %) (players game)) ]
    (html/table { :class "data" } (concat (list header) rows) )))

(defn- ac-to-html [ id ]
  (let [ { descr :description play :play set :set } (ac/all-ac-types id) ]
    (list (str/capitalize (name id))
          (html/color-span "#909090" (str ": " descr " Play: " play)))  ))

(defn- planet-to-html [ { id :id fresh :fresh res :res inf :inf } ]
  (list (str/capitalize (name id)) " - " (html/color-span "green" res)
        " + " (html/color-span "#ff4040" inf) " - "
        (if fresh (html/color-span "#00ff00" "Refreshed") (html/color-span "#808080" "Exhausted")))  )

(defn- player-html [ role all-planets { race-id :id acs :ac } ]
  { :pre [ (not (nil? race-id)) ] }
  (let [ show-all (or (= role :game-master) (= role race-id))
        race (races/all-races race-id)
        planets (filter-player race-id all-planets) ]
    (list
      [ :h3 (race :name) " - " (name race-id)
          (fighter-image race-id) (player-flag race-id) ]
      [ :div { :style "margin-left: 1cm;" }
        (if show-all
          (list "Action Cards: " (count acs) (html/ol (map ac-to-html acs)))
          "(hidden)")
        "Planets" (html/ol (map planet-to-html planets))]  )))

(defn players-html [ { planets :planets :as game} role ]
  (let [ amended-planets (clojure.set/join (vals planets) systems/all-planets-set) ]
    (list
      (players-table game amended-planets)
      (map (partial player-html role amended-planets) (players game)))))
