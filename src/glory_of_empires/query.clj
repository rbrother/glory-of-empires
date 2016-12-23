(ns glory-of-empires.query
  (:require [clojure.string :as string]) )

(defn- to-map [ query-assignment ]
  (let [ [ a b ] (string/split query-assignment #"=") ]
         { (keyword a)
           (cond
             (not b) ""
             (re-matches #"^%3A.+" b) (keyword (string/replace b #"^%3A" ""))
             :else b) } ))

(defn parse [ query-string ]
  (if query-string
    (let [ parts (string/split query-string #"&")  ]
      (apply merge (map to-map parts)) )
    {} ))
