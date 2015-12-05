(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest random-map-test
  (testing "make-random-map"
    (let [ a-map (round-board 2)
           correct-screen-locs [ [-324.0 -188.0] [-324.0 188.0] [0.0 -376.0] [0.0 0.0] [0.0 376.0] [324.0 -188.0] [324.0 188.0] ]
           correct-bounding-rect [ [ -324.0 -376.0 ] [ 756.0 752.0 ] ] ]
      (is (= (location-id [ -3 4 ] [ -5 -6 ] ) :c11 ))
      (is (=
        a-map
        [ { :logical-pos [-1  0 ], :system :setup-yellow, :id :a2 }
          { :logical-pos [-1  1 ], :system :setup-yellow, :id :a3 }
          { :logical-pos [ 0 -1 ], :system :setup-yellow, :id :b1 }
          { :logical-pos [ 0  0 ], :system :setup-red,    :id :b2 }
          { :logical-pos [ 0  1 ], :system :setup-yellow, :id :b3 }
          { :logical-pos [ 1 -1 ], :system :setup-yellow, :id :c1 }
          { :logical-pos [ 1  0 ], :system :setup-yellow, :id :c2 } ] ))
      (is (= (screen-locs a-map) correct-screen-locs ))
      (is (= (min-pos correct-screen-locs) [-324.0 -376.0] ))
      (is (= (max-pos correct-screen-locs) [ 324.0 376.0 ] ))
      (is (= (bounding-rect a-map) correct-bounding-rect ))
      (is (= (rect-size correct-bounding-rect) [ 1080.0 1128.0 ] ))
      (is (string? (xml-to-text (map-to-svg a-map { :scale 1.0 }))))
      (is (string? (xml-to-text (map-to-svg (random-systems a-map)))))
      (is (string? (xml-to-text (map-to-svg (swap-system a-map :a3 :mecatol-rex)))))
      )))
