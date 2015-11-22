(ns glory-of-empires.map-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest random-map-test
  (testing "make-random-map"
    (let [ a-map (make-random-map 1)
           normalized-map (map #(assoc-in % [:system :image] "xxx") a-map)
           correct-screen-locs [ [-324.0 -188.0] [-324.0 188.0] [0.0 -376.0] [0.0 0.0] [0.0 376.0] [324.0 -188.0] [324.0 188.0] ] ]
      (is (=
        normalized-map
        [ { :logical-pos [ -1 0 ] :system { :image "xxx" } }
          { :logical-pos [ -1 1 ] :system { :image "xxx" } }
          { :logical-pos [ 0 -1 ] :system { :image "xxx" } }
          { :logical-pos [ 0 0 ] :system { :image "xxx" } }
          { :logical-pos [ 0 1 ] :system { :image "xxx" } }
          { :logical-pos [ 1 -1 ] :system { :image "xxx" } }
          { :logical-pos [ 1 0 ] :system { :image "xxx" } } ] ))
      (is (= (screen-locs a-map) correct-screen-locs ))
      (is (= (min-pos correct-screen-locs) [-324.0 -376.0] ))
      (is (= (max-pos correct-screen-locs) [324.0 376.0] ))
      (is (= (mul-pos (map + (map - [324.0 376.0] [-324.0 -376.0]) tile-size) 0.5) [ 540.0 564.0] ))
      (is (= (map-to-svg normalized-map 0.5)
             [:html {}
              [:body {:style "background: #202020;"}
                [:svg {:width 540.0, :height 564.0, "xmlns:xlink" "http://www.w3.org/1999/xlink"}
                  [:g {:transform "scale(0.5) translate(324.0,376.0)"}
                    [:image {:x -324, :y -188, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x -324, :y 188, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x 0, :y -376, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x 0, :y 0, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x 0, :y 376, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x 324, :y -188, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]
                    [:image {:x 324, :y 188, :width 432, :height 376, "xlink:href" "http://www.brotherus.net/ti3/Tiles/xxx"}]]]]]))
      )))
