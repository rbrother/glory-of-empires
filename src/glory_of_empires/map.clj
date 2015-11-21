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

(defn distance [dx dy] (Math/sqrt (+ (* dx dx) (* dy dy))))

; -------------------------- map ---------------------------------

(def tile-height 376)
(def tile-width 432)

(defn- screen-pos
  { :test (fn [] (is (= { :x 0.0 :y 0.0 } (screen-pos { :x 0 :y 0 } )))) }
  [ { logical-x :x logical-y :y } ]
    { :x (* logical-x tile-width 0.75)
      :y (* tile-height (+ (* logical-x 0.5) logical-y)) } )

(defn- tile-on-table? [ piece map-size ]
  (let [ { x :x y :y } (screen-pos (:logical-pos piece)) ]
    (< (distance x y) (* tile-height map-size 1.01 ))))

(defn- random-system [ x y ]
  { :logical-pos { :x x :y y } :system (rand-nth all-systems) } )

(defn make-random-map
  { :test (fn [] (is (=
    (->> (make-random-map 1) (map #(assoc-in % [:system :image] "xxx")))
    [ { :logical-pos { :x -1 :y 0 } :system { :image "xxx" } }
      { :logical-pos { :x -1 :y 1 } :system { :image "xxx" } }
      { :logical-pos { :x 0 :y -1 } :system { :image "xxx" } }
      { :logical-pos { :x 0 :y 0 } :system { :image "xxx" } }
      { :logical-pos { :x 0 :y 1 } :system { :image "xxx" } }
      { :logical-pos { :x 1 :y -1 } :system { :image "xxx" } }
      { :logical-pos { :x 1 :y 0 } :system { :image "xxx" } } ] ))) }
  [ rings ]
    (let [ a-range (range (- rings) (inc rings)) ]
      (->> (range2d a-range a-range)
          (map (fn [ [x y] ] (random-system x y) ))
          (filter #(tile-on-table? % rings)) )))


;------------------ to svg ------------------------

(defn- piece-to-svg [ { logical-pos :logical-pos system :system :as tile } ]
  (let [ { x :x y :y } (screen-pos logical-pos) ]
    [ :image { :x (int x) :y (int y) :width tile-width :height tile-height
               "xlink:href" (str resources-url "Tiles/" (system :image)) } ] ))

(defn map-to-svg [ map-pieces ]
  (let [ { min-x :x min-y :y } (->> map-pieces (map :logical-pos) (map screen-pos) (min-pos))
         counter-translate (str (- min-x) "," (- min-y)) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        [ :svg { :width 1500, :height 1000, "xmlns:xlink" "http://www.w3.org/1999/xlink" }
          (concat [ :g { :transform (str "scale(0.5) translate(" counter-translate ")") } ]
                  (map piece-to-svg map-pieces)) ] ] ] ))

;----------------- web server ----------------------

(def big-map (xml-to-text (map-to-svg (make-random-map 4))))

(defn handler [request]
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (xml-to-text (map-to-svg (make-random-map 3))) } )


(defn -main [& args]
  (println (xml-to-text (map-to-svg (make-random-map 2))))
  (spit "map.html" (xml-to-text (map-to-svg (make-random-map 4))))
  (run-jetty handler {:port 3000})
)
