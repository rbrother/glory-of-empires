(ns glory-of-empires.map
  (:require [clojure.string :as str])
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use clojure.test)
  (:use ring.adapter.jetty)
  (:gen-class))

(def resources-url "http://www.brotherus.net/ti3/")

(def all-systems
  [ { :image "1planet/Tile-Acheron.gif" }
    { :image "1planet/Tile-Aeon.gif" }
    { :image "1planet/Tile-Aker.gif" }
    { :image "1planet/Tile-Ammit.gif" }
    { :image "1planet/Tile-Amun.gif" }
    { :image "1planet/Tile-Andjety.gif" }
    { :image "1planet/Tile-Anhur.gif" }
    { :image "1planet/Tile-Ankh.gif" }
    { :image "1planet/Tile-Anuket.gif" }
    { :image "1planet/Tile-Apis.gif" }
    { :image "1planet/Tile-Asgard.gif" }
    { :image "1planet/Tile-Asgard_III.gif" }
    { :image "1planet/Tile-Astennu.gif" }
    { :image "1planet/Tile-Aten.gif" }
    { :image "1planet/Tile-Babi.gif" }
    { :image "1planet/Tile-Bakha.gif" }
    { :image "1planet/Tile-Bast.gif" }
    { :image "1planet/Tile-Beriyil.gif" }
    { :image "1planet/Tile-Bes.gif" }
    { :image "1planet/Tile-Capha.gif" }
    { :image "1planet/Tile-Chensit.gif" }
    { :image "1planet/Tile-Chnum.gif" }
    { :image "1planet/Tile-Chuuka.gif" }
    { :image "1planet/Tile-Cicerus.gif" }
    { :image "1planet/Tile-Coruscant.gif" }
    { :image "1planet/Tile-Dedun.gif" }
    { :image "1planet/Tile-Deimo.gif" }
    { :image "1planet/Tile-Discworld.gif" }
    { :image "1planet/Tile-Dune.gif" }
    { :image "1planet/Tile-Elnath.gif" }
    { :image "1planet/Tile-Everra.gif" }
    { :image "1planet/Tile-Faunus.gif" }
    { :image "1planet/Tile-Fiorina.gif" }
    { :image "1planet/Tile-FloydIV.gif" }
    { :image "1planet/Tile-Garbozia.gif" }
    { :image "1planet/Tile-Heimat.gif" }
    { :image "1planet/Tile-Hopes_End.gif" }
    { :image "1planet/Tile-Inaak.gif" }
    { :image "1planet/Tile-Industrex.gif" }
    { :image "1planet/Tile-Iskra.gif" }
    { :image "1planet/Tile-Ithaki.gif" }
    { :image "1planet/Tile-Kanite.gif" }
    { :image "1planet/Tile-Kauket.gif" }
    { :image "1planet/Tile-Kazenoeki.gif" }
    { :image "1planet/Tile-Khepri.gif" }
    { :image "1planet/Tile-Khnum.gif" }
    { :image "1planet/Tile-Klendathu.gif" }
    { :image "1planet/Tile-Kobol.gif" }
    { :image "1planet/Tile-Laurin.gif" }
    { :image "1planet/Tile-Lesab.gif" }
    { :image "1planet/Tile-Lodor.gif" }
    { :image "1planet/Tile-Lv426.gif" }
    { :image "1planet/Tile-Medusa_V.gif" }
    { :image "1planet/Tile-Mehar_Xull.gif" }
    { :image "1planet/Tile-Mirage.gif" }
    { :image "1planet/Tile-Myrkr.gif" }
    { :image "1planet/Tile-Nanan.gif" }
    { :image "1planet/Tile-Natthar.gif" }
    { :image "1planet/Tile-Nef.gif" }
    { :image "1planet/Tile-Nexus.gif" }
    { :image "1planet/Tile-Niiwa-Sei.gif" }
    { :image "1planet/Tile-Pakhet.gif" }
    { :image "1planet/Tile-Parzifal.gif" }
    { :image "1planet/Tile-Perimeter.gif" }
    { :image "1planet/Tile-Petbe.gif" }
    { :image "1planet/Tile-Primor.gif" }
    { :image "1planet/Tile-Ptah.gif" }
    { :image "1planet/Tile-Qetesh.gif" }
    { :image "1planet/Tile-Quann.gif" }
    { :image "1planet/Tile-Radon.gif" }
    { :image "1planet/Tile-Saudor.gif" }
    { :image "1planet/Tile-Sem-Lore.gif" }
    { :image "1planet/Tile-Shai.gif" }
    { :image "1planet/Tile-Shool.gif" }
    { :image "1planet/Tile-Solitude.gif" }
    { :image "1planet/Tile-Sulako.gif" }
    { :image "1planet/Tile-Suuth.gif" }
    { :image "1planet/Tile-Tarmann.gif" }
    { :image "1planet/Tile-Tefnut.gif" }
    { :image "1planet/Tile-Tempesta.gif" }
    { :image "1planet/Tile-Tenenit.gif" }
    { :image "1planet/Tile-Theom.gif" }
    { :image "1planet/Tile-Thibah.gif" }
    { :image "1planet/Tile-Ubuntu.gif" }
    { :image "1planet/Tile-Vefut_II.gif" }
    { :image "1planet/Tile-Wadjet.gif" }
    { :image "1planet/Tile-Wellon.gif" }
    { :image "1planet/Tile-Wepwawet.gif" } ] )


; ---- coordinate tools

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

(defn- add-screen-coord [ { pos :logical-pos :as piece } ]
  (assoc piece :screen-pos (screen-pos pos) ))

(defn- tile-on-table? [ piece map-size ]
  (let [ { { x :x y :y } :screen-pos } (add-screen-coord piece) ]
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

(defn- piece-to-svg [ { { x :x y :y } :screen-pos system :system :as tile } ]
  [ :image { :x (int x) :y (int y) :width tile-width :height tile-height
             "xlink:href" (str resources-url "Tiles/" (system :image)) } ] )

(defn map-to-svg [ map-pieces ]
  (let [ pieces2 (map add-screen-coord map-pieces)
         { min-x :x min-y :y } (min-pos (map :screen-pos pieces2))
         counter-translate (str (- min-x) "," (- min-y)) ]
    [ :html {}
      [ :body { :style "background: #202020;" }
        [ :svg { :width 1500, :height 1000, "xmlns:xlink" "http://www.w3.org/1999/xlink" }
          (concat [ :g { :transform (str "scale(0.5) translate(" counter-translate ")") } ]
                  (map piece-to-svg pieces2)) ] ] ] ))

;----------------- web server ----------------------

(def big-map (xml-to-text (map-to-svg (make-random-map 4))))

(defn handler [request]
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body big-map } )


(defn -main [& args]
  (println (xml-to-text (map-to-svg (make-random-map 2))))
  (spit "map.html" (xml-to-text (map-to-svg (make-random-map 4))))
  (run-jetty handler {:port 3000})
)
