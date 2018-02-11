(ns glory-of-empires.view-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all]
            [glory-of-empires.view :as view]
            [glory-of-empires.systems :as systems]
            [glory-of-empires.game-state :as game-state]))

(deftest view-test
  (testing "view-test"
    (is (= "No map" ((view/board) (game-state/new-game-state "") :game-master)))))

(deftest views-test
  (testing "views"
    (are [calculated expected]
      (compare-structure calculated expected)
      (systems/planets-info-html
        {:resheph {:res 0 :inf 2 :tech {:red 1} :loc [-75 -70]}
         :ogdoad  {:res 1 :inf 0 :loc [75 70]}})
      [:ul
       [[:li
         [:div
          [:div "resheph"]
          [:div
           " res: "
           [:span {:style "color: #20FF20; font-weight: bold;"} 0]
           " inf: "
           [:span {:style "color: #FF2020; font-weight: bold;"} 2]]]]
        [:li
         [:div
          [:div "ogdoad"]
          [:div
           " res: "
           [:span {:style "color: #20FF20; font-weight: bold;"} 1]
           " inf: "
           [:span {:style "color: #FF2020; font-weight: bold;"} 0]]]]]])))


