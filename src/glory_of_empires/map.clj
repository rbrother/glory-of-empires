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

(defn- screen-loc
  { :test (fn [] (is (= { :x 0.0 :y 0.0 } (screen-loc { :x 0 :y 0 } )))) }
  [ [ logical-x logical-y ] ]
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

(defn- piece-to-svg [ { logical-pos :logical-pos system :system :as tile } ]
  (let [ [ x y ] (screen-loc logical-pos) ]
    [ :image { :x (int x) :y (int y) :width (first tile-size) :height (last tile-size)
               "xlink:href" (str resources-url "Tiles/" (system :image)) } ] ))

(defn bounding-rect [ map-pieces ]
  (let [ s-locs (screen-locs map-pieces) ]
    [ (min-pos s-locs) (map + (max-pos s-locs) tile-size) ] ))

(defn rect-size [ [ min-corner max-corner ] ] (map - max-corner min-corner))

(defn map-to-svg [ map-pieces scale ]
  (let [ bounds (bounding-rect map-pieces)
         min-corner (first bounds)
         svg-size (mul-vec (rect-size bounds) scale)
         counter-translate (str (- (first min-corner)) "," (- (last min-corner))) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        [ :svg { :width (first svg-size), :height (last svg-size), "xmlns:xlink" "http://www.w3.org/1999/xlink" }
          (concat [ :g { :transform (str "scale(" scale ") translate(" counter-translate ")") } ]
                  (map piece-to-svg map-pieces)) ] ] ] ))

;----------------- web server ----------------------

(defn handler [request]
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (xml-to-text (map-to-svg (make-random-map 3) 1.0)) } )

(defn -main [& args] (run-jetty handler {:port 3000} ) )
