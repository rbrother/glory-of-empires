(ns glory-of-empires.players
  (:use clojure-common.utils)
  (:require [clojure.string :as str])
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.systems :as systems])
  (:require [glory-of-empires.strategies :as strategies])
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

(defn ready-color [ready content]
  (html/color-span (if ready "#00ff00" "#808080") content))

(defn- player-controls [race-id]
  (fn [ { controller :controller owner :owner } ]
    (or (= controller race-id) (= owner race-id) )))

(defn filter-player [ race-id items ] (filter (player-controls race-id) items))

(defn strategy-list-item [ { id :id ready :ready } ]
  [ :div (ready-color ready (str/capitalize (name id)))]   )

(defn- player-row-data [ board planets
                        { race-id :id tg :tg ac :ac pc :pc cc :command-pool sa :strategy-alloc fs :fleet-supply
                         strategies :strategies } ]
  (let [ player-systems (filter-player race-id board)
        player-planets (filter-player race-id planets) ]
    [(str/capitalize (name race-id))
     (fighter-image race-id)
     (player-flag race-id)
     (apply min (map :order strategies))
     (map strategy-list-item strategies)
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

(defn players-table [ amended-players board amended-planets ]
  (let [ header [ "Race" "Color" "Symbol" "Init" "Strategy"  "VP" "CC" "FS" "SA" "Systems" "Planets" "Res" "Inf" "TG" "Army Res" "Tech" "AC" "PC" ]
        rows (map #(player-row-data board amended-planets %) amended-players) ]
    (html/table { :class "data" } (concat (list header) rows) )))

(defn- ac-to-html [ id ]
  (let [ { descr :description play :play set :set } (ac/all-ac-types id) ]
    (list (str/capitalize (name id))
          (html/color-span "#909090" (str ": " descr " Play: " play)))  ))

(defn- planet-to-html [ { id :id fresh :fresh res :res inf :inf } ]
  (list (str/capitalize (name id)) " - " (html/color-span "green" res)
        " + " (html/color-span "#ff4040" inf) " - "
        (ready-color fresh (if fresh "Ready" "Exhausted"))   ))

(defn- player-html [ role all-planets { race-id :id acs :ac } ]
  { :pre [ (not (nil? race-id)) ] }
  (let [ show-all (or (= role :game-master) (= role race-id))
        race (races/all-races race-id)
        planets (filter-player race-id all-planets) ]
    (list
      [ :h3 (race :name) " - " (name race-id)
          (fighter-image race-id) (player-flag race-id) ]
      [ :div { :style "margin-left: 1cm;" }
        [ :p "Strategy cards: xxx, yyy" ]
        (if show-all
          (list "Action Cards: " (count acs) (html/ol (map ac-to-html acs)))
          "(hidden)")
        "Planets" (html/ol (map planet-to-html planets))
        "Tech" (html/ol ["a" "b" "c"])
       ]  )))

(defn amend-player [ { player-id :id :as player } strategies ]
  (assoc player :strategies (filter-player player-id strategies)))

(defn players-html [ { planets :planets strat :strategies players :players board :map } role ]
  (let [amended-planets (clojure.set/join (vals planets) systems/all-planets-set)
        amended-strategies (clojure.set/join strat strategies/all-strategies-arr)
        sorted-strategies (sort-by :order amended-strategies)
        player-order (->> sorted-strategies (map :owner) (filter identity) distinct)
        players-in-order (->> player-order (map #(players %)))
        amended-players (->> players-in-order (map #(amend-player % amended-strategies))) ]
    (list
      (players-table amended-players (vals board) amended-planets)
      (map (partial player-html role amended-planets) amended-players))))
