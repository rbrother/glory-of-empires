(ns glory-of-empires.ac-pc-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all]
            [glory-of-empires.ac :refer :all]))

(deftest ac-deck-test
  (testing "ac-deck-test"
    (let [ ac-deck (create-ac-deck) ]
      (is (= 136 (count ac-deck)))
      (is (keyword? (nth ac-deck 10)))
      (is (= 2 (count (filter #(= % :military-foresight) ac-deck)))    ))))
