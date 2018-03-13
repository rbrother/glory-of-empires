(ns glory-of-empires.html-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.html :as html]
            [hiccup.core :as hiccup]
            [clojure-common.utils :as utils]))

(deftest select-test
  (testing "select-test"
    (is (= (hiccup/html (html/select {:id "abc"} ["mike" "jack" "jill"]))
           "<select id=\"abc\"><option>mike</option><option>jack</option><option>jill</option></select>"))))

(deftest table-test
  (testing "table-test"
    (is (utils/compare-structure
          (html/table {} [
                          ["Game" [:span {:id "gameName"}] [:a {:href "/login"} "Logout"]]
                          [" " [{:id "currentCommand" :style "color: green;"} " "] [{:id "commandResult"} " "]]
                          ["abc"
                           [{:colspan 4}
                            [:span {:id "currentCommand" :style "color: green;"}]
                            [:span {:id "commandResult"}]]]])

          [:table
           {}
           [[:tr {} [[:td "Game"] [:td [:span {:id "gameName"}]] [:td [:a {:href "/login"} "Logout"]]]]
            [:tr
             {}
             [[:td " "] [:td {:id "currentCommand" :style "color: green;"} [" "]] [:td {:id "commandResult"} [" "]]]]
            [:tr
             {}
             [[:td "abc"]
              [:td {:colspan 4} [[:span {:id "currentCommand" :style "color: green;"}] [:span {:id "commandResult"}]]]]]]]))))
