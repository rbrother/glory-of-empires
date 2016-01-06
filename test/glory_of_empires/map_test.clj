(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all])
  (:require [glory-of-empires.ships :as ships]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest map-creation-test
  (testing "make-random-map"
    (let [ a-map (round-board 2)
           b-map (rect-board 3 2)
           pieces (vals a-map) ]
    (are [ calculated expected ] (compare-structure calculated expected)
      (location-id [ -3 4 ] [ -5 -6 ] ) :c11
      a-map
        { :a2 { :id :a2 :logical-pos [ -1 0 ] :system :setup-yellow }
          :a3 { :id :a3 :logical-pos [ -1 1 ] :system :setup-yellow }
          :b1 { :id :b1 :logical-pos [ 0 -1 ] :system :setup-yellow }
          :b2 { :id :b2 :logical-pos [ 0 0 ] :system :setup-red }
          :b3 { :id :b3 :logical-pos [ 0 1 ] :system :setup-yellow }
          :c1 { :id :c1 :logical-pos [ 1 -1 ] :system :setup-yellow }
          :c2 { :id :c2 :logical-pos [ 1 0 ] :system :setup-yellow } }
      ))))

(deftest random-systems-test
  (testing "random-systems-test"
    (let [ standard-map (random-systems (round-board 3))
           all-planet-map (random-systems (round-board 3) 1.0) ]
      (are [ calculated expected ] (= calculated expected)
           (count standard-map) 19
           (->> standard-map (map last) (map :system) (distinct) (count)) 18 ; Two empty systems -> 18 distinct systems
           (->> all-planet-map (map last) (map :system) (distinct) (count)) 19 ; All distinct planets
           ))))

(deftest unit-operations-test
  (testing "unit-operations-test"
    (are [ calculated expected ] (compare-structure calculated expected)
         1 1
         ;;;;;;;;;;;;;; CONTINUE TEST FROM MINI-TEST-STATE

    )))

(deftest find-planet-test
  (testing "find planet"
    (let [board1
          {:a2 {:logical-pos [-1 0], :system :setup-yellow, :id :a2},
           :a3 {:logical-pos [-1 1], :system :setup-yellow, :id :a3},
           :b2 {:logical-pos [0 0], :system :abyz-fria, :id :b2,
                :planets {:abyz {:res 3, :inf 0}, :fria {:res 2, :inf 0, :tech {:blue 1}}},
                :controller :hacan, :ships {:ca6 {:type :ca, :id :ca6, :owner :hacan}}},
           :c1 {:logical-pos [1 -1], :system :setup-yellow, :id :c1},
           :c2 {:logical-pos [1 0], :system :setup-yellow, :id :c2}}]
      (is (= (get-system-loc board1 :abyz-fria) :b2))
      (is (= (find-planet-loc board1 :abyz) :b2))
      )))
