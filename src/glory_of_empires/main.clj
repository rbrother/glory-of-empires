(ns glory-of-empires.main
  (:use ring.adapter.jetty)
  (:use clojure-common.utils)
  (:use clojure-common.xml)
  (:use glory-of-empires.map)
  (:gen-class))

;----------------- game state ----------------------

(def game-state (atom { } ))

;----------------- commands ----------------------

; Commands change game-state

(defn command-fn [ [ command-id opts ] ]
  (fn [ state ]
    (case command-id
      :random-map (assoc state :map (make-random-map (get opts :size 3)))
      state ; default: do nothing
      )))

(defn run-command [ command ]
  (swap! game-state (command-fn command))
  "done")

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
      (reply
        (case message-id
          :random-map (run-command message)
          :map (let [ pieces (@game-state :map) ]
                 (if (nil? pieces) "No map"
                   (xml-to-text (map-to-svg (@game-state :map) (last message))))))))))

(defn -main [& args]
  (reset! game-state (load-from-file "game-state.clj"))
  (run-jetty handler {:port 3000} ) )
