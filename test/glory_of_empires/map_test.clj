(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all])
  (:require [glory-of-empires.ships :as ships]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(def mini-game-state
  { :map
    { :a1 { :logical-pos [0 0],
            :system :abyz-fria,
            :id :a1,
            :controller :hacan,
            :ships {
                :de7 {:type :de, :id :de7, :owner :hacan },
                :ca3 {:type :ca, :id :ca3, :owner :hacan } }
            :planets { :abyz { :controller :hacan }
                       :fria { :controller :hacan
                               :units {
                                 :gf1 {:type :gf, :id :gf1, :owner :hacan}
                                 :gf2 {:type :gf, :id :gf2, :owner :hacan}
                                 :sd1 {:type :sd, :id :sd1, :owner :hacan} } } } }
      :b1 { :logical-pos [1 0],
            :system :aah,
            :id :b1,
            :controller :norr,
            :ships {
               :fi3 {:type :fi, :id :fi3, :owner :norr },
               :fi8 {:type :fi, :id :fi8, :owner :norr } }
            :planets { :aah { :res 1 :inf 1 :loc [ 0 0 ] } } } },
    :players { :hacan {:id :hacan}, :norr {:id :norr}}} )

(deftest add-remove-unit-test
  (testing "add-remove-unit"
    (let [ start {:logical-pos [0 0], :system :setup-red, :id :a1, :controller :hacan,
                  :ships { :fi12 {:type :fi, :id :fi12} :cr7 {:type :cr, :id :cr7 } } } ]
    (is (= (del-unit-from-piece start :cr7)
           {:logical-pos [0 0], :system :setup-red, :id :a1, :controller :hacan,
                  :ships { :fi12 {:type :fi, :id :fi12} } } ))
    )))

(deftest svg-rendering-test
  (testing "svg rendering"
    (are [ calculated expected ] (compare-structure calculated expected)
      (group-ships [ :a ] [ 1 2 ] )           [ [ [:a ] 1 ] ]
      (group-ships [ :a :b ] [ 1 2 ] )        [ [ [:a ] 1 ] [ [:b] 2 ] ]
      (group-ships [ :a :a ] [ 1 2 ] )        [ [ [:a ] 1 ] [ [:a] 2 ] ]
      (group-ships [ :a :b :c ] [ 1 2 ] )     [ [ [:a :b] 1 ] [ [:c] 2 ] ]
      (map-to-svg (mini-game-state :map)) []
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

(deftest random-map-test
  (testing "make-random-map"
    (let [ a-map (round-board 2)
           b-map (rect-board 3 2)
           pieces (vals a-map)
           correct-screen-locs [ [-324.0 -188.0] [-324.0 188.0] [0.0 -376.0] [0.0 0.0] [0.0 376.0] [324.0 -188.0] [324.0 188.0] ]
           correct-bounding-rect [ [ -324.0 -376.0 ] [ 756.0 752.0 ] ] ]

      (is (= (location-id [ -3 4 ] [ -5 -6 ] ) :c11 ))
      (is (=
        a-map
        { :a2 { :id :a2 :logical-pos [ -1 0 ] :system :setup-yellow }
          :a3 { :id :a3 :logical-pos [ -1 1 ] :system :setup-yellow }
          :b1 { :id :b1 :logical-pos [ 0 -1 ] :system :setup-yellow }
          :b2 { :id :b2 :logical-pos [ 0 0 ] :system :setup-red }
          :b3 { :id :b3 :logical-pos [ 0 1 ] :system :setup-yellow }
          :c1 { :id :c1 :logical-pos [ 1 -1 ] :system :setup-yellow }
          :c2 { :id :c2 :logical-pos [ 1 0 ] :system :setup-yellow } } ))
      (is (= (screen-locs pieces) correct-screen-locs ))
      (is (= (min-pos correct-screen-locs) [-324.0 -376.0] ))
      (is (= (max-pos correct-screen-locs) [ 324.0 376.0 ] ))
      (is (= (bounding-rect pieces) correct-bounding-rect ))
      (is (= (rect-size correct-bounding-rect) [ 1080.0 1128.0 ] ))
      (is (string? (xml-to-text (map-to-svg a-map { :scale 1.0 }))))
      (is (string? (xml-to-text (map-to-svg (random-systems a-map)))))
      )))
