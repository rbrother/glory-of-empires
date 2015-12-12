(ns glory-of-empires.races
  (:use clojure-common.utils))

; see http://gameknight.com/?page_id=474
(def all-races-arr
  [ { :id :letnev :name "The Barony of Letnev"
      :home-system :letnev
      :starting-tech [ :antimass :hylar-v ]
      :leaders [ :admiral :general :diplomat ]
      :trade-agreements [ 1 1 ]
      :staring-units [ :dreadnought :destroyer :carrier :gf :gf :gf ]
      :abilities [
        "+1 to Fleet Supply"
        "May spend 2 Trade Goods to get +1 in actual round of space combat or +2 in ground combat" ]
      :racial-techs [
        { :id :l4-disruptors, :cost 6  }
        { :id :noneuclidian-shielding, :cost 4 } ]  }
    { :id :saar :name "The Clan of Saar " }
    { :id :hacan :name "The Emirates of Hacan" }
    { :id :sol :name "The Federation of Sol" }
    { :id :mentak :name "The Mentak Coalition" }
    { :id :naalu :name "The Naalu Collective" }
    { :id :nekro :name "The Nekro Virus" }
    { :id :norr :name "The Sardakk N'orr" }
    { :id :jolnar :name "The Universities of Jol-Nar" }
    { :id :winnu :name "The Winnu" }
    { :id :xxcha :name "The Xxcha Kingdom" }
    { :id :yssaril :name "The Yssaril Tribes" }
    { :id :yin :name "The Brotherhood of Yin" }
    { :id :muaat :name "The Embers of Muaat" }
    { :id :creauss :name "The Ghosts of Creuss" }
    { :id :lisix :name "The L1z1x Mindnet" }
    { :id :arborec :name "The Arborec" }
    ] )

(def all-races (index-by-id all-races-arr))
