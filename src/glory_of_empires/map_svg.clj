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
        (<= ship-count 4) [ [ 120 0 ] [ -120 0 ] ]
        :else [ [ 120 0 ] [ -120 0 ] [ 0 -120 ] ] )
    (= planets-count 2)
      (cond
        (<= ship-count 6) [ [ -90 80 ] [ 90 -80 ] ]
        :else [ [ -90 80 ] [ 90 -80 ] [ 0 0 ] ] )
    (= planets-count 3)
      [ [ 0 0 ] [ 90 -80 ] [ -90 -90 ] [ 90 110 ] ] ))

(def planet-units-locs [ [ 0 -30 ] [ 0 30 ] ])

(defn group-ships "Groups seq of ships to equally-sized groups in given location positions"
  { :test (fn [] (are [ calculated expected ] (= calculated expected)
      (group-ships [ :a ] [ :loc1 :loc2 ] )           [ [ [:a ] :loc1 ] ]
      (group-ships [ :a :b ] [ :loc1 :loc2 ] )        [ [ [:a ] :loc1 ] [ [:b] :loc2 ] ]
      (group-ships [ :a :b :c ] [ :loc1 :loc2 ] )     [ [ [:a :b] :loc1 ] [ [:c] :loc2 ] ] )) }
  [ ships group-locs ]
    (let [ ships-per-group (int (Math/ceil (/ (count ships) (count group-locs))))
           ship-groups (partition ships-per-group ships-per-group [] ships) ]
      (zip ship-groups group-locs)))

(defn center-group "Moves group of ships in given position to left to center the group horizontally"
  [ [ group [ x y ] ] ]
    (let [ group-width (apply + (map #(ships/width %) group)) ]
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
    (mapcat ships/group-svg grouped-ships)))

(defn planet-units-svg [ [ planet-id { units :units } ] system-info ]
  (if (or (not units) (empty? units)) nil
    (let [ planet-info (-> system-info :planets planet-id) ]
      (svg/g { :translate (planet-info :loc) :id (str (name planet-id) "-ground-units") }
        (ships-svg (vals units) planet-units-locs)))))

(defn piece-to-svg [ { logical-pos :logical-pos system-id :system loc-id :id controller :controller
                       ships :ships planets :planets } ]
  (let [ center (mul-vec systems/tile-size 0.5)
         system-info (systems/get-system system-id)
         ship-locs (default-ship-locs (count (system-info :planets)) (count ships))
         ships-content (if (or (not ships) (empty? ships)) [] (ships-svg (vals ships) ship-locs)) ; TODO: make default locs dependent on number of planets (0, 1, 2, 3)
         planets-units (if (or (not planets) (empty? planets)) []
                         (filter #(not (nil? %)) (map #(planet-units-svg % system-info) planets)))
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
