(ns glory-of-empires.command
  (:require [clojure-common.utils :as utils])
  (:require [glory-of-empires.map :as board])
  (:require [glory-of-empires.ships :as ships])
  (:require [glory-of-empires.players :as players]) )

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
  (fn [ game ] (update-planets (update game :map board-func))))

(defn- make-board-command [ new-board ]
  ^{ :require-role :game-master }
  (fn [ game ] (update-planets (merge game { :map new-board :ship-counters {} } ))))

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
  (fn [ game ] (players/set-players game player-ids-and-passwords)))

;------------ unit commands ------------------

(defn new-unit [ loc-id owner type ]
  (let [ types (if (sequential? type) type [ type ]) ]
    #(board/new-units loc-id owner types % )))

(defn del-unit [ unit-id ]
  (fn [game] (board/del-unit unit-id game)))

; units-defs can be combination of (1) unit-ids eg. :ws3 , (2) unit types eg. :gf, (3) count + type eg. 3 :gf.
; returns list of unit-id:s
(defn units-from [ [ a b :as unit-defs ] units-in-loc ]
  (cond (empty? unit-defs) '()
        (contains? units-in-loc a) ; unit-id eg. :ws3
            (cons a (units-from (next unit-defs) (dissoc units-in-loc a)))
        (ships/valid-unit-type? a) ; unit-type eg. :ca
          (let [ unit-id (->> units-in-loc (vals) (filter #(= (% :type) a)) (map :id) (first) ) ]
            (if unit-id
              (cons unit-id (units-from (next unit-defs) (dissoc units-in-loc unit-id)))
              (throw (Exception. (str "Unit of type " a " not found from the location")))   ))
        (number? a) ; count and type eg. 3 :gf. Expand to :gf :gf :gf
          (units-from (concat (nnext unit-defs) (take a (repeat b))) units-in-loc)
        :else (throw (Exception. (str "Unit definition unknown " a)))   ))

(defn- resolve-unit-ids [ [ a b c ] all-units ]
  (cond  (= a :from) (units-from c (utils/index-by-id (filter #(= (% :location) b) all-units)))
         (sequential? a) a
         :else [ a ] ))

(defn move [ & pars ]
  (let [ dest (last pars), unit-pars (drop-last pars) ]
    (fn [ { all-units :units :as game } ]
        (let [ unit-ids (resolve-unit-ids unit-pars (vals all-units)) ]
          (println unit-ids)
          (board/move-units unit-ids dest game)    ))))


