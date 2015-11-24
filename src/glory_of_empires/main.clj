(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.utils)
  (:use glory-of-empires.map)
  (:gen-class))

;----------------- web server ----------------------

(defn handler [request]
  (println "------------ request received -----------")
  (println (pretty-pr request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (get-random-map-html-text) } )

(defn -main [& args] (run-jetty handler {:port 3000} ) )
