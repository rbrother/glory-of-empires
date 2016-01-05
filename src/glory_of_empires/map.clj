(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure.test)
  (:require [glory-of-empires.systems :as systems])
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
  { :pre [ (not (nil? system-id))
           (not (nil? (systems/get-system system-id))) ]}
  (let [ planets ((systems/get-system system-id) :planets)
         ; Keep only planets names in the map, don't drag along static planet-info
         ; that we gan get from all-systems map when needed. This keeps the test-data manageable.
         empty-planets-map (if planets (map-map-values (fn [_] {}) planets) {}) ]
    (merge piece { :system system-id :planets empty-planets-map } )))

(defn swap-system [ board loc-id system-id ]
  { :pre [ (contains? board loc-id) ] }
  (update-in board [ loc-id ] swap-piece-system system-id))

(defn random-systems [ board ]
  (let [ planet-system-ratio 1
         planet-systems-count (int (* planet-system-ratio (count board)))
         special-systems-count (- (count board) planet-systems-count 1)
         planet-systems (systems/pick-random-planets planet-systems-count)
         special-systems (systems/pick-random-special-systems special-systems-count)
         all-systems (concat planet-systems special-systems [systems/get-system :mecatol-rex])
         systems-and-locs (map #(list %1 %2) (keys board) all-systems)
         swapper (fn [ current-board [ loc-id system-id ] ] (swap-system current-board loc-id system-id)) ]
    (reduce swapper board systems-and-locs)))

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

(defn map-polar [ clock rel-distance ]
  { :pre [ (>= clock 0) (<= clock 12) (>= rel-distance 0) (<= rel-distance 1) ] }
  (polar (- 180 (* clock 30)) (* rel-distance 0.5 tile-width)))

(def default-ship-locs [ [ -90 80 ] [ 90 -80 ] ]) ; suitable for standard 2-planet system

(def planet-units-locs [ [ 0 -30 ] [ 0 30 ] ])

(def ship-horiz-spacing 50)

(defn group-ships [ ships group-locs ]
  (let [ ships-per-group (int (Math/ceil (/ (count ships) (count group-locs))))
         ship-groups (partition ships-per-group ships-per-group [] ships) ]
    (zip ship-groups group-locs)))

(defn ship-group-svg [ [ group loc ] ] ; returns [ [:g ... ] [:g ... ] ... ]
  (if (empty? group) []
    (let [ ship (first group)
           next-loc (map + loc [ ship-horiz-spacing 0 ]) ]
      (conj (ship-group-svg [ (rest group) next-loc ] )
            (ships/svg ship loc)))))

(defn center-group [ [ group [ x y ] ] ]
  (let [ group-width (* (dec (count group)) ship-horiz-spacing) ]
    [ group [ (- x (* 0.5 group-width)) y ] ] ))

; Allows showing multiple fighters (and GF etc.) as <Fighter><Count> instead of individual icons.
(defn collapse-fighters [ [ { type1 :type count :count :as first } { type2 :type } & rrest :as all ] ]
  (if (and type1 type2)
    (let [ { individual-ids :individual-ids } (ships/all-unit-types type1) ]
      (if (and (not individual-ids) (= type1 type2))
        (let [ new-first (assoc first :count (inc (or count 1))) ]
          (collapse-fighters (cons new-first rrest)))
        (cons first (collapse-fighters (rest all)))))
    all))

(defn ships-svg [ ships group-locs ] ; returns [ [:g ... ] [:g ... ] ... ]
  (let [ sorted-ships (collapse-fighters (sort-by :type ships))
         grouped-ships (map center-group (group-ships sorted-ships group-locs)) ]
    (mapcat ship-group-svg grouped-ships)))

(defn planet-units-svg [ [ planet-id { units :units } ] system-info ]
  (if (or (not units) (empty? units)) nil
    (let [ planet-info (-> system-info :planets planet-id) ]
      (svg/g { :translate (planet-info :loc) :id (str (name planet-id) "-ground-units") }
        (ships-svg (vals units) planet-units-locs)))))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system loc-id :id controller :controller
                       ships :ships planets :planets } ]
  (let [ center (mul-vec tile-size 0.5)
         system-info (systems/get-system system-id)
         ships-content (if (or (not ships) (empty? ships)) [] (ships-svg (vals ships) default-ship-locs)) ; TODO: make default locs dependent on number of planets (0, 1, 2, 3)
         planets-units (if (or (not planets) (empty? planets)) []
                         (filter #(not (nil? %)) (map #(planet-units-svg % system-info) planets)))
         tile-label (svg/double-text (str/upper-case (name loc-id)) [ 25 200 ] { :id (str (name system-id) "-loc-label") }) ]
    (svg/g { :translate (screen-loc logical-pos) :id (str (name system-id) "-system") } [
      (svg/image [ 0 0 ] tile-size (str ships/resources-url "Tiles/" (system-info :image)))
      (svg/g { :translate center :id (str (name system-id) "-units") } `[ ~@planets-units ~@ships-content ])
      tile-label ] )))

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
