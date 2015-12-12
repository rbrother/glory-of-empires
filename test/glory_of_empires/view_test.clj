(ns glory-of-empires.view-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.view :as view]))

(deftest view-test
  (testing "view-test"
    (is (= "No map" (view/board)))))
