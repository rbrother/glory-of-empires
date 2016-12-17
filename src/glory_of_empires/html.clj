(ns glory-of-empires.html
  (:use clojure-common.utils)
  (:require [clojure-common.xml :as xml]))

(def xhtml-dtd "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"
   \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">" )

(defn page [ title extra-script body-content ]
  (str xhtml-dtd (xml/xml-to-text
    [ :html { :xmlns "http://www.w3.org/1999/xhtml" }
      [ :head
        [ :meta { :charset "UTF-8" } ]
        [ :title title ]
        [ :link { :rel "stylesheet" :type "text/css" :href "/html/game.css" } ]
        [ :script { :src "/html/jquery.min.js" } ]
        [ :script { :src "/html/glory-of-empires.js" } ]
        [ :script (if extra-script { :src extra-script } {}) ] ]
      `[ :body ~@body-content ] ] )))

(defn select [ attrs options ]
  `[ :select ~attrs ~@(map (fn [opt] [ :option {} opt ]) options) ] )

(defn- make-col [ col ] [ :td col ] )

(defn- make-row [ row ] `[ :tr ~@(map make-col row) ] )

(defn table [ & rows ] `[ :table ~@(map make-row rows) ] )
