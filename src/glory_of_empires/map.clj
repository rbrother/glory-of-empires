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

;-------------------- map query --------------------------

(defn get-loc-of [ board system-predicate ]
  (ffirst (filter (fn [ [ system-id system ] ] (system-predicate system)) board)))

; eg. :abyz-fria -> :a3
(defn get-system-loc [ board system-id ]
  (get-loc-of board (fn [ { system :system } ] (= system system-id))))

; eg. :fria -> :a3
(defn find-planet-loc [ board planet ]
  { :post [ (not (nil? %)) ] }
  (get-loc-of board (fn [ { planets :planets } ] (and planets (contains? planets planet)))))

;------------------- map operations -------------------------

(defn swap-piece-system [ piece system-id ]
  (let [ { planets :planets } (get-system system-id)
         ; Keep only planets names in the map, don't drag along static planet-info
         ; that we gan get from all-systems map when needed. This keeps the test-data manageable.
         empty-planets-map (map-map-values (fn [orig] {}) planets) ]
    (merge piece { :system system-id :planets empty-planets-map } )))

(defn swap-system [ board loc-id system-id ]
  { :pre [ (contains? board loc-id) ] }
  (update-in board [ loc-id ] swap-piece-system system-id))

(defn set-random-system [ piece ] (swap-piece-system piece (:id (rand-nth all-systems-arr))))

(defn random-systems [ board ] (map-map-values set-random-system board))

;-------------------- ships --------------------------

(defn new-unit-to-piece [ { system-id :id :as piece } loc-id owner type id ]
  (let [ unit { :type type :id id :owner owner } ]
    (if (= loc-id system-id)
      (-> piece
          (assoc :controller owner)
          (assoc-in [ :ships id ] unit ))
      (-> piece
          (assoc-in [ :planets loc-id :units id ] unit )))))

(defn new-unit-to-map [ board loc-id owner type id ]
  { :pre [ (ships/valid-unit-type? type) ] }
  (let [ system-id (if (contains? board loc-id) loc-id (find-planet-loc board loc-id)) ]
    (update-in board [ system-id ] new-unit-to-piece loc-id owner type id)))

(defn new-unit-index [ game-state type ]
  (inc (get-in game-state [ :ship-counters type ] 0)))

(defn new-unit [ loc-id owner type game-state ]
  { :pre [ (ships/valid-unit-type? type) ] }
    (let [ idx (new-unit-index game-state type)
           unit-id (keyword (str (name type) idx)) ]
      (-> game-state
        (assoc-in [ :ship-counters type ] idx )
        (update-in [ :map ] new-unit-to-map loc-id owner type unit-id))))

(defn new-units [ loc-id owner types game-state ]
  (let [ new-unit-of (fn [ new-game-state type] (new-unit loc-id owner type new-game-state )) ]
    (reduce new-unit-of game-state types)))

(defn del-unit-from-piece [ piece id ]
  (update-in piece [ :ships ] #(dissoc % id)))

(defn del-unit [ board id ]
  (map-map-values #(del-unit-from-piece % id) board))

(defn all-units [ board ] (apply merge (map :ships (vals board))))

(defn find-unit [ board id ] ((all-units board) id))

(defn move-unit [ board unit-id loc-id ]
   (let [ { owner :owner :as unit } (find-unit board unit-id) ]
     (-> board
         (del-unit unit-id)
         (assoc-in [ loc-id :ships unit-id ] unit)
         (assoc-in [ loc-id :controller ] owner))))

(defn move-units [ board unit-ids loc-id ]
  { :pre [ (sequential? unit-ids) ] }
  (let [ move-unit-to (fn [ new-board unit-id ] (move-unit new-board unit-id loc-id)) ]
    (reduce move-unit-to board unit-ids)))

;------------------ to svg ------------------------

(defn ship-group-svg [ [ group loc ] ] ; returns [ [:g ... ] [:g ... ] ... ]
  (if (empty? group) []
    (let [ ship (first group)
           next-loc (map + loc [50 0]) ]
      (conj (ship-group-svg [ (rest group) next-loc ] )
            (ships/svg ship loc)))))

(defn map-polar [ clock rel-distance ]
  (polar (- 180 (* clock 30)) (* rel-distance 0.5 tile-width)))

(def default-ship-locs [ (map-polar 8 0.6) (map-polar 0.5 0.4) ]) ; zero angle to down

(def planet-units-locs [ [ -20 -40 ] [ -20 40 ] ])

(defn group-ships [ ships group-locs ]
  (let [ ships-per-group (int (Math/ceil (/ (count ships) (count group-locs))))
         ship-groups (partition ships-per-group ships-per-group [] ships) ]
    (zip ship-groups group-locs)))

(defn ships-svg [ ships group-locs ] ; returns [ [:g ... ] [:g ... ] ... ]
  (let [ sorted-ships (sort-by :type ships)
         grouped-ships (group-ships sorted-ships group-locs) ]
    (mapcat ship-group-svg grouped-ships)))

(defn planet-units-svg [ { units :units :as planet } ]
  { :pre [ (not (nil? planet))] }
  (if (or (not units) (empty? units)) []
    (ships-svg (vals units) planet-units-locs)))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system loc-id :id controller :controller
                       ships :ships planets :planets } ]
  (let [ center (mul-vec tile-size 0.5)
         system-image ((get-system system-id) :image)
         ships-content (if (or (not ships) (empty? ships)) [] (ships-svg (vals ships) default-ship-locs)) ; TODO: make default locs dependent on number of planets (0, 1, 2, 3)
         planets-units (if (or (not planets) (empty? planets)) [] (mapcat planet-units-svg (vals planets)))
         tile-label (svg/double-text (str/upper-case (name loc-id)) (map-polar 9 0.8) {}) ]
    (svg/g { :translate (screen-loc logical-pos) } [
      (svg/image [ 0 0 ] tile-size (str ships/resources-url "Tiles/" system-image))
      (svg/g { :translate center }
         `[ ~@planets-units ~@ships-content ~tile-label ]) ] )))

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
