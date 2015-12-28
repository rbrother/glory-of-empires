(ns glory-of-empires.ships
  (:require [clojure.string :as string])
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.svg :as svg]))

;(def resources-url "http://www.brotherus.net/ti3/")
(def resources-url "http://localhost/ti3/")

(def all-unit-types-arr
  [ { :id :fi :type :ship :name "Fighter"     :image-name "Fighter"   :image-size [ 50 36 ] }
    { :id :de :type :ship :name "Destroyer"   :image-name "Destroyer" :image-size [ 42 56 ] }
    { :id :cr :type :ship :name "Cruiser"     :image-name "Cruiser"   :image-size [ 55 105 ] }
    { :id :ca :type :ship :name "Carrier"     :image-name "Carrier"   :image-size [ 50 139 ] }
    { :id :dr :type :ship :name "Dreadnaught" :image-name "Dreadnaught" :image-size [ 79 159 ] }
    { :id :ws :type :ship :name "Warsun"      :image-name "Warsun"    :image-size [ 135 113 ] }
    { :id :fl :type :ship :name "Flagship"    :image-name "Flagship"  :image-size [ 200 71 ] }
    { :id :gf :type :ground :name "Ground Force" :image-name "GF"       :image-size [ 48 57 ] }
    { :id :mu :type :ground :name "Mechanised Unit" :image-name "MU"    :image-size [ 75 36 ] }
    { :id :pds :type :ground :name "Planetary Defence System" :image-name "PDS" :image-size [ 67 49 ] }
    { :id :sd :type :ground :name "Spacedock"   :image-name "Spacedock"  :image-size [ 76 78 ] } ] )

(def all-unit-types (index-by-id all-unit-types-arr))

(defn valid-unit-type? [ type ] (contains? all-unit-types type))

(defn ship-image-url [ type race ]
  { :pre [ (valid-unit-type? type) ] }
  (let [ { image-name :image-name } (all-unit-types type)
         { color :unit-color} (races/all-races race) ]
    (str resources-url "Ships/" color "/Unit-" color "-" image-name ".png")))

(defn svg [ { id :id type :type race :owner } loc ]
  { :pre [ (valid-unit-type? type)
           (not (nil? race))
           (contains? races/all-races race) ] }
  (let [ ship-data (all-unit-types type)
         tile-size (ship-data :image-size)
         center-shift (mul-vec tile-size -0.5) ]
    (svg/g { :translate (map + center-shift loc) }
      [ (svg/image [0 0] tile-size (ship-image-url type race))
        (svg/double-text (string/upper-case (name id)) [ 0 (+ (last tile-size) 24 ) ] { :size 22 }) ] )))
