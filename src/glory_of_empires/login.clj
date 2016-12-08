(ns glory-of-empires.login
    (:require [glory-of-empires.html :as html]))

(defn- game-select [ game-names ]
  (html/select "game" (concat [ "*choose*" ] game-names)))

(defn login-page [ game-names ]
  (html/page "Glory of Empires login" ""
    [ [ :h1 "Glory of Empires" ]
      [ :h3 "Select Existing game" ]
      (html/table [ "Game Name" (game-select game-names) ] )
      [ :p "Or " [ :a { :href "create-game.html" } "create new game" ] ] ]))

(defn create-game-page []
  (html/table
    [ "Name" [ :input ] ]
    [ "GM Password" [ :input ] ] ))

