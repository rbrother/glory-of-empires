(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.xml)
  (:require [glory-of-empires.command :as command])
  (:require [glory-of-empires.view :as view])
  (:require [glory-of-empires.players :as players])
  (:require [glory-of-empires.game-state :as game-state])
  (:require [glory-of-empires.login :as login])
  (:require [glory-of-empires.command-page :as command-page])
  (:require [glory-of-empires.view-page :as view-page])
  (:require [glory-of-empires.html :as html])
  (:require [glory-of-empires.query :as query]) )

; Call (reload) from repl as needed.
; Allow modifications to source files to take effect without restart (only for development, disable for production)
; Avoid using :reload-all since that also reloads game-state which resets the atom of the game-state
; (alternatively we could reload-all and then load also the game state from the file again, but this is more clean.
(defn reload []
  (require 'glory-of-empires.main :reload)
  (require 'glory-of-empires.view :reload)
  (require 'glory-of-empires.map :reload)
  (require 'glory-of-empires.map-svg :reload)
  (require 'glory-of-empires.command :reload)
  (require 'glory-of-empires.players :reload)
  (require 'glory-of-empires.systems :reload)
  (require 'glory-of-empires.ships :reload)
  (require 'glory-of-empires.login :reload)
  (require 'glory-of-empires.command-page :reload)
  (require 'glory-of-empires.view-page :reload)
  (require 'glory-of-empires.html :reload)
  (require 'glory-of-empires.ac :reload))

;----------------- web server ----------------------

(defn reply
  ( [ text-content ] (reply text-content "text/html") )
  ( [ text-content type ]
    { :status 200
      :headers {"Content-Type" type
                "Access-Control-Allow-Origin" "*"}
      :body (str text-content) }  ))

(defn redirect [ url ]
  { :status 301 ; Permanent redirect
    :headers { "Location" url
               "Access-Control-Allow-Origin" "*"} } )

(defn handle-exception [ ex ]
  (println (clojure.stacktrace/print-stack-trace ex))
  [ :span { :style "color: #ff3030;" } (.getMessage ex) ] )

(defn execute-post [ message-type game-id game game-func history-item ]
  { :pre [ (game-state/game game-id) ] }
  (case message-type
    :info (str (game-func game))
    :view (xml-to-text (game-func game))
    :command (do (game-state/swap-game game-func history-item game-id) "ok") ; game-modifying commands
    :admin-command (do (game-state/swap-games game-func) "ok")  )) ; commands modifying the whole app state

(defn password-not-valid [ password role require-role ]
  (xml-to-text
    [ :span { :style "color: red;" }
      (str "Password " password " for " role " not valid for role " require-role ". ")
      "Return to" [ :a { :href "/login" } "Login" ] ] ))

(defn symbol-to-keyword [ s ] (if (symbol? s) (keyword s) s))

(defn- symbols-to-keywords [ [ cmd & pars ] ]
  (cons cmd (map symbol-to-keyword pars)))

(defn handle-post [ message-str games]
  (let [ message (read-string message-str)
         { game-id :game role :role password :password message-type :message-type raw-func :func } message
         func (symbols-to-keywords raw-func) ; (cmd a b c) -> (cmd :a :b :c)
         game (games game-id)
         game-func-with-role (binding [*ns* (find-ns 'glory-of-empires.main)] (eval func))
         game-func (fn [ game ] (game-func-with-role game role))
         require-role (get (meta game-func) :require-role)
         history-item { :time (str (java.util.Date.)) :command func :role role } ]
    (if (or (not require-role) (players/password-valid? require-role game message))
      (execute-post message-type game-id game game-func history-item)
      (password-not-valid password role require-role)   )))

(defn static-page [ path ]
  (let [ content (slurp path :encoding "UTF-8") ]
    (if (re-matches #".+css" path)
      (reply content "text/css")
      content )))

(def ignore-urls #{ "/favicon.ico" "/html/jquery.min.map" } )

(defn- handle-get [ uri query games ] ; returns ready response-map or string for normal HTTP 200 response
  (println "Query: " query)
  (case uri
    "/login" (login/login-page (keys games))
    "/create-game" (login/create-game-page)
    "/game" (command-page/html query)
    "/view" (view-page/html query)
    (cond
      (contains? ignore-urls uri) ""
      (re-matches #"\/html\/.+" uri) (static-page (subs uri 1))
      :else (redirect "/login") )))

(defn- handler-inner [request]
  (let [games (game-state/get-games)]
    (case (:request-method request)
      :post (let [message (slurp (:body request))]
              (try (handle-post message games) (catch Throwable e (xml-to-text (handle-exception e)))))
      :get (let [{uri :uri query-string :query-string} request]
             (handle-get uri (query/parse query-string) games)))))

(defn handler [request]
  (reload) ; REMOVE IN PRODUCION
  (let [ raw-reply (handler-inner request) ]
    (if (string? raw-reply)
      (reply raw-reply) ; format as HTTP 200 OK response
      raw-reply ))) ; already formatted as some HTTP response

(defn -main [& args]
  (game-state/load-games)
  (run-jetty handler {:port 3000} ))

(defn run [] (.start (Thread. -main))) ; Call from repl
