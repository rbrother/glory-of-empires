(ns glory-of-empires.map-svg
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:require [glory-of-empires.systems :as systems])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.svg :as svg]))

(defn- default-ship-locs [ planets-count ship-count ]
  (cond
    (= planets-count 0)
      (cond
        (<= ship-count 4) [ [ 0 0 ] ]
        :else [ [ 0 -80 ] [ 0 80 ] ] )
    (= planets-count 1)
      (cond
        (<= ship-count 2) [ [ 120 0 ] ]
        (<= ship-count 4) [ [ 120 0 ] [ 0 -120 ] ]
        :else [ [ 120 0 ] [ 0 -120 ] [ -120 0 ] ] )
    (= planets-count 2)
      (cond
        (<= ship-count 6) [ [ -90 80 ] [ 90 -80 ] ]
        :else [ [ -90 80 ] [ 90 -80 ] [ 0 0 ] ] )
    (= planets-count 3)
      [ [ 0 0 ] [ 90 -80 ] [ -90 -90 ] [ 90 110 ] ] ))

(defn planet-units-locs [ system-info planet-id ship-count ] ; allow ship-count param to be compatible with default-ship-locs
  (let [ planet-loc (-> system-info (:planets) (planet-id) (:loc)) ]
    (map (fn [ loc ] (map + loc planet-loc)) [ [ 0 -30 ] [ 0 30 ] ] )))

(defn center-group-to-loc "Moves group of ships in given position to left to center the group horizontally"
  [ group [ x y ] ]
      [ group [ (+ x (* -0.5 (ships/group-width group))) y ] ] )

(defn group-units "Groups seq of ships to equally-sized groups in given location positions"
  [ group-locs-func units ]
    (let [ group-locs (group-locs-func (count units))
           units-per-group (int (Math/ceil (/ (count units) (count group-locs))))
           unit-groups (partition units-per-group units-per-group [] units) ]
      (map center-group-to-loc unit-groups group-locs)))

(defn- collapse-group-id "Fighters, gf, etc. with same collapse-group-id are grouped to single item with count"
  [ { type :type individual-id :id } ]
    (let [ { individual-ids :individual-ids } (ships/all-unit-types type) ]
      (if individual-ids individual-id type)))

(defn- collapse-group [ [ first & rest :as group ] ]
  (if (single? group) first
    (-> first (assoc :count (count group)) (dissoc :id))))

(defn collapse-fighters "Allows showing multiple fighters (and GF etc.) as <Fighter><Count> instead of individual icons."
  [ ships ]
  (->> ships
       (sort-by :type)
       (partition-by collapse-group-id)
       (map collapse-group)))

(defn units-svg "Generates SVG for all ships in system (or units on planet) distributed to given group locations"
  [ [ units-map group-locs-func ] ]
  (->> (vals units-map)
       (collapse-fighters)
       (group-units group-locs-func)
       (mapcat ships/group-svg)))

(defn planetary-formations [ planets-map system-info ]
  (let [ planetary-formation (fn [ [ planet-id { units :units} ] ]
                               [ (or units {}) (partial planet-units-locs system-info planet-id) ] ) ]
    (map planetary-formation planets-map)))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system loc-id :id controller :controller
                       ships :ships planets :planets } ]
  (let [ center (mul-vec systems/tile-size 0.5)
         system-info (systems/get-system system-id)
         planet-count (count (or planets []))
         ships-formation [ ships (partial default-ship-locs planet-count) ]
         planetary-formations (planetary-formations (or planets {}) system-info)
         unit-formations (conj (vec planetary-formations) ships-formation) ; [ [ units-map locs-func ] [ units-map locs-func ] ... ]
         all-units-svg (mapcat units-svg unit-formations)
         tile-label (svg/double-text (str/upper-case (name loc-id)) [ 25 200 ] { :id (str (name system-id) "-loc-label") })
         system-id (str (name system-id) "-system") ]
    (svg/g { :translate (systems/screen-loc logical-pos) :id system-id } [
      (svg/image [ 0 0 ] systems/tile-size (str ships/resources-url "Tiles/" (system-info :image)) (str "system-" (name loc-id)) )
      (svg/g { :translate center :id (str (name system-id) "-units") } all-units-svg)
      tile-label ] )))

(defn bounding-rect [ map-pieces ]
  (let [ screen-locs (->> map-pieces (map :logical-pos) (map systems/screen-loc)) ]
    [ (min-pos screen-locs) (map + (max-pos screen-locs) systems/tile-size) ] ))

(defn render
  ( [ board ] (render board {} ))
  ( [ board opts ]
    (let [ map-pieces (vals board)
           scale (get opts :scale 0.5)
           [ min-corner max-corner :as bounds] (bounding-rect map-pieces)
           svg-size (mul-vec (rect-size bounds) scale) ]
      (svg/svg svg-size (svg/g { :scale scale :translate (mul-vec min-corner -1.0) }
                               (map piece-to-svg map-pieces) )))))
