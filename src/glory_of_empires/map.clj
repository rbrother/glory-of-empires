(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure.test)
  (:use glory-of-empires.systems)
  (:use glory-of-empires.ships)
  (:use glory-of-empires.races)
  (:gen-class))

(def resources-url "http://www.brotherus.net/ti3/")

; ----------- coordinate tools --------------------

(defn min-pos
  { :test (fn [] (is (= [ 7 -4 ] (min-pos [ [ 7 12 ] [ 8 -4 ] ] )))) }
  [ vectors ] (apply mapv min vectors))

(defn max-pos
  { :test (fn [] (is (= [ 8 12 ] (min-pos [ [ 7 12 ] [ 8 -4 ] ] )))) }
  [ vectors ] (apply mapv max vectors))

(defn pos> [ [ x1 y1 ] [ x2 y2 ] ] (and (> x1 x2) (> y1 y2)))
(defn pos< [ [ x1 y1 ] [ x2 y2 ] ] (and (< x1 x2) (< y1 y2)))

(defn distance [ vec1 ] (Math/sqrt (apply + (map * vec1 vec1))))

(defn mul-vec [ vec1 scalar ] (map * vec1 (repeat scalar)))

(defn inside-rect? [ pos [ small-corner large-corner ] ]
  (and (pos> pos small-corner) (pos< pos large-corner)))

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

(defn swap-system [ loc-id system-id board ]
  { :pre [ (contains? board loc-id) ] }
  (assoc-in board [ loc-id :system ] system-id))

;-------------------- ships --------------------------

(defn new-ship-to-piece [ { controller :controller ships :ships :as piece } owner type ]
  (let [ controller (piece :controller)
         initial-ships (or ships [])
         new-id "de123" ]
    (if (and (not (empty? ships)) (not= controller owner))
      (throw (Exception. "Cannot add ship of different"))
      (merge piece { :controller owner :ships (conj initial-ships { :type type :id new-id }) } ))))

(defn new-ship [ loc-id owner type board ]
  { :pre [ (contains? board loc-id)
           (contains? all-ship-types type)
           (contains? all-races owner) ] } ; TODO: use races in this game instead?
  (update-in board [ loc-id ] new-ship-to-piece owner type))

;------------------ to svg ------------------------

(defn- transform [ { loc :translate scale :scale } ]
  (let [ translate (if (nil? loc) "" (str "translate(" (Math/round (first loc)) "," (Math/round (last loc)) ")" ))
         scale-str (if (nil? scale) "" (str "scale(" scale ")")) ]
    { :transform (str scale-str " " translate) } ))

(defn- width-height [ loc ] { :width (first loc) :height (last loc) } )

; Location from top-left corner of tile to some polar-coordinate location relative to its center
(defn polar [ degrees distance ]
  (let [ radians (/ degrees 57.2958)
         center (mul-vec tile-size 0.5)
         center-polar (mul-vec [ (Math/sin radians) (Math/cos radians) ] distance) ]
    (map + center-polar center)))

(defn double-text [ text loc ]
  (let [ attr { :x 0 :y 0 :fill "white" :font-family "Arial" :font-size "30px" } ]
    [ :g (transform { :translate loc })
      [ :text (merge attr { :x 2 :y 2 :fill "black" }) text ]
      [ :text attr text ] ] ))

(defn- piece-to-svg [ { logical-pos :logical-pos system-id :system id :id :as tile } ]
  (let [ center (mul-vec tile-size 0.5)
         system (get-system system-id) ]
    [ :g (transform { :translate (screen-loc logical-pos) })
      [ :image (merge
                 { :x 0 :y 0 }
                 (width-height tile-size)
                 { "xlink:href" (str resources-url "Tiles/" (system :image)) } ) ]
      (double-text (str/upper-case (name id)) (polar 270 170)) ] ))

(defn bounding-rect [ map-pieces ]
  (let [ s-locs (screen-locs map-pieces) ]
    [ (min-pos s-locs) (map + (max-pos s-locs) tile-size) ] ))

(defn rect-size [ [ min-corner max-corner ] ] (map - max-corner min-corner))

(defn- svg [ size & content ]
  (concat [ :svg (merge (width-height size) { "xmlns:xlink" "http://www.w3.org/1999/xlink" } ) ] content ))

(defn map-to-svg
  ( [ board ] (map-to-svg board {} ))
  ( [ board opts ]
    (let [ map-pieces (vals board)
           scale (get opts :scale 0.5)
           bounds (bounding-rect map-pieces)
           min-corner (first bounds)
           svg-size (mul-vec (rect-size bounds) scale) ]
      (svg svg-size
        (concat
          [ :g (transform { :scale scale :translate (mul-vec min-corner -1.0) } ) ]
          (map piece-to-svg map-pieces))))))
