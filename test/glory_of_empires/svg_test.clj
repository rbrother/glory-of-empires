(ns glory-of-empires.svg-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all])
  (:require [glory-of-empires.svg :as svg]))

(deftest svg-test
  (testing "svg"
    (are [ calculated expected ] (compare-structure calculated expected)
    (svg/double-text "Hello" [ 6 6 ] {})
                                 [   :g
                                  { :id "shaded-text" :transform " translate(6,6)" }
                                  (list    [ :text { :fill "black" :font-family "Arial" :font-size "36px" :x 2 :y 2 } "Hello" ]
                                           [ :text { :fill "white" :font-family "Arial" :font-size "36px" :x 0 :y 0 } "Hello" ] ) ]
                                 )))
