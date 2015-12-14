(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest ship-rendering-test
  (testing "ship rendering"
    (let []
      (is (= (ship-svg { :id :xyz :type :cruiser } "Yellow" [ -50 20 ] )
             [:g {:transform " translate(-27,-52)"}
              [:image { :x -50, :y 20, :width 55, :height 105,
                        "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Cruiser.png" } ]] ))
      (is (= (ship-group-svg [ [ { :id :abc :type :fighter } { :id :xyz :type :cruiser } ] [-50 20] ] "Yellow" )
             [ [:g { :transform " translate(-27,-52)"}
                 [:image {:x 0, :y 20, :width 55, :height 105,
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Cruiser.png"}]]
               [:g { :transform " translate(-25,-18)"}
                 [:image {:x -50, :y 20, :width 50, :height 36,
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png"}]]] ))
      (is (= (ship-group-svg [ [ { :id :abc :type :dreadnaught } { :id :xyz :type :dreadnaught } ] [-50 20] ] "Yellow" )
             [  [:g { :transform " translate(-39,-79)"}
                   [:image {:x 0, :y 20, :width 79, :height 159,
                            "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" } ]]
                [:g { :transform " translate(-39,-79)"}
                   [:image {:x -50, :y 20, :width 79, :height 159,
                            "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" } ]]] ))
      )))

(deftest random-map-test
  (testing "make-random-map"
    (let [ a-map (round-board 2)
           b-map (rect-board 3 2)
           pieces (vals a-map)
           correct-screen-locs [ [-324.0 -188.0] [-324.0 188.0] [0.0 -376.0] [0.0 0.0] [0.0 376.0] [324.0 -188.0] [324.0 188.0] ]
           correct-bounding-rect [ [ -324.0 -376.0 ] [ 756.0 752.0 ] ]
           b2-after-new-ship {
                :logical-pos [0 0],
                :system :mecatol-rex,
                :id :b2,
                :controller :hacan,
                :ships [ {:type :carrier, :id "de123"} ] }
           c-map (->> b-map
                      (swap-system :b2 :mecatol-rex)
                      (new-ship :b2 :hacan :carrier)) ]
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
      (is (string? (xml-to-text (map-to-svg c-map))))
      (is (= (c-map :b2) b2-after-new-ship ))
      )))
