(ns glory-of-empires.svg-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all])
  (:require [glory-of-empires.svg :as svg]))

(deftest svg-test
  (testing "svg"
    (is (= (svg/double-text "Hello" [ 6 6 ] {})
           [:g {:transform " translate(6,6)"}
              [:text {:x 2, :y 2, :fill "black", :font-family "Arial", :font-size "36px"} "Hello"]
              [:text {:x 0, :y 0, :fill "white", :font-family "Arial", :font-size "36px"} "Hello"]]))))
