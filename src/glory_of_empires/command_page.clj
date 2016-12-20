(ns glory-of-empires.command-page
    (:require [glory-of-empires.html :as html]))

(def command-examples-select
  (html/select { :id "examples" :onchange "ExampleChanged();" }
                 [ "round-board 3"
                   "rect-board 10 5"
                   "set-systems-random"
                   "set-system :c4 :mecatol-rex"
                   "del-system :c4"
                   "set-players :hacan \"hacanpassword\" :norr \"pdw2\" :naalu \"xyz\""
                   "new-unit :b2 :hacan :ca"
                   "new-unit :a2 :norr [ :ca :cr :fi :fi ]"
                   "new-unit :meer :hacan [ :gf :gf ]"
                   "del-unit :ca3"
                   "move-unit :ca1 :a2"
                   "move-unit [ :ca1 :ca2 ] :b1"
                   "move-unit [ :gf7 :pds3 ] :ca1 (not working yet)" ] ))

(def view-examples-select
  (html/select { :id "viewExamples" :onchange "ViewExampleChanged();" }
                 [ "board 0.4"
                   "players"
                   "vertical (view/board 0.3) (view/players)"
                   "horizontal (view/board 0.3) (view/players)" ] ))

(defn html [ ]
  (html/page "Glory of Empires" "/html/command-page.js" [
      (html/table {}
        [ "Game" [ :span { :id "gameName" } ] [ :a { :href "/login" } "Logout" ] ]
        [ "Command" [ :input { :style "width: 300px;" :id "command" :autofocus "true" } ]
                    [ :button { :type "button" :onclick "ExecuteCommand();" } "Execute" ]
                    "Examples" command-examples-select ]
        [ " " [ { :colspan 4 }
                [ :span { :id "currentCommand" :style "color: green;" } ] " - "
                [ :span { :id "commandResult" } ] ] ]
        [ "View" [ :input { :style "width: 300px;" :id "viewDefinition" :value "board" :onchange "ViewDefinitionChanged();" } ]
          [ :button { :type "button" :onclick "OpenView();" } "Open" ]
          "Examples" view-examples-select [ :span { :id "viewResult" } ] ] )
      [ :div { :id "view" :style "margin: 8px;" } ] ] ))
