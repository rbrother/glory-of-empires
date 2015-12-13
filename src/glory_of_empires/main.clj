(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.xml)
  (:require [glory-of-empires.command :as command])
  (:require [glory-of-empires.view :as view])
  (:require [glory-of-empires.game-state :as game-state])
  (:gen-class))

;----------------- web server ----------------------

(defn reply [ content ]
  { :status 200
    :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
    :body (xml-to-text content) }  )

(defn handle-exception [ ex ]
  (println (clojure.stacktrace/print-stack-trace ex))
  [ :span { :style "color: #ff0000;" } (.getMessage ex) ] )

(defn eval-input [ message ]
  (binding [*ns* (find-ns 'glory-of-empires.main)]
    (eval (read-string message))))

(defn handler [request]
  (println "------------ request received -----------")
  (let [ message (slurp (:body request)) ]
    (println message)
    (reply (try (eval-input message) (catch Throwable e (handle-exception e))))))

(defn -main [& args]
  (game-state/load-game)
  (run-jetty handler {:port 3000} ) )
