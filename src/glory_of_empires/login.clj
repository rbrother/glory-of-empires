(ns glory-of-empires.login
    (:require [glory-of-empires.html :as html]))

(defn login-page [ game-names ]
  (html/page "Glory of Empires login" ""
    [ [ :h1 "Glory of Empires" ]
      [ :h3 "Select Existing game" ]
      [ :table
        [ :tr
          [ :td "Game Name" ]
          [ :td (html/select "game" (concat [ "*choose*" ] game-names)) ]]]
      [ :p "Or " [ :a { :href "create-game.html" } "create new game" ] ] ]))

(defn create-game-page []
  [ :table
        [ :tr [ :td { :colspan 3 } [ :h3 "Create new game" ] ] ]
        [ :tr [ :td "Name" ] [ :td {} [ :input ] ] ]
        [ :tr [ :td "GM Password" ] [ :td [ :input ] ] ]
        [ :tr [ :td [ :button "Create" ] ] ] ] )
