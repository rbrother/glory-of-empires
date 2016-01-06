(ns glory-of-empires.map-svg
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure.test)
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

(def planet-units-locs [ [ 0 -30 ] [ 0 30 ] ])

(defn group-ships "Groups seq of ships to equally-sized groups in given location positions"
  { :test (fn [] (are [ calculated expected ] (= calculated expected)
      (group-ships [ :loc1 :loc2 ] [ :a ] )           [ [ [:a ] :loc1 ] ]
      (group-ships [ :loc1 :loc2 ] [ :a :b ] )        [ [ [:a ] :loc1 ] [ [:b] :loc2 ] ]
      (group-ships [ :loc1 :loc2 ] [ :a :b :c ] )     [ [ [:a :b] :loc1 ] [ [:c] :loc2 ] ] )) }
  [ group-locs ships ]
    (let [ ships-per-group (int (Math/ceil (/ (count ships) (count group-locs))))
           ship-groups (partition ships-per-group ships-per-group [] ships) ]
      (zip ship-groups group-locs)))

(defn center-group "Moves group of ships in given position to left to center the group horizontally"
  [ [ group [ x y ] ] ]
    (let [ group-width (apply + (map #(ships/width %) group)) ]
      [ group [ (- x (* 0.5 group-width)) y ] ] ))

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

(defn ships-svg "Generates SVG for all ships distributed to given group locations"
  [ ships group-locs ]
  (->> ships
       (group-ships group-locs)
       (map center-group)
       (mapcat ships/group-svg)))

(defn planet-units-svg [ [ planet-id { units :units } ] system-info ]
  (if (or (not units) (empty? units)) nil
    (let [ planet-info (-> system-info :planets planet-id) ]
      (svg/g { :translate (planet-info :loc) :id (str (name planet-id) "-ground-units") }
        (ships-svg (collapse-fighters (vals units)) planet-units-locs)))))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system loc-id :id controller :controller
                       ships :ships planets :planets } ]
  (let [ center (mul-vec systems/tile-size 0.5)
         system-info (systems/get-system system-id)
         sorted-ships (collapse-fighters (vals (or ships {})))
         ship-locs (default-ship-locs (count (system-info :planets)) (count sorted-ships))
         ships-content (if (empty? sorted-ships) [] (ships-svg sorted-ships ship-locs))
         planets-units (if (or (not planets) (empty? planets)) []
                         (->> planets
                              (map #(planet-units-svg % system-info))
                              (filter #(not (nil? %)))))
         tile-label (svg/double-text (str/upper-case (name loc-id)) [ 25 200 ] { :id (str (name system-id) "-loc-label") }) ]
    (svg/g { :translate (systems/screen-loc logical-pos) :id (str (name system-id) "-system") } [
      (svg/image [ 0 0 ] systems/tile-size (str ships/resources-url "Tiles/" (system-info :image)))
      (svg/g { :translate center :id (str (name system-id) "-units") } `[ ~@planets-units ~@ships-content ])
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

(run-tests)
