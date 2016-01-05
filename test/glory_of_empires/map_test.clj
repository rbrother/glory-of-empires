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
                :dr7 {:type :dr, :id :dr7, :owner :hacan },
                :ca3 {:type :ca, :id :ca3, :owner :hacan } }
            :planets { :abyz { :controller :hacan
                               :units {
                                 :gf3 {:type :gf, :id :gf3, :owner :hacan }
                                 :pds1 {:type :pds, :id :pds1, :owner :hacan }
                                 :pds2 {:type :pds, :id :pds2, :owner :hacan } }}
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
      (map-to-svg (mini-game-state :map))
        [   :svg
            { :height 282.0 :width 378.0 :xmlns:xlink "http://www.w3.org/1999/xlink" }
            [   :g
                { :transform "scale(0.5) translate(0,0)" }
                [   :g
                    { :id "abyz-fria-system" :transform " translate(0,0)" }
                    [ :image { :height 376 :width 432 :x 0 :xlink:href "http://localhost/ti3/Tiles/2planet/Tile-Abyz-Fria.gif" :y 0 } ]
                    [   :g
                        { :id "abyz-fria-units" :transform " translate(216,188)" }
                        [   :g
                            { :id "abyz-ground-units" :transform " translate(-75,-70)" }
                            [   :g
                                { :transform " translate(-24,-58)" }
                                [ :image { :height 57 :width 48 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0 } ] ]
                            [   :g
                                { :transform " translate(-33,6)" }
                                [ :image { :height 49 :width 67 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-PDS.png" :y 0 } ]
                                [   :g
                                    { :id "shaded-text" :transform " translate(67,40)" }
                                    [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "2" ]
                                    [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "2" ] ] ] ]
                        [   :g
                            { :id "fria-ground-units" :transform " translate(75,70)" }
                            [   :g
                                { :transform " translate(-24,-58)" }
                                [ :image { :height 57 :width 48 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0 } ]
                                [   :g
                                    { :id "shaded-text" :transform " translate(48,40)" }
                                    [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "2" ]
                                    [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "2" ] ] ]
                            [   :g
                                { :transform " translate(-38,-9)" }
                                [ :image { :height 78 :width 76 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-Spacedock.png" :y 0 } ] ] ]
                        [   :g
                            { :transform " translate(-115,11)" }
                            [ :image { :height 139 :width 50 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-Carrier.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(0,159)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2 } "CA3" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0 } "CA3" ] ] ]
                        [   :g
                            { :transform " translate(51,-159)" }
                            [ :image { :height 159 :width 79 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(0,179)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2 } "DR7" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0 } "DR7" ] ] ] ]
                    [   :g
                        { :id "abyz-fria-loc-label" :transform " translate(25,200)" }
                        [ :text { :fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2 } "A1" ]
                        [ :text { :fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0 } "A1" ] ] ]
                [   :g
                    { :id "aah-system" :transform " translate(324,188)" }
                    [ :image { :height 376 :width 432 :x 0 :xlink:href "http://localhost/ti3/Tiles/1planet/Tile-Aah.gif" :y 0 } ]
                    [   :g
                        { :id "aah-units" :transform " translate(216,188)" }
                        [   :g
                            { :transform " translate(95,-18)" }
                            [ :image { :height 36 :width 50 :x 0 :xlink:href "http://localhost/ti3/Ships/Red/Unit-Red-Fighter.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(50,40)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "2" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "2" ] ] ] ]
                    [   :g
                        { :id "aah-loc-label" :transform " translate(25,200)" }
                        [ :text { :fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2 } "B1" ]
                        [ :text { :fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0 } "B1" ] ] ] ] ]
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
      (is (map? (random-systems a-map)))
      )))
