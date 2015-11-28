(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.utils)
  (:use glory-of-empires.map)
  (:gen-class))

;----------------- game state ----------------------

(def game-state (atom nil))

;----------------- web server ----------------------

(defn reply [ content ]
  { :status 200
    :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
    :body content }  )

(defn handler [request]
  (println "------------ request received -----------")
  (let [ message-text (slurp (:body request)) ]
    (println message-text)
    (let [ message (read-string message-text)
           message-id (first message) ]
      (cond
        (= message-id :command)
          (reply "done")
        (= message-id :map)
          (reply (get-random-map-svg-text (last message)))))))

(defn -main [& args]
  (reset! game-state (load-from-file "game-state.clj"))
  (run-jetty handler {:port 3000} ) )
