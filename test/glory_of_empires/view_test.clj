(ns glory-of-empires.view-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.view :as view]
            [glory-of-empires.game-state :as game-state]))

(deftest view-test
  (testing "view-test"
    (is (= "No map" ((view/board) (game-state/new-game-state ""))))))
