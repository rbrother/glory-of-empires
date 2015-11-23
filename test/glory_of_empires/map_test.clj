(ns glory-of-empires.map-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest random-map-test
  (testing "make-random-map"
    (let [ a-map (make-random-map 1)
           normalized-map (map #(assoc-in % [:system :image] "xxx") a-map)
           correct-screen-locs [ [-324.0 -188.0] [-324.0 188.0] [0.0 -376.0] [0.0 0.0] [0.0 376.0] [324.0 -188.0] [324.0 188.0] ]
           correct-bounding-rect [ [ -324.0 -376.0 ] [ 756.0 752.0 ] ] ]
      (is (= (location-id [ -3 4 ] [ -5 -6 ] ) "C10" ))
      (is (=
        normalized-map
        [ {:logical-pos [-1 0], :system {:image "xxx"}, :id "A1"}
          {:logical-pos [-1 1], :system {:image "xxx"}, :id "A2"}
          {:logical-pos [0 -1], :system {:image "xxx"}, :id "B0"}
          {:logical-pos [0 0], :system {:image "xxx"}, :id "B1"}
          {:logical-pos [0 1], :system {:image "xxx"}, :id "B2"}
          {:logical-pos [1 -1], :system {:image "xxx"}, :id "C0"}
          {:logical-pos [1 0], :system {:image "xxx"}, :id "C1"} ] ))
      (is (= (screen-locs a-map) correct-screen-locs ))
      (is (= (min-pos correct-screen-locs) [-324.0 -376.0] ))
      (is (= (max-pos correct-screen-locs) [ 324.0 376.0 ] ))
      (is (= (bounding-rect a-map) correct-bounding-rect ))
      (is (= (rect-size correct-bounding-rect) [ 1080.0 1128.0 ] ))
      (is (= (map-to-svg normalized-map 0.5)
        [:html {}
          [:body {:style "background: #202020;"}
            [:svg {:width 540.0, :height 564.0, "xmlns:xlink" "http://www.w3.org/1999/xlink"}
              [:g {:transform "scale(0.5) translate(324.0,376.0)"}
                [:g {:transform " translate(-324.0,-188.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(-324.0,188.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(0.0,-376.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(0.0,0.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(0.0,376.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(324.0,-188.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]
                [:g {:transform " translate(324.0,188.0)"}
                  [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]]]]]))
      )))
