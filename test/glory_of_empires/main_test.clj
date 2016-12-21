(ns glory-of-empires.main-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.main :as main] ))

(deftest parse-query-test
  (testing "view-test"
    (is (= {:gameName :sandbox, :role :game-master, :password "xyz"}
           (main/parse-query "gameName=%3Asandbox&role=%3Agame-master&password=xyz")))
    (is (= {:gameName :sandbox, :role :game-master, :password ""}
           (main/parse-query "gameName=%3Asandbox&role=%3Agame-master&password=")))
    ))
