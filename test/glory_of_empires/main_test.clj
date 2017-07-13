(ns glory-of-empires.main-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.main :as main]
            [glory-of-empires.query :as query]
            [glory-of-empires.game-state :as game-state] ))

(deftest parse-query-test
  (testing "view-test"
    (is (= {:gameName :sandbox, :role :game-master, :password "xyz"}
           (query/parse "gameName=%3Asandbox&role=%3Agame-master&password=xyz")))
    (is (= {:gameName :sandbox, :role :game-master, :password ""}
           (query/parse "gameName=%3Asandbox&role=%3Agame-master&password=")))
    ))

(deftest game-state-test
  (testing "game-state-test"
    (game-state/swap-game identity {} :sandbox) ))
