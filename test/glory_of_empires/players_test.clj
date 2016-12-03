(ns glory-of-empires.players-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use clojure.test)
  (:require [glory-of-empires.players :as players]))

(deftest players-test
  (testing "players-html"
    (let [ p { :players { :hacan { :id :hacan }
                          :naalu { :id :naalu } } } ]
      (is (= (players/set-players [ :hacan :naalu ] {})
             { :players
               { :hacan { :id :hacan }
                 :naalu { :id :naalu } } } ))
      (is (compare-structure (players/players-html p)
        [   :div
            { }
            [   :div
                { }
                [   :h3
                    { }
                    [ :span { } ":hacan - The Emirates of Hacan" ]
                    [ :img { :src "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png" } ] ] ]
            [   :div
                { }
                [   :h3
                    { }
                    [ :span { } ":naalu - The Naalu Collective" ]
                    [ :img { :src "http://www.brotherus.net/ti3/Ships/Tan/Unit-Tan-Fighter.png" } ] ] ] ] )) )))

