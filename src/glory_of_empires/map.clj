(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure.test)
  (:use glory-of-empires.systems)
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.svg :as svg])
  (:use glory-of-empires.races)
  (:gen-class))

; -------------------------- map ---------------------------------

(def tile-width 432 )
(def tile-height 376 )
(def tile-size [ tile-width tile-height ] )

(defn- screen-loc [ [ logical-x logical-y ] ]
  { :pre [ (integer? logical-x) (integer? logical-y) ] }
  [ (* logical-x tile-width 0.75)
    (* (last tile-size) (+ (* logical-x 0.5) logical-y)) ] )

(defn screen-locs [ map-pieces ]
  (->> map-pieces (map :logical-pos) (map screen-loc)))

(def good-letters [ "a", "b", "c", "d", "e", "f", "g",
              "h", "j", "k", "m", "n", "p",
              "r", "s", "t", "u", "v", "z", "y", "z" ] )

(defn location-id [ [ x y ] [ min-x min-y ] ]
  (keyword (str (good-letters (- x min-x)) (+ 1 (- y min-y)))))

(defn- logical-distance [ [ logical-x logical-y ] ]
  (let [ abs-x (Math/abs logical-x)
         abs-y (Math/abs logical-y) ]
    (if (pos? (* logical-x logical-y)) (+ abs-x abs-y) (max abs-x abs-y))))

(def setup-tiles [ :setup-red :setup-yellow :setup-light-blue :setup-medium-blue :setup-dark-blue ] )

(defn- setup-system [ pos tile-index ]
  { :logical-pos pos
    :system (nth setup-tiles (mod tile-index 5)) } )

(defn amend-tile-ids [ map-pieces ]
  (let [ min-loc (min-pos (map :logical-pos map-pieces))
         amend-tile-id (fn [tile] (assoc tile :id (location-id (:logical-pos tile) min-loc))) ]
    (map amend-tile-id map-pieces)))

(defn- make-board [ initial-range-size piece-filter ]
  (let [ a-range (range (- initial-range-size) (inc initial-range-size)) ]
    (->> (range2d a-range a-range)
         (filter piece-filter)
         (map (fn [ pos ] (setup-system pos (logical-distance pos))))
         (amend-tile-ids)
         (index-by-id))))

(defn round-board [ rings ]
  (make-board rings (fn [ pos ] (< (logical-distance pos) rings))))

(defn rect-board [ width height ]
  (let [ pixel-size [ (* width tile-width 0.75) (* height tile-height) ]
         bounding-rect [ (mul-vec pixel-size -0.5) (mul-vec pixel-size 0.5) ] ]
    (make-board (+ width height) (fn [ pos ] (inside-rect? (screen-loc pos) bounding-rect)))))

;------------------- map operations -------------------------

(defn set-random-system [ piece ] (assoc piece :system (:id (rand-nth all-systems-arr))))

(defn random-systems [ board ] (map-map-values set-random-system board))

(defn swap-system [ board loc-id system-id ]
  { :pre [ (contains? board loc-id) ] }
  (assoc-in board [ loc-id :system ] system-id))

;-------------------- ships --------------------------

(defn new-unit-to-piece [ { controller :controller ships :ships :as piece } owner type id ]
  (let [ controller (piece :controller) ]
    (if (and (not (empty? ships)) (not= controller owner))
      (throw (Exception. "Cannot add ship of different owner"))
      (-> piece
          (assoc :controller owner)
          (assoc-in [ :ships id ] { :type type :id id } )))))

(defn new-unit-to-map [ board loc-id owner type id ]
  { :pre [ (contains? board loc-id)
           (ships/valid-unit-type? type) ] }
    (update-in board [ loc-id ] new-unit-to-piece owner type id))

(defn new-unit-index [ game-state type ]
  (inc (get-in game-state [ :ship-counters type ] 0)))

(defn new-unit [ loc-id owner type game-state ]
  { :pre [ (ships/valid-unit-type? type) ] }
    (let [ idx (new-unit-index game-state type)
           ship-id (keyword (str (name type) idx)) ]
      (-> game-state
        (assoc-in [ :ship-counters type ] idx )
        (update-in [ :map ] new-unit-to-map loc-id owner type ship-id))))

(defn del-unit-from-piece [ piece id ]
  (update-in piece [ :ships ] #(dissoc % id)))

(defn del-unit [ board id ]
  (map-map-values #(del-unit-from-piece % id) board))

;------------------ to svg ------------------------

(defn ship-group-svg [ [ group loc ] race ] ; returns [ [:g ... ] [:g ... ] ... ]
  (if (empty? group) []
    (let [ ship (first group)
           next-loc (map + loc [50 0]) ]
      (conj (ship-group-svg [ (rest group) next-loc ] race)
            (ships/svg ship race loc)))))

(defn map-polar [ clock rel-distance ]
  (polar (- 180 (* clock 30)) (* rel-distance 0.5 tile-width)))

(def default-ship-locs [ (map-polar 8 0.6) (map-polar 0.5 0.4) ]) ; zero angle to down

(defn group-ships [ ships group-locs ]
  (let [ ships-per-group (int (Math/ceil (/ (count ships) (count group-locs))))
         ship-groups (partition ships-per-group ships-per-group [] ships) ]
    (zip ship-groups group-locs)))

(defn ships-svg [ controller ships ] ; returns [ [:g ... ] [:g ... ] ... ]
  { :pre [ (not (nil? controller))
           (contains? all-races controller) ] }
  (let [ sorted-ships (sort-by :type ships)
         group-locs default-ship-locs ; TODO: make default locs dependent on number of planets (0, 1, 2, 3)
         grouped-ships (group-ships sorted-ships group-locs) ]
    (mapcat #(ship-group-svg % controller) grouped-ships)))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system id :id controller :controller ships :ships } ]
  (let [ center (mul-vec tile-size 0.5)
         system (get-system system-id)
         ships-content (if (or (nil? ships) (empty? ships)) [] (ships-svg controller (vals ships))) ]
    (svg/g { :translate (screen-loc logical-pos) } [
      (svg/image [ 0 0 ] tile-size (str ships/resources-url "Tiles/" (system :image)))
      (svg/g { :translate center }
         (conj ships-content (svg/double-text (str/upper-case (name id)) (map-polar 9 0.8) {}) )) ] )))

(defn bounding-rect [ map-pieces ]
  (let [ s-locs (screen-locs map-pieces) ]
    [ (min-pos s-locs) (map + (max-pos s-locs) tile-size) ] ))

(defn rect-size [ [ min-corner max-corner ] ] (map - max-corner min-corner))

(defn map-to-svg
  ( [ board ] (map-to-svg board {} ))
  ( [ board opts ]
    (let [ map-pieces (vals board)
           scale (get opts :scale 0.5)
           bounds (bounding-rect map-pieces)
           min-corner (first bounds)
           svg-size (mul-vec (rect-size bounds) scale) ]
      (svg/svg svg-size (svg/g { :scale scale :translate (mul-vec min-corner -1.0) } (map piece-to-svg map-pieces) )))))

(run-tests)
