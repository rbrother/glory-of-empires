(ns glory-of-empires.map-test-data)

(def mini-game-state
  {
    :map {
           :a1 { :id :a1
                 :controller :hacan
                 :logical-pos [ 0 0]
                 :planets #{ :abyz :fria}
                 :system :abyz-fria}
           :b1 { :id :b1
                 :activated { :hacan true }
                 :controller :norr
                 :logical-pos [ 1 0]
                 :planets #{ :aah}
                 :system :aah}}
    :planets {
              :abyz { :id :abyz :controller :hacan :ready true}
              :fria { :id :fria :controller :hacan :ready false}
              :aah { :id :aah :controller :norr :ready true}}

    :units {
            :gf3 { :id :gf3 :location :a1 :planet :abyz :owner :hacan :type :gf}
            :pds1 { :id :pds1 :location :a1 :planet :abyz :owner :hacan :type :pds}
            :pds2 { :id :pds2 :location :a1 :planet :abyz :owner :hacan :type :pds}
            :gf1 { :id :gf1 :location :a1 :planet :fria :owner :hacan :type :gf}
            :gf2 { :id :gf2 :location :a1 :planet :fria :owner :hacan :type :gf}
            :sd1 { :id :sd1 :location :a1 :planet :fria :owner :hacan :type :sd}
            :ca3 { :id :ca3 :location :a1 :owner :hacan :type :ca}
            :dr7 { :id :dr7 :location :a1 :owner :hacan :type :dr}
            :de2 { :id :de2 :location :b1 :owner :norr :type :de}
            :fi1 { :id :fi1 :location :b1 :owner :norr :type :fi}
            :fi2 { :id :fi2 :location :b1 :owner :norr :type :fi}
            :fi3 { :id :fi3 :location :b1 :owner :norr :type :fi}
            :fi4 { :id :fi4 :location :b1 :owner :norr :type :fi}
            :fi5 { :id :fi5 :location :b1 :owner :norr :type :fi}
            :fi8 { :id :fi8 :location :b1 :owner :norr :type :fi}
            :ws1 { :id :ws1 :location :b1 :owner :norr :type :ws}}
    :players {
              :hacan { :id :hacan :tg 0 :command-pool 3 :strategy-alloc 2 :fleet-supply 3
                       :ac [ :minelayers :morale-boost ]
                       :pc []
                       :techs #{} }
              :norr { :id :norr :tg 0
                      :ac [ :privateers ]
                      :pc []
                      :command-pool 2 :strategy-alloc 2 :fleet-supply 4
                      :techs #{} }}
    :ac-deck [ :diplomatic-immunity :flank-speed :flank-speed :into-breach ]
    :pc-deck [ ]
    :ac-discard []
    :pc-discard [ ]
    :strategies #{
                  { :id :leadership4 :owner :norr :ready true :bonus 0 }
                  { :id :diplomacy4 :owner :norr :ready false :bonus 0 }
                  { :id :politics4  :ready true :bonus 1 }
                  { :id :production :ready true :bonus 0 }
                  { :id :trade4 :owner :hacan :ready false :bonus 0 }
                  { :id :warfare4 :owner :hacan :ready true :bonus 0 }
                  { :id :technology4 :ready true :bonus 1 }
                  { :id :bureaucracy :ready true :bonus 2 }
                  }
    :ship-counters { :ca 3 :de 2 :fi 8 :ws 1 :gf 3 :pds 2}})

(def many-ships [
                 {:type :cr, :id :cr1, :owner :norr}
                 {:type :cr, :id :cr2, :owner :norr}
                 {:type :fi, :id :fi1, :owner :norr}
                 {:type :fi, :id :fi2, :owner :norr}
                 {:type :fi, :id :fi3, :owner :norr}
                 {:type :dr, :id :dr1, :owner :norr}])
