(ns glory-of-empires.ac-pc-test
  (:use clojure-common.utils)
  (:require [clojure.test :refer :all]
            [glory-of-empires.ac :refer :all]
            [glory-of-empires.map-test-data :as map-test-data]
            [glory-of-empires.command :as command] ))

(deftest ac-deck-test
  (testing "ac-deck-test"
    (let [ ac-deck (create-ac-deck) ]
      (is (= 136 (count ac-deck)))
      (is (keyword? (nth ac-deck 10)))
      (is (= 2 (count (filter #(= % :military-foresight) ac-deck)))    ))))

(deftest ac-command-test
  (testing "ac commands"
    (let [acs-added ((command/ac-deck-create) map-test-data/mini-game-state :game-master)]
      (is (= 136 (count (acs-added :ac-deck)))))
    (let [ac-picked ((command/ac-get) map-test-data/mini-game-state :hacan)]
      (is (=  #{:diplomatic-immunity :minelayers :morale-boost}
              (-> ac-picked :players :hacan :ac))))
    (let [ac-played ((command/ac-play :minelayers) map-test-data/mini-game-state :hacan)]
      (is (= #{:morale-boost} (-> ac-played :players :hacan :ac)))
      (is (= #{:minelayers} (-> ac-played :ac-discard))))))