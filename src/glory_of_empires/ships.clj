(ns glory-of-empires.ships
  (:require [clojure.string :as string])
  (:use clojure-common.utils)
  (:require [glory-of-empires.races :as races])
  (:require [glory-of-empires.svg :as svg]))

(def resources-url "http://www.brotherus.net/ti3/")

(def all-unit-types-arr
  [ { :id :fi :type :ship :name "Fighter"     :individual-ids false :image-name "Fighter"   :image-size [ 50 36 ] }
    { :id :de :type :ship :name "Destroyer"   :individual-ids true  :image-name "Destroyer" :image-size [ 42 56 ] }
    { :id :cr :type :ship :name "Cruiser"     :individual-ids true  :image-name "Cruiser"   :image-size [ 55 105 ] }
    { :id :ca :type :ship :name "Carrier"     :individual-ids true  :image-name "Carrier"   :image-size [ 50 139 ] }
    { :id :dr :type :ship :name "Dreadnaught" :individual-ids true  :image-name "Dreadnaught" :image-size [ 79 159 ] }
    { :id :ws :type :ship :name "Warsun"      :individual-ids true  :image-name "Warsun"    :image-size [ 135 113 ] }
    { :id :fl :type :ship :name "Flagship"    :individual-ids true  :image-name "Flagship"  :image-size [ 200 71 ] }
    { :id :gf :type :ground :name "Ground Force" :individual-ids false :image-name "GF"     :image-size [ 48 57 ] }
    { :id :st :type :ground :name "Shocktroop" :individual-ids false :image-name "ST"       :image-size [ 48 57 ] }
    { :id :mu :type :ground :name "Mechanised Unit" :individual-ids false :image-name "MU"  :image-size [ 75 36 ] }
    { :id :pds :type :ground :name "Planetary Defence System" :individual-ids false  :image-name "PDS" :image-size [ 67 49 ] }
    { :id :sd :type :ground :name "Spacedock" :individual-ids false  :image-name "Spacedock" :image-size [ 76 78 ] } ] )

(def all-unit-types (index-by-id all-unit-types-arr))

(defn valid-unit-type? [ type ] (contains? all-unit-types type))

(defn ship-image-url [ type race ]
  { :pre [ (valid-unit-type? type) ] }
  (let [ { image-name :image-name } (all-unit-types type)
         { color :unit-color} (races/all-races race) ]
    (str resources-url "Ships/" color "/Unit-" color "-" image-name ".png")))

(defn svg [ { id :id type :type race :owner count :count } loc ]
  { :pre [ (valid-unit-type? type)
           (not (nil? race))
           (contains? races/all-races race) ] }
  (let [ { [ width height :as tile-size ] :image-size individual-ships? :individual-ids } (all-unit-types type)
         center-shift [ 0 (* -0.5 height) ] ; Only need to center vertically. Horizontal centering done at group level
         final-loc (map round-any (map + center-shift loc))
         id-label (fn [] (svg/double-text (string/upper-case (name id)) [ 0 (+ 20 height) ] { :size 20 }))
         count-label (fn [ count ] (svg/double-text (str count) [ width 40 ] { :size 45 } )) ]
    (svg/g { :translate final-loc }
       (concat [ (svg/image [0 0] tile-size (ship-image-url type race)) ]
               (cond
                 individual-ships? [ (id-label) ]
                 (and count (> count 1)) [ (count-label count) ]
                 :else [ ] )))))

(defn width [ { type :type count :count :as ship } ]
  (let [ { [ image-width _ ] :image-size } (all-unit-types type)
         count-label-width (if count 30 0) ]
    (+ image-width count-label-width)))

(defn group-width [ ships ] (apply + (map width ships)))

(defn group-svg [ [ group [ x y :as loc ] ] ] ; returns [ [:g ... ] [:g ... ] ... ]
  (if (empty? group) []
    (let [ { type :type count :count :as ship } (first group)
           next-loc [ (+ x (width ship) 1) y ] ]
      (conj (group-svg [ (rest group) next-loc ] )
            (svg ship loc)))))
