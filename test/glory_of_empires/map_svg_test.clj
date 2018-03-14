(ns glory-of-empires.map-svg-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all]
            [glory-of-empires.map-svg :as map-svg])
  (:use glory-of-empires.map-test-data))

(def mini-map (mini-game-state :map))

(deftest render-map
  (testing "svg rendering"
    (are [calculated expected]
      (compare-structure calculated expected)
      (map-svg/bounding-rect (vals mini-map)) [[0.0 0.0] [756.0 564.0]]
      (map-svg/collapse-fighters many-ships)
      [{:id :cr1 :owner :norr :type :cr}
       {:id :cr2 :owner :norr :type :cr}
       {:id :dr1 :owner :norr :type :dr}
       {:count 3 :owner :norr :type :fi}]
      (map-svg/render mini-game-state)

      [:svg
       {:height 282.0 :width 378.0 :xmlns:xlink "http://www.w3.org/1999/xlink"}
       (list [:g
              {:transform "scale(0.5) translate(0,0)"}
              (list [:g
                     {:id "abyz-fria-system" :transform " translate(0,0)"}
                     (list [:image
                            {:height 376 :onclick "clicked('system-a1')" :width 432 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Tiles/2planet/Tile-Abyz-Fria.gif" :y 0}]
                           [:g
                            {:id "abyz-fria-system-units" :transform " translate(216,188)"}
                            (list [:g
                                   {:transform " translate(-60,-128)"}
                                   (list [:image
                                          {:height 57 :onclick "clicked('unit-gf3')" :width 48 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0}])]
                                  [:g
                                   {:transform " translate(-137,-121)"}
                                   (list [:image
                                          {:height 43 :onclick "clicked('unit-abyz')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/FlagWavy/Flag-Wavy-hacan.png" :y 0}])]
                                  [:g
                                   {:transform " translate(-123,-64)"}
                                   (list [:image
                                          {:height 49 :onclick "clicked('unit-pds')" :width 67 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-PDS.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(67,40)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2} "2"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0} "2"])])]
                                  [:g
                                   {:transform " translate(75,12)"}
                                   (list [:image
                                          {:height 57 :onclick "clicked('unit-gf')" :width 48 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-GF.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(48,40)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2} "2"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0} "2"])])]
                                  [:g
                                   {:transform " translate(-2,19)"}
                                   (list [:image
                                          {:height 43 :onclick "clicked('unit-fria')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/FlagWavy/Flag-Wavy-hacan.png" :y 0}])]
                                  [:g
                                   {:transform " translate(37,61)"}
                                   (list [:image
                                          {:height 78 :onclick "clicked('unit-sd1')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-Spacedock.png" :y 0}])]
                                  [:g
                                   {:transform " translate(-103,1)"}
                                   (list [:image
                                          {:height 159 :onclick "clicked('unit-dr7')" :width 79 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-Dreadnaught.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(0,179)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2} "DR7"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0} "DR7"])])]
                                  [:g
                                   {:transform " translate(-154,11)"}
                                   (list [:image
                                          {:height 139 :onclick "clicked('unit-ca3')" :width 50 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Yellow/Unit-Yellow-Carrier.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(0,159)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2} "CA3"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0} "CA3"])])]
                                  [:g
                                   {:transform " translate(52,-101)"}
                                   (list [:image
                                          {:height 43 :onclick "clicked('unit-a1')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/FlagWavy/Flag-Wavy-hacan.png" :y 0}])])]
                           [:g
                            {:id "abyz-fria-loc-label" :transform " translate(25,200)"}
                            (list [:text {:fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2} "A1"]
                                  [:text {:fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0} "A1"])])]
                    [:g
                     {:id "aah-system" :transform " translate(324,188)"}
                     (list [:image
                            {:height 376 :onclick "clicked('system-b1')" :width 432 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Tiles/1planet/Tile-Aah.gif" :y 0}]
                           [:g
                            {:id "aah-system-units" :transform " translate(216,188)"}
                            (list [:g
                                   {:transform " translate(-38,-51)"}
                                   (list [:image
                                          {:height 43 :onclick "clicked('unit-aah')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/FlagWavy/Flag-Wavy-norr.png" :y 0}])]
                                  [:g
                                   {:transform " translate(150,-28)"}
                                   (list [:image
                                          {:height 56 :onclick "clicked('unit-de2')" :width 42 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Red/Unit-Red-Destroyer.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(0,76)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2} "DE2"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0} "DE2"])])]
                                  [:g
                                   {:transform " translate(49,-44)"}
                                   (list [:image
                                          {:height 88 :onclick "clicked('unit-hacan')" :width 100 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/CCs/CC-hacan.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(0,108)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2} "HACAN"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0} "HACAN"])])]
                                  [:g
                                   {:transform " translate(3,-141)"}
                                   (list [:image
                                          {:height 43 :onclick "clicked('unit-b1')" :width 76 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/FlagWavy/Flag-Wavy-norr.png" :y 0}])]
                                  [:g
                                   {:transform " translate(-78,-138)"}
                                   (list [:image
                                          {:height 36 :onclick "clicked('unit-fi')" :width 50 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Red/Unit-Red-Fighter.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(50,40)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "45px" :x 2 :y 2} "6"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "45px" :x 0 :y 0} "6"])])]
                                  [:g
                                   {:transform " translate(-187,-56)"}
                                   (list [:image
                                          {:height 113 :onclick "clicked('unit-ws1')" :width 135 :x 0 :xlink:href "http://www.brotherus.net:81/ti3/Ships/Red/Unit-Red-Warsun.png" :y 0}]
                                         [:g
                                          {:id "shaded-text" :transform " translate(0,133)"}
                                          (list [:text {:fill "black" :font-family "Arial" :font-size "20px" :x 2 :y 2} "WS1"]
                                                [:text {:fill "white" :font-family "Arial" :font-size "20px" :x 0 :y 0} "WS1"])])])]
                           [:g
                            {:id "aah-loc-label" :transform " translate(25,200)"}
                            (list [:text {:fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2} "B1"]
                                  [:text {:fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0} "B1"])])])])]

      )))
