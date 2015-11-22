(ns glory-of-empires.map-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.map :refer :all]))

; Most of our tests are inline-tests in the respective functions, but for longer tests we can put here

(deftest random-map-test
  (testing "make-random-map"
    (is (=
      (->> (make-random-map 1) (map #(assoc-in % [:system :image] "xxx")))
      [ { :logical-pos { :x -1 :y 0 } :system { :image "xxx" } }
        { :logical-pos { :x -1 :y 1 } :system { :image "xxx" } }
        { :logical-pos { :x 0 :y -1 } :system { :image "xxx" } }
        { :logical-pos { :x 0 :y 0 } :system { :image "xxx" } }
        { :logical-pos { :x 0 :y 1 } :system { :image "xxx" } }
        { :logical-pos { :x 1 :y -1 } :system { :image "xxx" } }
        { :logical-pos { :x 1 :y 0 } :system { :image "xxx" } } ] ))))
