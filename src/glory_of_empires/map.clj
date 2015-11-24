(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure-common.xml)
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

(def tile-size [ 432 376 ] )

(def good-letters [ "A", "B", "C", "D", "E", "F", "G",
              "H", "J", "K", "L", "M", "N", "P",
              "R", "S", "T", "U", "V", "X", "Y", "Z" ] )

(defn location-id [ [ x y ] [ min-x min-y ] ]
  (str (good-letters (- x min-x)) (+ 1 (- y min-y))))

(defn- screen-loc [ [ logical-x logical-y ] ]
  [ (* logical-x (first tile-size) 0.75)
    (* (last tile-size) (+ (* logical-x 0.5) logical-y)) ] )

(defn screen-locs [ map-pieces ]
  (->> map-pieces (map :logical-pos) (map screen-loc)))

(defn- tile-on-table? [ piece map-size ]
  (let [ pos (screen-loc (:logical-pos piece)) ]
    (< (distance pos) (* (last tile-size) map-size 1.01 ))))

(defn- random-system [ x y ]
  { :logical-pos [ x y ] :system (rand-nth all-systems) } )

(defn amend-tile-ids [ map-pieces ]
  (let [ min-loc (min-pos (map :logical-pos map-pieces))
         amend-tile-id (fn [tile] (assoc tile :id (location-id (:logical-pos tile) min-loc))) ]
    (map amend-tile-id map-pieces)))

(defn make-random-map [ rings ]
  (let [ a-range (range (- rings) (inc rings)) ]
    (->> (range2d a-range a-range)
         (map (fn [ [x y] ] (random-system x y) ))
         (filter #(tile-on-table? % rings))
         (amend-tile-ids) )))

;------------------- ships ----------------



;------------------ to svg ------------------------

(defn- transform [ { loc :translate scale :scale } ]
  (let [ translate (if (nil? loc) "" (str "translate(" (first loc) "," (last loc) ")" ))
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

(defn- piece-to-svg [ { logical-pos :logical-pos system :system id :id :as tile } ]
  (let [ center (mul-vec tile-size 0.5) ]
    [ :g (transform { :translate (screen-loc logical-pos) })
      [ :image (merge { :x 0 :y 0 } (width-height tile-size) { "xlink:href" (str resources-url "Tiles/" (system :image)) } ) ]
      (double-text id (polar 270 170)) ] ))

(defn bounding-rect [ map-pieces ]
  (let [ s-locs (screen-locs map-pieces) ]
    [ (min-pos s-locs) (map + (max-pos s-locs) tile-size) ] ))

(defn rect-size [ [ min-corner max-corner ] ] (map - max-corner min-corner))

(defn- svg [ size & content ]
  (concat [ :svg (merge (width-height size) { "xmlns:xlink" "http://www.w3.org/1999/xlink" } ) ] content ))

(defn map-to-svg [ map-pieces scale ]
  (let [ bounds (bounding-rect map-pieces)
         min-corner (first bounds)
         svg-size (mul-vec (rect-size bounds) scale) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        (svg svg-size
          (concat [ :g (transform { :scale scale :translate (mul-vec min-corner -1.0) } ) ]
                  (map piece-to-svg map-pieces))) ] ] ))


; Create example for testing
(defn get-random-map-html-text []
  (xml-to-text (map-to-svg (make-random-map 3) 0.6)))
