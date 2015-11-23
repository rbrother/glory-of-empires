(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use clojure.test)
  (:use ring.adapter.jetty)
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

(defn make-random-map [ rings ]
  (let [ a-range (range (- rings) (inc rings)) ]
    (->> (range2d a-range a-range)
         (map (fn [ [x y] ] (random-system x y) ))
         (filter #(tile-on-table? % rings)) )))

;------------------ to svg ------------------------

(defn- transform [ { loc :translate scale :scale } ]
  (let [ translate (if (nil? loc) "" (str "translate(" (first loc) "," (last loc) ")" ))
         scale-str (if (nil? scale) "" (str "scale(" scale ")")) ]
    { :transform (str scale-str " " translate) } ))

(defn- piece-to-svg [ { logical-pos :logical-pos system :system :as tile } ]
  [ :g (transform { :translate (screen-loc logical-pos) })
    [ :image { :x 0 :y 0 :width (first tile-size) :height (last tile-size)
               "xlink:href" (str resources-url "Tiles/" (system :image)) } ] ] )

(defn bounding-rect [ map-pieces ]
  (let [ s-locs (screen-locs map-pieces) ]
    [ (min-pos s-locs) (map + (max-pos s-locs) tile-size) ] ))

(defn rect-size [ [ min-corner max-corner ] ] (map - max-corner min-corner))

(defn- svg [ size & content ]
  (concat [ :svg { :width (first size) :height (last size) "xmlns:xlink" "http://www.w3.org/1999/xlink" } ] content ))

(defn map-to-svg [ map-pieces scale ]
  (let [ bounds (bounding-rect map-pieces)
         min-corner (first bounds)
         svg-size (mul-vec (rect-size bounds) scale) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        (svg svg-size
          (concat [ :g (transform { :scale scale :translate (mul-vec min-corner -1.0) } ) ]
                  (map piece-to-svg map-pieces))) ] ] ))

;----------------- web server ----------------------

(defn handler [request]
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (xml-to-text (map-to-svg (make-random-map 3) 1.0)) } )

(defn -main [& args] (run-jetty handler {:port 3000} ) )
