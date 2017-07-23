(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use glory-of-empires.map-test-data)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all])
  (:require [glory-of-empires.ships :refer :all])
  (:require [glory-of-empires.command :as command]))

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
    (let [ added-ships ((command/new :hacan 2 :fi :cr :a1) mini-game-state :hacan) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (added-ships :units) {
              :gf3 { :id :gf3 :location :a1 :planet :abyz :owner :hacan :type :gf }
              :pds1 { :id :pds1 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :pds2 { :id :pds2 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :gf1 { :id :gf1 :location :a1 :planet :fria :owner :hacan :type :gf }
              :gf2 { :id :gf2 :location :a1 :planet :fria :owner :hacan :type :gf }
              :sd1 { :id :sd1 :location :a1 :planet :fria :owner :hacan :type :sd }
              :ca3 { :id :ca3 :location :a1 :owner :hacan :type :ca }
              :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr }

              :fi9 { :id :fi9 :location :a1 :owner :hacan :type :fi }
              :fi10 { :id :fi10 :location :a1 :owner :hacan :type :fi }
              :cr1 { :id :cr1 :location :a1 :owner :hacan :type :cr }

              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi }
              :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } }
           (added-ships :ship-counters) { :ca 3 :de 2 :fi 10 :ws 1 :cr 1 :gf 3 :pds 2 }   ))
    (let [ added-units ((command/new :norr :gf :gf :pds :aah) mini-game-state :norr ) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (added-units :units) {
              :ca3 { :id :ca3 :location :a1 :owner :hacan :type :ca }
              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr }
              :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi }
              :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :gf1 { :id :gf1 :location :a1 :owner :hacan :planet :fria :type :gf }
              :gf2 { :id :gf2 :location :a1 :owner :hacan :planet :fria :type :gf }
              :gf3 { :id :gf3 :location :a1 :owner :hacan :planet :abyz :type :gf }
              :gf4 { :id :gf4 :location :b1 :planet :aah :owner :norr :type :gf }
              :gf5 { :id :gf5 :location :b1 :planet :aah :owner :norr :type :gf }
              :pds1 { :id :pds1 :location :a1 :owner :hacan :planet :abyz :type :pds }
              :pds2 { :id :pds2 :location :a1 :owner :hacan :planet :abyz :type :pds }
              :pds3 { :id :pds3 :location :b1 :planet :aah :owner :norr :type :pds }
              :sd1 { :id :sd1 :location :a1 :owner :hacan :planet :fria :type :sd }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } } ))
    (let [ deleted-units ((command/del :ca3 :from :b1 2 :fi) mini-game-state :game-master) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (deleted-units :units) {
              :gf3 { :id :gf3 :location :a1 :planet :abyz :owner :hacan :type :gf }
              :pds1 { :id :pds1 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :pds2 { :id :pds2 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :gf1 { :id :gf1 :location :a1 :planet :fria :owner :hacan :type :gf }
              :gf2 { :id :gf2 :location :a1 :planet :fria :owner :hacan :type :gf }
              :sd1 { :id :sd1 :location :a1 :planet :fria :owner :hacan :type :sd }
              :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr }
              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } } ))
    (let [ moved-units (move-units [ :gf1 :gf3 :pds1 :ca3 ] :aah mini-game-state) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (moved-units :units) {
              :gf3 { :id :gf3 :location :b1 :planet :aah :owner :hacan :type :gf }
              :pds1 { :id :pds1 :location :b1 :planet :aah :owner :hacan :type :pds }
              :pds2 { :id :pds2 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :gf1 { :id :gf1 :location :b1 :planet :aah :owner :hacan :type :gf }
              :gf2 { :id :gf2 :location :a1 :planet :fria :owner :hacan :type :gf }
              :sd1 { :id :sd1 :location :a1 :planet :fria :owner :hacan :type :sd }
              :ca3 { :id :ca3 :location :b1 :owner :hacan :type :ca }
              :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr }
              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi }
              :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } } ))
    (let [ moved-units ((command/move :from :a1 :pds 2 :gf :ca3 :b1) mini-game-state :game-master) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (moved-units :units) {
              :pds2 { :id :pds2 :location :a1 :planet :abyz :owner :hacan :type :pds }
              :sd1 { :id :sd1 :location :a1 :planet :fria :owner :hacan :type :sd }
              :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr }
              :gf3 { :id :gf3 :location :a1 :planet :abyz :owner :hacan :type :gf }

              :gf1 { :id :gf1 :location :b1 :planet :aah :owner :hacan :type :gf }
              :gf2 { :id :gf2 :location :b1 :planet :aah :owner :hacan :type :gf }
              :ca3 { :id :ca3 :location :b1 :owner :hacan :type :ca }
              :pds1 { :id :pds1 :location :b1 :planet :aah :owner :hacan :type :pds }

              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi }
              :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } } ))
    (let [ moved-units ((command/move :from :a1 :all :b1) mini-game-state :hacan) ]
      (are [ calculated expected ] (compare-structure calculated expected)
           (moved-units :units) {
              :pds2 { :id :pds2 :location :b1 :planet :aah :owner :hacan :type :pds }
              :gf1 { :id :gf1 :location :b1 :planet :aah :owner :hacan :type :gf }
              :sd1 { :id :sd1 :location :b1 :planet :aah :owner :hacan :type :sd }
              :dr7 { :id :dr7 :location :b1 :owner :hacan :type :dr }
              :ca3 { :id :ca3 :location :b1 :owner :hacan :type :ca }
              :pds1 { :id :pds1 :location :b1 :planet :aah :owner :hacan :type :pds }
              :gf2 { :id :gf2 :location :b1 :planet :aah :owner :hacan :type :gf }
              :gf3 { :id :gf3 :location :b1 :planet :aah :owner :hacan :type :gf }
              :de2 { :id :de2 :location :b1 :owner :norr :type :de }
              :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi }
              :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi }
              :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi }
              :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi }
              :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi }
              :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi }
              :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws } } )) ))

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
