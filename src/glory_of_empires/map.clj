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
  { :test (fn [] (is (= { :x 7 :y -4 } (min-pos [ { :x 7 :y 12} { :x 8 :y -4 } ] )))) }
  [ coords ] { :x (apply min (map :x coords)) :y (apply min (map :y coords)) } )

(defn max-pos
  { :test (fn [] (is (= { :x 8 :y 12 } (min-pos [ { :x 7 :y 12} { :x 8 :y -4 } ] )))) }
  [ coords ] { :x (apply max (map :x coords)) :y (apply max (map :y coords)) } )

(defn distance [dx dy] (Math/sqrt (+ (* dx dx) (* dy dy))))

(defn add-pos [ { x1 :x y1 :y } { x2 :x y2 :y } ] { :x (+ x1 x2) :y (+ y1 y2) } )
(defn sub-pos [ { x1 :x y1 :y } { x2 :x y2 :y } ] { :x (- x1 x2) :y (- y1 y2) } )
(defn mul-pos [ { x :x y :y } m ] { :x (* x m) :y (* y m) } )

; -------------------------- map ---------------------------------

(def tile-size { :x 432 :y 376 } )

(defn- screen-pos
  { :test (fn [] (is (= { :x 0.0 :y 0.0 } (screen-pos { :x 0 :y 0 } )))) }
  [ { logical-x :x logical-y :y } ]
    { :x (* logical-x (tile-size :x) 0.75)
      :y (* (tile-size :y) (+ (* logical-x 0.5) logical-y)) } )

(defn- tile-on-table? [ piece map-size ]
  (let [ { x :x y :y } (screen-pos (:logical-pos piece)) ]
    (< (distance x y) (* (tile-size :y) map-size 1.01 ))))

(defn- random-system [ x y ]
  { :logical-pos { :x x :y y } :system (rand-nth all-systems) } )

(defn make-random-map [ rings ]
  (let [ a-range (range (- rings) (inc rings)) ]
    (->> (range2d a-range a-range)
         (map (fn [ [x y] ] (random-system x y) ))
         (filter #(tile-on-table? % rings)) )))


;------------------ to svg ------------------------

(defn- piece-to-svg [ { logical-pos :logical-pos system :system :as tile } ]
  (let [ { x :x y :y } (screen-pos logical-pos) ]
    [ :image { :x (int x) :y (int y) :width (tile-size :x) :height (tile-size :y)
               "xlink:href" (str resources-url "Tiles/" (system :image)) } ] ))

(defn map-to-svg [ map-pieces scale ]
  (let [ screen-locs (->> map-pieces (map :logical-pos) (map screen-pos))
         min-p (min-pos screen-locs)
         max-p (max-pos screen-locs)
         size (mul-pos (add-pos (sub-pos max-p min-p) tile-size) scale)
         counter-translate (str (- (min-p :x)) "," (- (min-p :y))) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        [ :svg { :width (:x size), :height (:y size), "xmlns:xlink" "http://www.w3.org/1999/xlink" }
          (concat [ :g { :transform (str "scale(" scale ") translate(" counter-translate ")") } ]
                  (map piece-to-svg map-pieces)) ] ] ] ))

;----------------- web server ----------------------

(defn handler [request]
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (xml-to-text (map-to-svg (make-random-map 3) 1.0)) } )

(defn -main [& args] (run-jetty handler {:port 3000} ) )
