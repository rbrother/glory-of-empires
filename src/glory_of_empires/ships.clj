(ns glory-of-empires.ships
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.svg :as svg]))

;(def resources-url "http://www.brotherus.net/ti3/")
(def resources-url "http://localhost/ti3/")

(def all-ship-types-arr
  [ { :id :fighter   :short "fi" :image-name "Fighter" :image-size [ 50 36 ] }
    { :id :destroyer :short "de" :image-name "Destroyer" :image-size [ 42 56 ] }
    { :id :cruiser   :short "cr" :image-name "Cruiser" :image-size [ 55 105 ] }
    { :id :carrier   :short "ca" :image-name "Carrier" :image-size [ 50 139 ] }
    { :id :dreadnaught :short "dr" :image-name "Dreadnaught" :image-size [ 79 159 ] }
    { :id :warsun :short "ws" :image-name "Warsun" :image-size [ 135 113 ] }
    { :id :flagship  :short "fl" :image-name "Flagship" :image-size [ 200 71 ] }
    { :id :ground-force  :short "gf" :image-name "GF" :image-size [ 48 57 ] }
    { :id :mechanized-unit :short "mu" :image-name "MU" :image-size [ 75 36 ] }
    { :id :pds :short "pds" :image-name "PDS" :image-size [ 67 49 ] }
    { :id :space-dock :short "sd" :image-name "Spacedock" :image-size [ 76 78 ] } ] )

(def all-ship-types (index-by-id all-ship-types-arr))

(defn valid-ship-type? [ type ] (contains? all-ship-types type))

(defn ship-image-url [ { type :type :as ship } race ]
  (let [ { image-name :image-name } (all-ship-types type)
         { color :unit-color} (races/all-races race) ]
    (str resources-url "Ships/" color "/Unit-" color "-" image-name ".png")))

(defn svg [ { id :id type :type :as ship } race loc ]
  { :pre [ (valid-ship-type? type) ] }
  (let [ ship-data (all-ship-types type)
         tile-size (ship-data :image-size)
         center-shift (mul-vec tile-size -0.5) ]
    (svg/g { :translate (map + center-shift loc) }
      [ (svg/image [0 0] tile-size (ship-image-url ship race))
        (svg/double-text (name id) [ 0 (+ (last tile-size) 24 ) ] { :size 24 }) ] )))
