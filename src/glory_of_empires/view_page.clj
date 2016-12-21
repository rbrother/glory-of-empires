(ns glory-of-empires.view-page
    (:require [glory-of-empires.html :as html]))

(defn html [ { view :view } ]
  (html/page "Glory of Empires" "/html/command-page.js" [
    "View: " view
    ] ))
