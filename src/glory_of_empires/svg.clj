(ns glory-of-empires.svg
  (:use clojure-common.utils))

(defn image [ [ x y ] [ width height ] url ]
  [ :image { :x x :y y :width width :height height :xlink:href url } ] )

(defn- transform [ { loc :translate scale :scale } ]
  (let [ translate (if loc (str "translate(" (round-any (first loc)) "," (round-any (last loc)) ")" ) "" )
         scale-str (if scale (str "scale(" scale ")") "" ) ]
    { :transform (str scale-str " " translate) } ))

(defn g [ opts content ]
  { :pre [ (map? opts) (sequential? content) ] }
  `[ :g ~(transform opts) ~@content ] )

(defn text [ content [ x y ] { color :color font :font size :size } ]
  [ :text { :x x :y y :fill (or color "white")
            :font-family (or font "Arial") :font-size (str (or size "36") "px") } content ] )

(defn double-text [ content loc opts ]
  (g { :translate loc } [
    (text content [ 2 2 ] (assoc opts :color "black"))
    (text content [ 0 0 ] (assoc opts :color "white")) ] ))

(defn svg [ [ width height ] & content ]
  `[ :svg { :width ~width :height ~height :xmlns:xlink "http://www.w3.org/1999/xlink" } ~@content ] )
