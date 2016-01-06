(ns glory-of-empires.map-test-data)

(def mini-game-state
  { :map
    { :a1 { :logical-pos [0 0],
            :system :abyz-fria,
            :id :a1,
            :controller :hacan,
            :ships {
                :dr7 {:type :dr, :id :dr7, :owner :hacan },
                :ca3 {:type :ca, :id :ca3, :owner :hacan } }
            :planets { :abyz { :controller :hacan
                               :units {
                                 :gf3 {:type :gf, :id :gf3, :owner :hacan }
                                 :pds1 {:type :pds, :id :pds1, :owner :hacan }
                                 :pds2 {:type :pds, :id :pds2, :owner :hacan } }}
                       :fria { :controller :hacan
                               :units {
                                 :gf1 {:type :gf, :id :gf1, :owner :hacan}
                                 :gf2 {:type :gf, :id :gf2, :owner :hacan}
                                 :sd1 {:type :sd, :id :sd1, :owner :hacan} } } } }
      :b1 { :logical-pos [1 0],
            :system :aah,
            :id :b1,
            :controller :norr,
            :ships {
               :fi3 {:type :fi, :id :fi3, :owner :norr },
               :fi8 {:type :fi, :id :fi8, :owner :norr } }
            :planets { :aah { :res 1 :inf 1 :loc [ 0 0 ] } } } },
    :players { :hacan {:id :hacan}, :norr {:id :norr}}} )


(def many-ships [
  {:type :cr, :id :cr1, :owner :norr }
  {:type :cr, :id :cr2, :owner :norr }
  {:type :fi, :id :fi1, :owner :norr }
  {:type :fi, :id :fi2, :owner :norr }
  {:type :fi, :id :fi3, :owner :norr }
  {:type :dr, :id :dr1, :owner :norr } ] )
