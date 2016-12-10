(ns glory-of-empires.html
  (:use clojure-common.utils))

(defn page [ title extra-script body-content ]
  [ :html
    [ :head
      [ :meta { :charset "UTF-8" } ]
      [ :title title ]
      [ :script { :src "/glory-of-empires/jquery.min.js" } ]
      [ :script { :src "/glory-of-empires/glory-of-empires.js" } ]
      [ :script extra-script ] ]
    `[ :body { :style "background: #202020; color: white;" } ~@body-content ] ] )

(defn select [ attrs options ]
  `[ :select ~attrs ~@(map (fn [opt] [ :option {} opt ]) options) ] )

(defn- make-col [ col ] [ :td col ] )

(defn- make-row [ row ] `[ :tr ~@(map make-col row) ] )

(defn table [ & rows ] `[ :table ~@(map make-row rows) ] )
