(ns glory-of-empires.svg
  (:use clojure-common.utils))

(defn image [ [ x y ] [ width height ] url ]
  [ :image { :x x :y y :width width :height height "xlink:href" url } ] )

(defn- transform [ { loc :translate scale :scale } ]
  (let [ translate (if (nil? loc) "" (str "translate(" (Math/round (first loc)) "," (Math/round (last loc)) ")" ))
         scale-str (if (nil? scale) "" (str "scale(" scale ")")) ]
    { :transform (str scale-str " " translate) } ))

(defn g [ opts content ]
  { :pre [ (map? opts) (sequential? content) ] }
  `[ :g ~(transform opts) ~@content ] )

(defn svg [ [ width height ] & content ]
  `[ :svg { :width ~width :height ~height "xmlns:xlink" "http://www.w3.org/1999/xlink" } ~@content ] )
