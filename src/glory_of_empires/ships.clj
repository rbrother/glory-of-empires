(ns glory-of-empires.ships
  (:use clojure-common.utils))

(def all-ship-types-arr
  [ { :id :fighter   :short "fi" }
    { :id :destroyer :short "de" }
    { :id :cruiser   :short "cr" }
    { :id :carrier   :short "ca" }
    { :id :dreadnaught :short "dr" }
    { :id :flagship  :short "fl" } ] )

(def all-ship-types (index-by-id all-ship-types-arr))
