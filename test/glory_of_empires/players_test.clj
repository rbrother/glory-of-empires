(ns glory-of-empires.players-test
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use clojure.test)
  (:require [glory-of-empires.players :as players]))

(deftest players-test
  (testing "players-html"
    (let [ p { :players { :hacan { :id :hacan }
                          :naalu { :id :naalu } } } ]
      (is (= (players/set-players {} [ :hacan "hacanpassword" :naalu "pwd2" ] )
             { :players
               { :hacan { :id :hacan :password "hacanpassword" }
                 :naalu { :id :naalu :password "pwd2" } } } ))
      (is (compare-structure (players/players-html p)
        [   :div
          [   :table
              { :class "data" }
              [   :tr
                  { }
                  [ :td "Race" ]
                  [ :td "Color" ]
                  [ :td "Symbol" ]
                  [ :td "VP" ]
                  [ :td "Systems" ]
                  [ :td "Planets" ]
                  [ :td "Res" ]
                  [ :td "Inf" ]
                  [ :td "TG" ]
                  [ :td "Tech" ]
                  [ :td "AC" ]
                  [ :td "PC" ] ]
              [   :tr
                  { }
                  [ :td :hacan ]
                  [ :td "Color" ]
                  [ :td "Symbol" ]
                  [ :td "VP" ]
                  [ :td "Systems" ]
                  [ :td "Planets" ]
                  [ :td "Res" ]
                  [ :td "Inf" ]
                  [ :td "Tech" ]
                  [ :td 0 ]
                  [ :td 0 ]
                  [ :td 0 ] ]
              [   :tr
                  { }
                  [ :td :naalu ]
                  [ :td "Color" ]
                  [ :td "Symbol" ]
                  [ :td "VP" ]
                  [ :td "Systems" ]
                  [ :td "Planets" ]
                  [ :td "Res" ]
                  [ :td "Inf" ]
                  [ :td "Tech" ]
                  [ :td 0 ]
                  [ :td 0 ]
                  [ :td 0 ] ] ]
          [   :div
              [   :h3
                  [ :span { } ":hacan - The Emirates of Hacan" ]
                  [ :img { :src "http://www.brotherus.net/ti3/Ships/Yellow/Unit-Yellow-Fighter.png" } ] ] ]
          [   :div
              [   :h3
                  [ :span { } ":naalu - The Naalu Collective" ]
                  [ :img { :src "http://www.brotherus.net/ti3/Ships/Tan/Unit-Tan-Fighter.png" } ] ] ] ] )) )))

