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

(defn select [ id options ]
  `[ :select { :id id } ~@(map (fn [opt] [ :option {} opt ]) options) ] )
