(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.xml)
  (:require [glory-of-empires.command :as command])
  (:require [glory-of-empires.view :as view])
  (:require [glory-of-empires.players :as players])
  (:require [glory-of-empires.game-state :as game-state])
  (:require [glory-of-empires.login :as login])
  (:require [glory-of-empires.html :as html]))

; Call (reload) from repl as needed.
; Allow modifications to source files to take effect without restart (only for development, disable for production)
; Avoid using :reload-all since that also reloads game-state which resets the atom of the game-state
; (alternatively we could reload-all and then load also the game state from the file again, but this is more clean.
(defn reload []
  (require 'glory-of-empires.main :reload)
  (require 'glory-of-empires.view :reload)
  (require 'glory-of-empires.command :reload)
  (require 'glory-of-empires.players :reload)
  (require 'glory-of-empires.login :reload)
  (require 'glory-of-empires.html :reload))

;----------------- web server ----------------------

(defn reply [ text-content ]
  { :status 200
    :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
    :body (str text-content) }  )

(defn handle-exception [ ex ]
  (println (clojure.stacktrace/print-stack-trace ex))
  [ :span { :style "color: #ff3030;" } (.getMessage ex) ] )

(defn execute-post [ message-type game-id game game-func ]
  (case message-type
    :info (game-func game)
    :view (xml-to-text (game-func game))
    :command (do (game-state/swap-game game-func game-id) "ok") ; game-modifying commands
    :admin-command (do (game-state/swap-games game-func) "ok")  )) ; commands modifying the whole app state

(defn handle-post [ message-str ]
  (let [ message (read-string message-str)
         { game-id :game role :role password :password message-type :message-type func :func } message
         game (game-state/game game-id)
         game-func (binding [*ns* (find-ns 'glory-of-empires.main)] (eval func))
         require-role (get (meta game-func) :require-role) ]
    (if (or (not require-role) (players/password-valid? require-role game message))
      (execute-post message-type game-id game game-func)
      (throw (Exception. (str "Password " password " for " role " not valid for role " require-role)))   )))

(defn static-page [ path ] (slurp path :encoding "UTF-8"))

(def ignore-urls #{ "/favicon.ico" "/html/jquery.min.map" } )

(defn- handle-get [ uri query ] ; returns ready response-map or string for normal HTTP 200 response
  (cond
    (contains? ignore-urls uri) ""
    (re-matches #"\/html\/.+" uri) (static-page (subs uri 1))
    (= "/game" uri) (static-page "html/game.html")
    (= "/create-game" uri) (login/create-game-page)
    :else (login/login-page (game-state/game-names))))

(defn- handler-inner [request]
  (case (:request-method request)
    :post (let [ message (slurp (:body request)) ]
            (println message)
            (try (handle-post message) (catch Throwable e (xml-to-text (handle-exception e)))))
    :get (let [ { uri :uri query :query } request ]
           (println uri query)
           (handle-get uri query))))

(defn handler [request]
  (reload)
  (println "------------")
  (let [ raw-reply (handler-inner request) ]
    (if (string? raw-reply)
      (reply raw-reply) ; format as HTTP 200 OK response
      raw-reply ))) ; already formatted as some HTTP response

(defn -main [& args]
  (game-state/load-games)
  (run-jetty handler {:port 3000} ))

(defn runbg [] (.start (Thread. -main))) ; Call from repl
