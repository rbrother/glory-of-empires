(ns glory-of-empires.players-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use clojure.test)
  (:require [glory-of-empires.players :as players]))

(deftest players-test
  (testing "players-html"
    (let [ p { :hacan { :id :hacan }
               :naalu { :id :naalu } } ]
      (is (= (players/set-players [ :hacan :naalu ] {})
             { :players
               { :hacan { :id :hacan }
                 :naalu { :id :naalu } } } ))
      (is (= (players/players-html p)
             [:div {} [:h1 {} "Players"]
                [:div {} [:h2 {} "The Emirates of Hacan"]]
                [:div {} [:h2 {} "The Naalu Collective"]]]))
      )))
