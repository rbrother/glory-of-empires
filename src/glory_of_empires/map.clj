(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure.test)
  (:use glory-of-empires.systems)
  (:gen-class))

(def resources-url "http://www.brotherus.net/ti3/")

; ----------- coordinate tools --------------------

(defn min-pos
  { :test (fn [] (is (= [ 7 -4 ] (min-pos [ [ 7 12 ] [ 8 -4 ] ] )))) }
  [ vectors ] (apply mapv min vectors))

(defn max-pos
  { :test (fn [] (is (= [ 8 12 ] (min-pos [ [ 7 12 ] [ 8 -4 ] ] )))) }
  [ vectors ] (apply mapv max vectors))

(defn distance [ vec1 ] (Math/sqrt (apply + (map * vec1 vec1))))

(defn mul-vec [ vec1 scalar ] (map * vec1 (repeat scalar)))

; -------------------------- map ---------------------------------

(def tile-width 432 )
(def tile-height 376 )
(def tile-size [ tile-width tile-height ] )

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
    :system (nth setup-tiles (min 4 tile-index)) } )

(defn amend-tile-ids [ map-pieces ]
  (let [ min-loc (min-pos (map :logical-pos map-pieces))
         amend-tile-id (fn [tile] (assoc tile :id (location-id (:logical-pos tile) min-loc))) ]
    (map amend-tile-id map-pieces)))

(defn round-board [ rings ]
  (let [ a-range (range (- rings) (inc rings)) ]
    (->> (range2d a-range a-range)
         (filter (fn [ pos ] (< (logical-distance pos) rings)))
         (map (fn [ pos ] (setup-system pos (logical-distance pos))))
         (amend-tile-ids) )))

(defn square-board [ width height ]
  (let [ size (+ width height)
         a-range (range (- size) (inc size)) ]
    nil))

;------------------- map operations -------------------------

(defn set-random-system [ piece ] (assoc piece :system (:id (rand-nth all-systems-arr))))

(defn random-systems [ board ] (map set-random-system board))

(defn swap-system [ board loc-id system-id ]
  (let [ swap-system-piece (fn [ piece ]
           (if (= (:id piece) loc-id) (assoc piece :system system-id) piece)) ]
      (map swap-system-piece board)))

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

(defn- screen-loc [ [ logical-x logical-y ] ]
  [ (* logical-x (first tile-size) 0.75)
    (* (last tile-size) (+ (* logical-x 0.5) logical-y)) ] )

(defn screen-locs [ map-pieces ]
  (->> map-pieces (map :logical-pos) (map screen-loc)))

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
  ( [ map-pieces ] (map-to-svg map-pieces {} ))
  ( [ map-pieces opts ]
    (let [ scale (get opts :scale 0.5)
           bounds (bounding-rect map-pieces)
           min-corner (first bounds)
           svg-size (mul-vec (rect-size bounds) scale) ]
      (svg svg-size
        (concat
          [ :g (transform { :scale scale :translate (mul-vec min-corner -1.0) } ) ]
          (map piece-to-svg map-pieces))))))
