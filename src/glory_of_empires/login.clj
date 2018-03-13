(ns glory-of-empires.login
    (:require [glory-of-empires.html :as html]))

(defn- game-select [ game-names ]
  (html/select
    { :id "game" :name "gameName" :onchange "GameSelected();" }
    (concat [ "--choose--" ] game-names)))

(def login-script "/html/login.js")

(defn login-page [ game-names ]
  (html/page "Glory of Empires login" login-script
             [:h1 "Glory of Empires"]
             [:hr]
             [:h3 "Select Existing game"]
             [:form {:method "GET" :action "game"}
              (html/table {} [
                              ["Game Name" (game-select game-names)]
                              ["Role" [:span {:id "role-selector"}]]
                              ["Password" [:input {:id "password" :name "password"}]]])
              [:input {:type "submit" :value "Open Game"}]]
             [:hr]
             [:p "Or " [:a {:href "create-game"} "create new game"]]))

(defn create-game-page []
  (html/page "Glory of Empires create game" login-script
             [:h1 "Glory of Empires"]
             [:hr]
             [:h3 "Create new game"]
             (html/table {} [
                             ["Game Name" [:input {:id "game" :name "gameName"}] "Use lower case letters and minus (-) only"]
                             ["GM Password" [:input {:id "password" :name "password"}]]])
             [:button {:onclick "CreateGame();"} "Create Game"]
             [:p {:id "result"}]))

