(ns glory-of-empires.players-test
  (:use clojure-common.utils)
  (:use clojure.test)
  (:require [glory-of-empires.players :as players]))

(deftest players-test
  (testing "players-html"
    (let [ p { :players { :hacan { :id :hacan }
                          :naalu { :id :naalu } } } ]
      (is (= (players/set-players {} [ :hacan "hacanpassword" :naalu "pwd2" ] )
             { :players
               { :hacan { :id :hacan :password "hacanpassword" :ac [] :pc [] }
                 :naalu { :id :naalu :password "pwd2" :ac [] :pc [] } } } ))    )))

