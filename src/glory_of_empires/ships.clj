(ns glory-of-empires.ships
  (:use clojure-common.utils))

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
