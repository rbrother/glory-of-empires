(ns glory-of-empires.map-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.svg :as svg]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(def mini-game-state
  { :map
    {:a1
      {:logical-pos [0 0],
      :system :setup-red,
      :id :a1,
      :controller :hacan,
      :ships [
        {:type :fighter, :id "de123"}
        {:type :fighter, :id "xx123"}]}},
   :players
    { :hacan {:id :hacan},
      :norr {:id :norr},
      :naalu {:id :naalu}}} )

(def mini-game-svg
  [ :g {:transform " translate(0,0)"}
    [ :image {:x 0, :y 0, "xlink:href" "http://www.brotherus.net/ti3/Tiles/Setup/Tile-Setup-Red.gif", :width 432, :height 376}]
    [ :g {:transform "translate(216,188)"}
      [ :g {:transform " translate(-25,-18)"}
        [ :image {:x -112.2368680667313, :y 64.80004202599832, :width 50, :height 36,
                 "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png" }]]
      [ :g {:transform " translate(-25,-18)"}
        [ :image {:x 22.362051432357585, :y -83.45596836498032, :width 50, :height 36,
                 "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png" }]]
      [ :g {:transform " translate(-173,0)"}
        [:text {:x 2, :y 2, :fill "black", :font-family "Arial", :font-size "36px"} "A1"]
        [:text {:x 0, :y 0, :fill "white", :font-family "Arial", :font-size "36px"} "A1"]]]] )

(deftest svg-test
  (testing "svg"
    (is (= (svg/double-text "Hello" [ 6 6 ] {})
           [:g {:transform " translate(6,6)"}
             [:text {:x 2, :y 2, :fill "black", :font-family "Arial", :font-size "36px"} "Hello"]
             [:text {:x 0, :y 0, :fill "white", :font-family "Arial", :font-size "36px"} "Hello"]]))))

(deftest ship-rendering-test
  (testing "ship rendering"
    (let []
      (is (= (ships/svg { :id :xyz :type :cruiser } :hacan [ -50 20 ] )
             [:g {:transform " translate(-27,-52)"}
              [:image { :x -50, :y 20, :width 55, :height 105,
                        "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Cruiser.png" } ]] ))
      (is (= (ship-group-svg [ [ { :id :abc :type :fighter } { :id :xyz :type :cruiser } ] [-50 20] ] :hacan )
             [ [:g { :transform " translate(-27,-52)"}
                 [:image {:x 0, :y 20, :width 55, :height 105,
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Cruiser.png"}]]
               [:g { :transform " translate(-25,-18)"}
                 [:image {:x -50, :y 20, :width 50, :height 36,
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png"}]]] ))
      (is (= (ship-group-svg [ [ { :id :abc :type :dreadnaught } { :id :xyz :type :dreadnaught } ] [-50 20] ] :hacan )
             [  [:g { :transform " translate(-39,-79)"}
                   [:image {:x 0, :y 20, :width 79, :height 159,
                            "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" } ]]
                [:g { :transform " translate(-39,-79)"}
                   [:image {:x -50, :y 20, :width 79, :height 159,
                            "xlink:href" "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" } ]]] ))
      (is (= (group-ships [ :a ] [ 1 2 ] )           [ [ [:a ] 1 ] ] ))
      (is (= (group-ships [ :a :b ] [ 1 2 ] )        [ [ [:a ] 1 ] [ [:b] 2 ] ] ))
      (is (= (group-ships [ :a :a ] [ 1 2 ] )        [ [ [:a ] 1 ] [ [:a] 2 ] ] ))
      (is (= (group-ships [ :a :b :c ] [ 1 2 ] )     [ [ [:a :b] 1 ] [ [:c] 2 ] ] ))
      (is (= (ships-svg :naalu [ { :id :abc :type :cruiser } { :id :abc :type :cruiser } ] )
             [ [:g {:transform " translate(-27,-52)"}
                 [:image {:x -112.2368680667313, :y 64.80004202599832, :width 55, :height 105
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Tan/Unit-Tan-Cruiser.png"}]]
               [:g {:transform " translate(-27,-52)"}
                 [:image {:x 22.362051432357585, :y -83.45596836498032, :width 55, :height 105
                          "xlink:href" "http://www.brotherus.net/ti3/Ships/Tan/Unit-Tan-Cruiser.png"}]] ] ))
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
