(ns glory-of-empires.command
  (:require [clojure-common.utils :as utils])
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.players :as players])
  (:require [glory-of-empires.ac :as ac]) )

; There commands do not actually do anything, but they
; return game-updating func for the specified command

;------------ command helpers ---------------

; The whole planet-list is re-created when board is modified, so board modification
; should not be done after game starts and there is some info on planets
(defn- update-planets [ { board :map :as game } ]
  (let [ all-planets
         (->>  board (vals) (map :planets)
               (map seq) (flatten) (remove nil?)
               (map (fn [id] { :id id :controller nil }))  ) ]
    (assoc game :planets (utils/index-by-id all-planets))  ))

(defn- board-command [ board-func ]
  (fn [ game & pars ] (update-planets (update game :map board-func))))

(defn- make-board-command [ new-board ]
  ^{ :require-role :game-master }
  (fn [ game & pars ] (update-planets (merge game { :map new-board :ship-counters {} } ))))

;----------- map commands --------------------

(defn round-board
  ( [] (round-board 3) )
  ( [ size ]
    (make-board-command (board/round-board size))))

(defn rect-board
  ( [] (rect-board 3 3) )
  ( [ width height ]
    (make-board-command (board/rect-board width height))))

(defn set-systems-random [ & opt ] (board-command #(apply board/random-systems % opt)))

(defn set-system [ loc-id system-id ]
  (board-command #(board/swap-system % [ loc-id system-id ] )))

(defn del-system [ loc-id ]
  (board-command #(dissoc % loc-id)))

;------------- players -----------------

(defn set-players [ & player-ids-and-passwords ]
  ^{ :require-role :game-master }
  (fn [ game & pars ] (players/set-players game player-ids-and-passwords)))

;------------ unit commands ------------------

(defn- resolve-unit-types [ [ a b & others :as types ] ]
  (cond (not a) '()
        (ships/valid-unit-type? a) (cons a (resolve-unit-types (next types)))
        (number? a) (concat (take a (repeat b)) (resolve-unit-types others))
        :else (throw (Exception. (str "Unit type unknown " a)))   ))

(defn new [ owner & pars ]
  (let [ loc-id (last pars) types (drop-last pars) ]
    ^{ :require-role :player }
    (fn [ game role ] (ships/new-units loc-id owner (resolve-unit-types types) game))  ))

; units-defs can be combination of (1) unit-ids eg. :ws3 , (2) unit types eg. :gf, (3) count + type eg. 3 :gf.
; returns list of unit-id:s
(defn- resolve-unit-ids [ [ a b & others :as unit-defs ] available-units ]
  (cond (not a) '()
        (= a :from)
          (let [ units-at-loc (filter #(= (% :location) b) (vals available-units)) ]
            (resolve-unit-ids others (utils/index-by-id units-at-loc))   )
        (= a :all) (keys available-units)
        (contains? available-units a) ; unit-id eg. :ws3
            (cons a (resolve-unit-ids (next unit-defs) (dissoc available-units a)))
        (ships/valid-unit-type? a) ; unit-type eg. :ca
          (let [ unit-id (->> available-units (vals) (filter #(= (% :type) a)) (map :id) (sort) (first) ) ]
            (if unit-id
              (cons unit-id (resolve-unit-ids (next unit-defs) (dissoc available-units unit-id)))
              (throw (Exception. (str "Unit of type " a " not found from the location")))   ))
        (number? a) ; count and type eg. 3 :gf. Expand to :gf :gf :gf
          (resolve-unit-ids (concat others (take a (repeat b))) available-units)
        :else (throw (Exception. (str "Unit definition unknown " a)))   ))

(defn del [ & unit-pars ]
  ^{ :require-role :player }
  (fn [ { all-units :units :as game } role ]
    (let [ unit-ids (resolve-unit-ids unit-pars all-units) ]
      (ships/del-units unit-ids game))))

(defn move [ & pars ]
  (let [ dest (last pars), unit-pars (drop-last pars) ]
    ^{ :require-role :player }
    (fn [ { all-units :units :as game } role ]
        (let [ unit-ids (resolve-unit-ids unit-pars all-units) ]
          (ships/move-units unit-ids dest game)    ))))

;----------- card-commands commands ------------------

(defn ac-deck-create    "Adds a fresh shuffled pack of AC: sto "
  []
  ^{:require-role :game-master}
  (fn [ game role ]
    (-> game
        (assoc :ac-deck (ac/create-ac-deck)))))

(defn ac-get    "get AC from deck to a player"
  ( [] ^{:require-role :player} (ac-get nil))
  ( [ player-in ] ^{:require-role :player}
    (fn [ game role ]
      (let [player (or player-in role)
            card (first (:ac-deck game))]
        (-> game
            (update-in [:ac-deck] rest)
            (update-in [:players player :ac] #(set (conj % card)))    )))))

;----------- high-level commands ------------------

(defn start-game
  "Adds players starting units to their home-systems, shuffles new pack of AC and PC and marks round 1 started"
  []
  ^{ :require-role :game-master }
  (fn [ game role ]
    (-> game
        (assoc :ac-deck (ac/create-ac-deck))   )))

