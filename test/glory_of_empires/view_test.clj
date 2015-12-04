(ns glory-of-empires.map-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.view :as view]))

(deftest view-test
  (testing "view-test"
    (is (= {} (view/board)))))
