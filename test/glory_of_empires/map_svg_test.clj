(ns glory-of-empires.map-svg-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map-svg :refer :all])
  (:use glory-of-empires.map-test-data))

(def mini-map (mini-game-state :map))

(deftest render-map
  (testing "svg rendering"
    (are [ calculated expected ] (compare-structure calculated expected)
      (bounding-rect (vals mini-map))         [ [0.0 0.0] [756.0 564.0] ]
      (collapse-fighters many-ships)
         [ { :id :cr1 :owner :norr :type :cr }
           { :id :cr2 :owner :norr :type :cr }
           { :id :dr1 :owner :norr :type :dr }
           { :count 3 :owner :norr :type :fi } ]
      (render (mini-game-state :map))
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
                            { :transform " translate(-99,-128)" }
                            [ :image { :height 57 :width 48 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0 } ] ]
                        [   :g
                            { :transform " translate(-123,-64)" }
                            [ :image { :height 49 :width 67 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-PDS.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(67,40)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "2" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "2" ] ] ]
                        [   :g
                            { :transform " translate(36,12)" }
                            [ :image { :height 57 :width 48 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(48,40)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "2" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "2" ] ] ]
                        [   :g
                            { :transform " translate(37,61)" }
                            [ :image { :height 78 :width 76 :x 0 :xlink:href "http://localhost/ti3/Ships/Yellow/Unit-Yellow-Spacedock.png" :y 0 } ] ]
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
                            { :transform " translate(102,-18)" }
                            [ :image { :height 36 :width 50 :x 0 :xlink:href "http://localhost/ti3/Ships/Red/Unit-Red-Fighter.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(50,40)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2 } "6" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0 } "6" ] ] ]
                        [   :g
                            { :transform " translate(59,-28)" }
                            [ :image { :height 56 :width 42 :x 0 :xlink:href "http://localhost/ti3/Ships/Red/Unit-Red-Destroyer.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(0,76)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2 } "DE2" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0 } "DE2" ] ] ]
                        [   :g
                            { :transform " translate(-67,-176)" }
                            [ :image { :height 113 :width 135 :x 0 :xlink:href "http://localhost/ti3/Ships/Red/Unit-Red-Warsun.png" :y 0 } ]
                            [   :g
                                { :id "shaded-text" :transform " translate(0,133)" }
                                [ :text { :fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2 } "WS1" ]
                                [ :text { :fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0 } "WS1" ] ] ] ]
                    [   :g
                        { :id "aah-loc-label" :transform " translate(25,200)" }
                        [ :text { :fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2 } "B1" ]
                        [ :text { :fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0 } "B1" ] ] ] ] ]   )))
