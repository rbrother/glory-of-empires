(ns glory-of-empires.tech
  (:use clojure-common.utils))

; https://www.boardgamegeek.com/filepage/71870/tech-tree

(def all-tech-arr
  [ { :id :l4-disruptors :name "L4 Disruptors"
      :description "You don’t need to spend 2 Trade Goods to use your racial ability in ground combat." }
    { :id :noneuclidian-shielding :name "Noneuclidian Shielding"
      :description "When you use the sustain damage ability of one of your units, it prevents two casualties (rather than one)." }
    ])


(def all-tech (index-by-id all-tech-arr))
