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

(defn run-command [ command ]
  (swap! game-state command)
  "ok")

(defn get-map
  ( [] (get-map {}) )
  ( [ opts ]
    (let [ m (@game-state :map) ]
      (if (nil? m) "No map" (map-to-svg m opts)))))

(defn set-random-map
  ( [] (set-random-map { :size 3 } ))
  ( [ opts ]
    (run-command
      (fn [ state ] (assoc state :map (make-random-map (get opts :size 3)))))))

;----------------- web server ----------------------

(defn reply [ content ]
  { :status 200
    :headers {"Content-Type" "text/html" "Access-Control-Allow-Origin" "*"}
    :body (xml-to-text content) }  )

(defn handle-exception [ ex ]
  (println (str ex))
  [ :span { :style "color: #ff0000;" } (str "Error: " (.getMessage ex)) ] )

(defn handler [request]
  (println "------------ request received -----------")
  (let [ message-text (slurp (:body request)) ]
    (println message-text)
    (let [ message (read-string message-text) ]
      (reply (try (eval message) (catch Exception e (handle-exception e)))))))

(defn -main [& args]
  (reset! game-state (load-from-file "game-state.clj"))
  (run-jetty handler {:port 3000} ) )
