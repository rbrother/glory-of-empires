(ns glory-of-empires.html-test
  (:require [clojure.test :refer :all]
            [glory-of-empires.html :as html]
            [clojure-common.xml :as xml]))

(deftest select-test
  (testing "select-test"
    (is (= (xml/xml-to-text (html/select { :id "abc" } [ "mike" "jack" "jill" ]))
           "
<select id=\"abc\">
    <option>mike</option>
    <option>jack</option>
    <option>jill</option>
</select>"  ))))

