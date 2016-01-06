(ns glory-of-empires.map-test-data)

(def mini-game-state
  { :map {
        :a1 {   :controller :hacan
            :id :a1
            :logical-pos [ 0 0 ]
            :planets {   :abyz {   :controller :hacan
                    :units { :gf3 { :id :gf3 :owner :hacan :type :gf }
                             :pds1 { :id :pds1 :owner :hacan :type :pds }
                             :pds2 { :id :pds2 :owner :hacan :type :pds } } }
                :fria {   :controller :hacan
                    :units { :gf1 { :id :gf1 :owner :hacan :type :gf }
                             :gf2 { :id :gf2 :owner :hacan :type :gf }
                             :sd1 { :id :sd1 :owner :hacan :type :sd } } } }
            :ships { :ca3 { :id :ca3 :owner :hacan :type :ca }
                     :dr7 { :id :dr7 :owner :hacan :type :dr } }
            :system :abyz-fria }
        :b1 {   :controller :norr
            :id :b1
            :logical-pos [ 1 0 ]
            :planets { :aah { :inf 1 :loc [ 0 0 ] :res 1 } }
            :ships {   :de2 { :id :de2 :owner :norr :type :de }
                :fi1 { :id :fi1 :owner :norr :type :fi }
                :fi2 { :id :fi2 :owner :norr :type :fi }
                :fi3 { :id :fi3 :owner :norr :type :fi }
                :fi4 { :id :fi4 :owner :norr :type :fi }
                :fi5 { :id :fi5 :owner :norr :type :fi }
                :fi8 { :id :fi8 :owner :norr :type :fi }
                :ws1 { :id :ws1 :owner :norr :type :ws } }
            :system :aah } }
    :players { :hacan { :id :hacan } :norr { :id :norr } }
    :ship-counters { :de 2 :fi 5 :ws 1 } } )

(def many-ships [
  {:type :cr, :id :cr1, :owner :norr }
  {:type :cr, :id :cr2, :owner :norr }
  {:type :fi, :id :fi1, :owner :norr }
  {:type :fi, :id :fi2, :owner :norr }
  {:type :fi, :id :fi3, :owner :norr }
  {:type :dr, :id :dr1, :owner :norr } ] )
