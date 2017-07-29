(ns glory-of-empires.command-page
    (:require [glory-of-empires.html :as html]))

(def command-examples-select
  (html/select { :id "examples" :onchange "ExampleChanged();" }
                 [ "---- game setup ----"
                   "round-board 3"
                   "rect-board 10 5"
                   "set-systems-random"
                   "set-system c4 mecatol-rex"
                   "del-system c4"
                   "set-players hacan \"abc\" norr \"xyz\" naalu \"123\""
                   "---- unit operations ----"
                   "new hacan ca b2"
                   "new norr ca cr 2 fi a2"
                   "new hacan 2 gf meer"
                   "new 2 gf meer"
                   "del ca3"
                   "del from c1 2 gf ca"
                   "move ca1 a2"
                   "move ca1 ca2 b1"
                   "move from d1 ws2 ca 3 gf b1"
                   "move from d1 ws2 ca 3 gf b1"
                   "move from d1 all b1"
                   "---- cards ----"
                   "ac-deck-create"
                   "ac-get"
                   "ac-get hacan"
                   "ac-play local-unrest \"details...\"" ] ))

(def view-examples-select
  (html/select { :id "viewExamples" :onchange "ViewExampleChanged();" }
                 [ "systems 0.5"
                   "board 0.4"
                   "players"
                   "vertical (view/board 0.3) (view/players)"
                   "horizontal (view/board 0.3) (view/players)" ] ))

(defn html [ { game-name :gameName role :role } ]
  (html/page "Glory of Empires" "/html/command-page.js" [
      (html/table {}
        [ "Game" (str (name game-name) " / " (name role) ) ]
        [ "Command" [ :input { :style "width: 300px;" :id "command" :autofocus "true" } ]
                    [ :button { :type "button" :onclick "ExecuteCommand();" } "Execute" ]
                    "Examples" command-examples-select ]
        [ " " [ { :colspan 4 }
                [ :span { :id "currentCommand" :style "color: green;" } ] " - "
                [ :span { :id "commandResult" } ] ] ]
        [ "View" [ :input { :style "width: 300px;" :id "viewDefinition" :value "board" :onchange "ViewDefinitionChanged();" } ]
          " " "Examples" view-examples-select [ :span { :id "viewResult" } ] ]
        [ " " [ { :colspan 4 } [ :span { :id "viewStatus" :style "color: green;" } ] ] ] )
      [ :div { :id "view" :style "margin: 8px;" } ] ] ))
