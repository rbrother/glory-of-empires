(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.utils)
  (:use glory-of-empires.map)
  (:gen-class))

;----------------- web server ----------------------

(defn handler [request]
  (println "------------ request received -----------")
  (println (pretty-pr request))
  (println (slurp (:body request)))
  {:status 200
   :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
   :body (get-random-map-svg-text) } )

(defn -main [& args] (run-jetty handler {:port 3000} ) )
