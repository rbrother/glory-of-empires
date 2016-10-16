(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.xml)
  (:require [glory-of-empires.command :as command])
  (:require [glory-of-empires.view :as view])
  (:require [glory-of-empires.game-state :as game-state])
  (:gen-class))

;----------------- web server ----------------------

(defn reply [ text-content ]
  { :status 200
    :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
    :body (str text-content) }  )

(defn handle-exception [ ex ]
  (println (clojure.stacktrace/print-stack-trace ex))
  [ :span { :style "color: #ff3030;" } (.getMessage ex) ] )

(defn eval-input [ message-str ]
  (binding [*ns* (find-ns 'glory-of-empires.main)]
    (let [ message (read-string message-str)
           { game-id :game role :role password :password message-type :message-type func :func } message ]
      ; here distinguish by message-type
      (eval func))))

; example post request
;{ :headers {origin http://www.brotherus.net, ...}, :server-port 80,
; :content-type application/x-www-form-urlencoded; charset=UTF-8, :character-encoding UTF-8, :uri /, :server-name empires.brotherus.net,
; :query-string nil, :body #object[org.eclipse.jetty.server.HttpInputOverHTTP 0x1d2abf3 HttpInputOverHTTP@1d2abf3],
; :scheme :http, :request-method :post}

; example get request from http://empires.brotherus.net/empires/page.html?a=66
; { :uri /page.html, :server-name empires.brotherus.net, :query-string a=66,  :request-method :get}

(defn handle-get [] nil)

(defn handler [request]
  (let [ message (slurp (:body request)) ]
    (println (str (new java.util.Date) ": " message))
    (reply (try (eval-input message) (catch Throwable e (xml-to-text (handle-exception e)))))))

(defn -main [& args]
  (game-state/load-game)
  (run-jetty handler {:port 3000} ) )
