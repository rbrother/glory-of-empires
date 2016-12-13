(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.xml)
  (:require [glory-of-empires.command :as command])
  (:require [glory-of-empires.view :as view])
  (:require [glory-of-empires.game-state :as game-state])
  (:require [glory-of-empires.login :as login])
  (:require [glory-of-empires.html :as html])
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
           { game-id :game role :role password :password message-type :message-type func :func } message
           game (game-state/game game-id)
           func-with-game (apply list (conj (vec func) game)) ]
      (case message-type
        :info (eval func-with-game)
        :view (xml-to-text (eval func-with-game))
        :command (do (game-state/swap-game (eval func) game-id) "ok") ))))

; example post request
;{ :headers {origin http://www.brotherus.net, ...}, :server-port 80,
; :content-type application/x-www-form-urlencoded; charset=UTF-8, :character-encoding UTF-8, :uri /, :server-name empires.brotherus.net,
; :query-string nil, :body #object[org.eclipse.jetty.server.HttpInputOverHTTP 0x1d2abf3 HttpInputOverHTTP@1d2abf3],
; :scheme :http, :request-method :post}

; example get request from http://empires.brotherus.net/empires/page.html?a=66
; { :uri /page.html, :server-name empires.brotherus.net, :query-string a=66,  :request-method :get}

; Call from repl.
; Allow modifications to source files to take effect without restart (only for development, disable for production)
; Avoid using :reload-all since that also reloads game-state which resets the atom of the game-state
; (alternatively we could reload-all and then load also the game state from the file again, but this is more clean.
(defn reload []
  (use 'glory-of-empires.main :reload)
  (use 'glory-of-empires.view :reload)
  (use 'glory-of-empires.command :reload)
  (use 'glory-of-empires.login :reload)
  (use 'glory-of-empires.html :reload))

(defn static-page [ path ] (slurp path :encoding "UTF-8"))

(def ignore-urls #{ "/favicon.ico" "/html/jquery.min.map" } )

(defn- handle-get [ uri query ]
  (cond
    (contains? ignore-urls uri) ""
    (re-matches #"\/html\/.+" uri) (static-page (subs uri 1))
    (= "/game" uri) (static-page "html/game.html")
    :else (login/login-page (game-state/game-names))))

(defn handler [request]
  (reload)
  (println "------------")
  (reply
    (case (:request-method request)
      :post
        (let [ message (slurp (:body request)) ]
          (println (str (new java.util.Date) ": " message))
          (try (eval-input message) (catch Throwable e (xml-to-text (handle-exception e)))))
      :get
        (let [ { uri :uri query :query } request ]
          (println uri query)
          (handle-get uri query)))))

(defn -main [& args]
  (game-state/load-games)
  (run-jetty handler {:port 3000} ))

(defn runbg [] (.start (Thread. -main))) ; Call from repl
