(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:require [glory-of-empires.systems :as systems])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.svg :as svg]))

; -------------------------- map ---------------------------------

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
  (let [ [ tile-width tile-height ] systems/tile-size
         pixel-size [ (* width tile-width 0.75) (* height tile-height) ]
         bounding-rect [ (mul-vec pixel-size -0.5) (mul-vec pixel-size 0.5) ] ]
    (make-board (+ width height) (fn [ pos ] (inside-rect? (systems/screen-loc pos) bounding-rect)))))

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

(defn all-systems [ game ] (map :system (vals (game :map))))

;------------------- map piece operations -------------------------

(defn swap-piece-system [ piece system-id ]
  { :pre [ (keyword? system-id) ]}
  (let [ planets ((systems/get-system system-id) :planets)
         ; Keep only planets names in the map, don't drag along static planet-info
         ; that we gan get from all-systems map when needed. This keeps the test-data manageable.
         planet-ids (if planets (keys planets) [] ) ]
    (merge piece { :system system-id :planets (set planet-ids) } )))

(defn swap-system [ board [ loc-id system-id ] ]
  { :pre [ (contains? board loc-id) (keyword? system-id) ] }
  (update-in board [ loc-id ] swap-piece-system system-id))

(defn random-systems [ board & opt ]
  (let [ planet-system-ratio (or (first opt) 0.70)
         systems (systems/pick-random-systems (count board) planet-system-ratio)
         systems-and-locs (map vector (keys board) systems ) ]
    (reduce swap-system board systems-and-locs)))

;-------------------- ships --------------------------

(defn- new-unit-index [ game-state type ]
  (inc (get-in game-state [ :ship-counters type ] 0)))

; Resolves into { :location :a1 :planet :aah } (planet can be also nil)
(defn- resolve-location [ loc unit-type { board :map planets :planets :as game } ]
  (let [ ship? (= :ship ((ships/all-unit-types unit-type) :type))
         is-system? (contains? (set (all-systems game)) loc) ]
    (cond
      ; :a1 (system-loc) for ships
      (and ship? (board loc)) { :location loc }
      ; :aah, :arinam-meer (system-id) for ships
      (and ship? is-system?) { :location (get-system-loc board loc) }
      ; :aah (planet-id) for ground-units
      (and (not ship?) (planets loc)) { :location (find-planet-loc board loc) :planet loc }
      ; :a1 (system-loc) for ground-units when only 1 planet in the system
      (and (not ship?) (board loc)) { :location loc :planet (first ((board loc) :planets)) }
      :else (throw (Exception. (str "Cannot resolve location " loc " for unit type " unit-type)))  )))

(defn- new-unit [ unit-id loc-id owner type game-state ]
  (merge { :id unit-id :owner owner :type type }
        (resolve-location loc-id type game-state) ))

(defn- add-new-unit [ loc-id owner type game-state ]
  { :pre [ (ships/valid-unit-type? type) ] }
  (let [ idx (new-unit-index game-state type)
         unit-id (keyword (str (name type) idx)) ]
    (-> game-state
        (assoc-in [ :ship-counters type ] idx )
        (update :units assoc unit-id (new-unit unit-id loc-id owner type game-state))  )))

(defn new-units [ loc-id owner types game-state ]
  (let [ new-unit-of (fn [ new-game-state type] (add-new-unit loc-id owner type new-game-state )) ]
    (reduce new-unit-of game-state types)))

(defn- operate-on-units [ game units-fn unit-ids ]
  (update game :units (fn [units] (reduce units-fn units unit-ids))))

(defn del-unit [ units id ] (dissoc units id))

(defn del-units [ ids game ] (operate-on-units game del-unit ids))

(defn move-unit [ units-map unit-id loc-id game ]
  (update units-map unit-id (fn [unit] (merge unit (resolve-location loc-id (:type unit) game))))  )

(defn move-units [ unit-ids loc-id game ]
  { :pre [ (sequential? unit-ids) ] }
  (let [ move-unit-to (fn [ units-map unit-id ] (move-unit units-map unit-id loc-id game)) ]
    (operate-on-units move-unit-to unit-ids)))

;------------------- merging of units and map (for rendering) ------------------

(defn- combine-planets-units [ planets units ]
  (let [ gather-planet-units (fn [planet-id]
                               (filter #(= (% :planet) planet-id) units)) ]
    (map (fn [planet] { :id planet :units (gather-planet-units planet) } ) planets)))

(defn- combine-piece-units [ { piece-id :id :as piece } units ]
  (let [ ship-in-system?
         (fn [ { ship-loc :location planet :planet } ]
           (and (= ship-loc piece-id) (not planet))) ]
    (-> piece
        (assoc :ships (filter ship-in-system? units))
        (update :planets combine-planets-units units)   )))

(defn combine-map-units [ map-pieces units ]
  (map (fn [piece] (combine-piece-units piece units)) map-pieces))
