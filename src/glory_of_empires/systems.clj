(ns glory-of-empires.systems
  (:use clojure-common.utils))

(def all-systems-arr
  [ { :id :aah       :image "1planet/Tile-Aah.gif"     :planets { :aah { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :acheron   :image "1planet/Tile-Acheron.gif" :planets { :acheron { :res 1 :inf 2 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :aeon      :image "1planet/Tile-Aeon.gif"    :planets { :aeon { :res 1 :inf 3 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :aker      :image "1planet/Tile-Aker.gif"    :planets { :aker { :res 1 :inf 2 :loc [ 0 0 ] } } }
    { :id :ammit     :image "1planet/Tile-Ammit.gif"   :planets { :ammit { :res 1 :inf 0 :loc [ 0 0 ] } } }
    { :id :amun      :image "1planet/Tile-Amun.gif"    :planets { :amun { :res 0 :inf 1 :loc [ 0 0 ] } } }
    { :id :andjety   :image "1planet/Tile-Andjety.gif" :planets { :andjety { :res 3 :inf 0 :loc [ 0 0 ] } } }
    { :id :anhur     :image "1planet/Tile-Anhur.gif"   :planets { :anhur { :res 2 :inf 1 :loc [ 0 0 ] } } }
    { :id :ankh      :image "1planet/Tile-Ankh.gif"    :planets { :ankh { :res 2 :inf 2 :loc [ 0 0 ] } } }
    { :id :anuket    :image "1planet/Tile-Anuket.gif"  :planets { :anuket { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :apis      :image "1planet/Tile-Apis.gif"    :planets { :apis { :res 0 :inf 2 :loc [ 0 0 ] } } }
    { :id :asgard    :image "1planet/Tile-Asgard.gif"  :planets { :asgard { :res 2 :inf 2 :loc [ 0 0 ] :special "Red artifact, refresh for 2 storm troops" } } }
    { :id :asgard3   :image "1planet/Tile-Asgard_III.gif" :planets { :asgard3 { :res 1 :inf 3 :tech { :red 1 } :loc [ 0 0 ]
                     :special "Occupier gains: Built-in space-dock unit, 3 PDS with deep-space-cannon, 9 fighter support. Invading ground forces -1 on combat."} } }
    { :id :astennu   :image "1planet/Tile-Astennu.gif" :planets { :astennu { :res 2 :inf 0 :loc [ 0 0 ] } } }
    { :id :aten      :image "1planet/Tile-Aten.gif"    :planets { :aten { :res 0 :inf 3 :loc [ 0 0 ] } } }
    { :id :babi      :image "1planet/Tile-Babi.gif"    :planets { :babi { :res 2 :inf 0 :loc [ 0 0 ] } } }
    { :id :bakha     :image "1planet/Tile-Bakha.gif"   :planets { :bakha { :res 2 :inf 3 :loc [ 0 0 ] } } }
    { :id :bast      :image "1planet/Tile-Bast.gif"    :planets { :bast { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :beriyil   :image "1planet/Tile-Beriyil.gif" :planets { :beriyil { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :bes       :image "1planet/Tile-Bes.gif"     :planets { :bes { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :capha     :image "1planet/Tile-Capha.gif"   :planets { :capha { :res 3 :inf 0 :loc [ 0 0 ] } } }
    { :id :chensit   :image "1planet/Tile-Chensit.gif" :planets { :chensit { :res 1 :inf 1 :loc [ 0 0 ] } } }
    { :id :chnum     :image "1planet/Tile-Chnum.gif"   :planets { :chnum { :res 2 :inf 0 :loc [ 0 0 ] } } }
    { :id :chuuka    :image "1planet/Tile-Chuuka.gif"  :planets { :chuuka { :res 1 :inf 0 :tech { :blue 1 } :loc [ 0 0 ] } } }
    { :id :cicerus   :image "1planet/Tile-Cicerus.gif" :planets { :cicerus { :res 2 :inf 2 :loc [ 0 0 ] } } }
    { :id :coruscant :image "1planet/Tile-Coruscant.gif" :planets { :coruscant { :res 2 :inf 4 :loc [ 0 0 ] } } }

    { :id :dedun     :image "1planet/Tile-Dedun.gif" :planets { :dedun { :res 2 :inf 0 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :deimo     :image "1planet/Tile-Deimo.gif" :planets { :deimo { :res 0 :inf 1 :loc [ 0 0 ]
                     :special "Occupier gains: Built-in space-dock with no fighter support and unlimited prod capacity." } } }
    { :id :discworld :image "1planet/Tile-Discworld.gif" :planets { :discworld { :res 3 :inf 3 :tech { :any 1 } :loc [ 0 0 ] } } }
    { :id :dune      :image "1planet/Tile-Dune.gif" :planets { :dune { :res 0 :inf 4 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :elnath    :image "1planet/Tile-Elnath.gif" :planets { :elnath { :res 2 :inf 0 :tech { :blue 1 } :loc [ 0 0 ] } } }
    { :id :everra    :image "1planet/Tile-Everra.gif" :type :nebula, :planets { :everra { :res 3 :inf 1 :loc [ 0 0 ] } } }
    { :id :faunus    :image "1planet/Tile-Faunus.gif" :planets { :faunus { :res 1 :inf 3 :tech { :green 2 } :loc [ 0 0 ] } } }
    { :id :fiorina   :image "1planet/Tile-Fiorina.gif" :planets { :fiorina { :res 2 :inf 1 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :floyd4    :image "1planet/Tile-FloydIV.gif" :planets { :floyd4 { :res 0 :inf 2 :tech { :red 1 } :loc [ 0 0 ] } } }
    { :id :garbozia  :image "1planet/Tile-Garbozia.gif" :planets { :garbozia { :res 2 :inf 1 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :heimat    :image "1planet/Tile-Heimat.gif" :planets { :heimat { :res 3 :inf 2 :loc [ 0 0 ]
                     :special "Fleet supply limit may be exceeded in this system by 1" } } }
    { :id :hopes-end :image "1planet/Tile-Hopes_End.gif" :planets { :hopes-end { :res 3 :inf 0 :loc [ 0 0 ]
                     :special "May be exhausted for 2 shock-troops" } } }
    { :id :inaak     :image "1planet/Tile-Inaak.gif" }
    { :id :industrex :image "1planet/Tile-Industrex.gif" }
    { :id :iskra     :image "1planet/Tile-Iskra.gif" }
    { :id :ithaki    :image "1planet/Tile-Ithaki.gif" }
    { :id :kanite    :image "1planet/Tile-Kanite.gif" }
    { :id :kauket    :image "1planet/Tile-Kauket.gif" }
    { :id :kazenoeki :image "1planet/Tile-Kazenoeki.gif" }
    { :id :khepri    :image "1planet/Tile-Khepri.gif" }
    { :id :khnum     :image "1planet/Tile-Khnum.gif" }
    { :id :klendathu :image "1planet/Tile-Klendathu.gif" }
    { :id :kobol     :image "1planet/Tile-Kobol.gif" :planets { :kobol { :res 2 :inf 5 :loc [ 0 0 ] } } }
    { :id :laurin    :image "1planet/Tile-Laurin.gif" }
    { :id :lesab     :image "1planet/Tile-Lesab.gif" }
    { :id :lodor     :image "1planet/Tile-Lodor.gif" }
    { :id :lv426     :image "1planet/Tile-Lv426.gif" }
    { :id :medusa-v  :image "1planet/Tile-Medusa_V.gif" }
    { :id :mehar-xull :image "1planet/Tile-Mehar_Xull.gif" }
    { :id :mirage    :image "1planet/Tile-Mirage.gif" }
    { :id :myrkr     :image "1planet/Tile-Myrkr.gif" }
    { :id :nanan     :image "1planet/Tile-Nanan.gif" }
    { :id :natthar   :image "1planet/Tile-Natthar.gif" }
    { :id :nef       :image "1planet/Tile-Nef.gif" }
    { :id :nexus     :image "1planet/Tile-Nexus.gif" }
    { :id :niiwa-sei :image "1planet/Tile-Niiwa-Sei.gif" }
    { :id :pakhet    :image "1planet/Tile-Pakhet.gif" }
    { :id :parzifal  :image "1planet/Tile-Parzifal.gif" :planets { :parzifal { :res 4 :inf 3 :tech { :green 1 } :loc [ 0 0 ] } } }
    { :id :perimeter :image "1planet/Tile-Perimeter.gif" :planets { :perimeter { :res 2 :inf 2 :loc [ 0 0 ] } } }
    { :id :petbe     :image "1planet/Tile-Petbe.gif" }
    { :id :primor    :image "1planet/Tile-Primor.gif" }
    { :id :ptah      :image "1planet/Tile-Ptah.gif" }
    { :id :qetesh    :image "1planet/Tile-Qetesh.gif" }
    { :id :quann     :image "1planet/Tile-Quann.gif" }
    { :id :radon     :image "1planet/Tile-Radon.gif" }
    { :id :saudor    :image "1planet/Tile-Saudor.gif" }
    { :id :sem-lore  :image "1planet/Tile-Sem-Lore.gif" }
    { :id :shai      :image "1planet/Tile-Shai.gif" }
    { :id :shool     :image "1planet/Tile-Shool.gif" }
    { :id :solitude  :image "1planet/Tile-Solitude.gif" }
    { :id :sulako    :image "1planet/Tile-Sulako.gif" }
    { :id :suuth     :image "1planet/Tile-Suuth.gif" }
    { :id :tarmann   :image "1planet/Tile-Tarmann.gif" }
    { :id :tefnut    :image "1planet/Tile-Tefnut.gif" }
    { :id :tempesta  :image "1planet/Tile-Tempesta.gif" }
    { :id :tenenit   :image "1planet/Tile-Tenenit.gif" }
    { :id :theom     :image "1planet/Tile-Theom.gif" }
    { :id :thibah    :image "1planet/Tile-Thibah.gif" }
    { :id :ubuntu    :image "1planet/Tile-Ubuntu.gif" }
    { :id :vefut2    :image "1planet/Tile-Vefut_II.gif" }
    { :id :wadjet    :image "1planet/Tile-Wadjet.gif" }
    { :id :wellon    :image "1planet/Tile-Wellon.gif" }
    { :id :wepwawet  :image "1planet/Tile-Wepwawet.gif" }

    { :id :abyz-fria :image "2planet/Tile-Abyz-Fria.gif"
      :planets { :abyz { :res 3 :inf 0 :loc [ -75 -70 ] }
                 :fria { :res 2 :inf 0 :tech { :blue 1 } :loc [ 75 70 ] } } }
    { :id :achill    :image "2planet/Tile-Achill.gif" }
    { :id :aeterna-tammuz :image "2planet/Tile-AeternaTammuz.gif" }
    { :id :arinam-meer :image "2planet/Tile-Arinam-Meer.gif" }
    { :id :arnor-lor :image "2planet/Tile-Arnor-Lor.gif" }
    { :id :aztlan-senhora :image "2planet/Tile-Aztlan-Senhora.gif" }
    { :id :bereg-lirta4 :image "2planet/Tile-Bereg-Lirta_IV.gif" }
    { :id :bondar-maclean :image "2planet/Tile-Bondar-MacLean.gif" }
    { :id :cato :image "2planet/Tile-Cato.gif" }
    { :id :centauri-gral :image "2planet/Tile-Centauri-Gral.gif" }
    { :id :coorneeq-resculon :image "2planet/Tile-Coorneeq-Resculon.gif" }
    { :id :custos-xhal :image "2planet/Tile-Custos-Xhal.gif" }
    { :id :dal-bootha-xxehan :image "2planet/Tile-Dal_Bootha-Xxehan.gif" }
    { :id :garneau-hadfield :image "2planet/Tile-Garneau-Hadfield.gif" }
    { :id :gilea-xerel-wu :image "2planet/Tile-Gilea-Xerel_Wu.gif" }
    { :id :gurmee-uguza :image "2planet/Tile-Gurmee-Uguza.gif" }
    { :id :hana :image "2planet/Tile-Hana.gif" }
    { :id :hercalor-tiamat :image "2planet/Tile-Hercalor-Tiamat.gif" }
    { :id :kazaros-mirabar :image "2planet/Tile-Kazaros-Mirabar.gif" }
    { :id :lazar-sakulag :image "2planet/Tile-Lazar-Sakulag.gif" }
    { :id :lisis-velnor :image "2planet/Tile-Lisis-Velnor.gif" }
    { :id :machall-sorkragh :image "2planet/Tile-MachallSorkragh.gif" }
    { :id :masada-jamiah :image "2planet/Tile-Masada-Jamiah.gif" }
    { :id :massada-iuddaea :image "2planet/Tile-Massada-Iuddaea.gif" }
    { :id :mellon-zohbat :image "2planet/Tile-Mellon-Zohbat.gif" }
    { :id :nadir-lucus :image "2planet/Tile-Nadir-Lucus.gif" }
    { :id :new-albion-starpoint :image "2planet/Tile-New_Albion-Starpoint.gif" }
    { :id :nummantia-hisspania :image "2planet/Tile-Nummantia-Hisspania.gif" }
    { :id :othor-aiel :image "2planet/Tile-OthorAiel.gif"
      :planets { :othor { :res 1 :inf 3 :tech { :green 1 } } :aiel { :res 2 :inf 0 :tech { :blue 1 } } } }
    { :id :pankow-prenzlberg :image "2planet/Tile-Pankow-Prenzlberg.gif" }
    { :id :perro-senno :image "2planet/Tile-Perro-Senno.gif" }
    { :id :qucenn-rarron :image "2planet/Tile-Qucenn-Rarron.gif" }
    { :id :renenutet-osiris :image "2planet/Tile-Renenutet-Osiris.gif" }
    { :id :renpet-tawaret :image "2planet/Tile-Renpet-Tawaret.gif" }
    { :id :resheph-ogdoad :image "2planet/Tile-Resheph-Ogdoad.gif" }
    { :id :salem-iogu :image "2planet/Tile-SalemIogu.gif" }
    { :id :shiva-wishnu :image "2planet/Tile-Shiva-Wishnu.gif" }
    { :id :sulla-martinez :image "2planet/Tile-SullaMartinez.gif" }
    { :id :sumerian-arcturus :image "2planet/Tile-Sumerian-Arcturus.gif" }
    { :id :temujin-avanti :image "2planet/Tile-Temujin-Avanti.gif" }
    { :id :tequran-torkan :image "2planet/Tile-Tequran-Torkan.gif" }
    { :id :thoth-mafdet :image "2planet/Tile-Thoth-Mafdet.gif" }
    { :id :tsion-bellatrix :image "2planet/Tile-Tsion-Bellatrix.gif" }
    { :id :vega :image "2planet/Tile-Vega.gif" }
    { :id :verne-quebec :image "2planet/Tile-Verne-Quebec.gif" }
    { :id :vortekai-deguzz :image "2planet/Tile-Vortekai-Deguzz.gif" }

    { :id :ashtroth-loki-abaddon :image "3planet/Tile-Ashtroth-Loki-Abaddon.gif" }
    { :id :rigel :image "3planet/Tile-Rigel.gif" }
    { :id :elder-uhuru-amani :image "3planet/Tile-Elder-Uhuru-Amani.gif" }
    { :id :tianshang-tiangu-changtian :image "3planet/Tile-Tianshang-Tiangu-Changtian.gif" }

    { :id :akoytay   :type :home-system   :image "HomeSystem/Tile-HS-Akoytay.gif" }
    { :id :alkari    :type :home-system   :image "HomeSystem/Tile-HS-Alkari.gif" }
    { :id :alliance  :type :home-system   :image "HomeSystem/Tile-HS-Alliance.gif" }
    { :id :altair    :type :home-system   :image "HomeSystem/Tile-HS-Altair.gif" }
    { :id :andorian  :type :home-system   :image "HomeSystem/Tile-HS-Andorian.gif" }
    { :id :arborec   :type :home-system   :image "HomeSystem/Tile-HS-Arborec.gif" }
    { :id :asari     :type :home-system   :image "HomeSystem/Tile-HS-Asari.gif" }
    { :id :asgard    :type :home-system   :image "HomeSystem/Tile-HS-Asgard.gif" }
    { :id :atreides  :type :home-system   :image "HomeSystem/Tile-HS-Atreides.gif" }
    { :id :bajoran   :type :home-system   :image "HomeSystem/Tile-HS-Bajoran.gif" }
    { :id :batarian  :type :home-system   :image "HomeSystem/Tile-HS-Batarian.gif" }
    { :id :bene-gesserit  :type :home-system :image "HomeSystem/Tile-HS-Bene_Gesserit.gif" }
    { :id :bene-tleilaxu  :type :home-system :image "HomeSystem/Tile-HS-Bene_Tleilaxu.gif" }
    { :id :betazoid  :type :home-system   :image "HomeSystem/Tile-HS-Betazoid.gif" }
    { :id :bolian    :type :home-system   :image "HomeSystem/Tile-HS-Bolian.gif" }
    { :id :bulrathi  :type :home-system   :image "HomeSystem/Tile-HS-Bulrathi.gif" }
    { :id :caitian   :type :home-system   :image "HomeSystem/Tile-HS-Caitian.gif" }
    { :id :cannibals :type :home-system   :image "HomeSystem/Tile-HS-Cannibals.gif" }
    { :id :cardassian  :type :home-system  :image "HomeSystem/Tile-HS-Cardassian.gif" }
    { :id :cartel   :image "HomeSystem/Tile-HS-Cartel.gif" }
    { :id :centauri   :image "HomeSystem/Tile-HS-Centauri.gif" }
    { :id :chaos   :image "HomeSystem/Tile-HS-Chaos.gif" }
    { :id :chromatics   :image "HomeSystem/Tile-HS-Chromatics.gif" }
    { :id :corrino   :image "HomeSystem/Tile-HS-Corrino.gif" }
    { :id :creuss   :image "HomeSystem/Tile-HS-Creuss.gif" }
    { :id :creuss-gate   :image "HomeSystem/Tile-HS-Creuss_Gate.gif" }
    { :id :croax   :image "HomeSystem/Tile-HS-Croax.gif" }
    { :id :dark-eldar   :image "HomeSystem/Tile-HS-DarkEldar.gif" }
    { :id :darlocks   :image "HomeSystem/Tile-HS-Darlocks.gif" }
    { :id :dominion   :image "HomeSystem/Tile-HS-Dominion.gif" }
    { :id :dusun   :image "HomeSystem/Tile-HS-Dusun.gif" }
    { :id :earth   :image "HomeSystem/Tile-HS-Earth.gif" }
    { :id :eldar   :image "HomeSystem/Tile-HS-Eldar.gif" }
    { :id :elerians   :image "HomeSystem/Tile-HS-Elerians.gif" }
    { :id :elzenkar   :image "HomeSystem/Tile-HS-Elzenkar.gif" }
    { :id :embers   :image "HomeSystem/Tile-HS-Embers.gif" }
    { :id :fedaron   :image "HomeSystem/Tile-HS-Fedaron.gif" }
    { :id :federation   :image "HomeSystem/Tile-HS-Federation.gif" }
    { :id :ferengi   :image "HomeSystem/Tile-HS-Ferengi.gif" }
    { :id :firijii   :image "HomeSystem/Tile-HS-Firijii.gif" }
    { :id :geth   :image "HomeSystem/Tile-HS-Geth.gif" }
    { :id :gnolams   :image "HomeSystem/Tile-HS-Gnolams.gif" }
    { :id :goauld   :image "HomeSystem/Tile-HS-Goauld.gif" }
    { :id :hacan   :image "HomeSystem/Tile-HS-Hacan.gif" }
    { :id :harkonnen   :image "HomeSystem/Tile-HS-Harkonnen.gif" }
    { :id :hirogen   :image "HomeSystem/Tile-HS-Hirogen.gif" }
    { :id :humans   :image "HomeSystem/Tile-HS-Humans.gif" }
    { :id :imperium-of-man   :image "HomeSystem/Tile-HS-Imperium_of_Man.gif" }
    { :id :ironleague   :image "HomeSystem/Tile-HS-Ironleague.gif" }
    { :id :jipa   :image "HomeSystem/Tile-HS-Jipa.gif" }
    { :id :jolnar   :image "HomeSystem/Tile-HS-Jolnar.gif" }
    { :id :kazon   :image "HomeSystem/Tile-HS-Kazon.gif" }
    { :id :klackon   :image "HomeSystem/Tile-HS-Klackon.gif" }
    { :id :klingon   :image "HomeSystem/Tile-HS-Klingon.gif" }
    { :id :krenim   :image "HomeSystem/Tile-HS-Krenim.gif" }
    { :id :krogan   :image "HomeSystem/Tile-HS-Krogan.gif" }
    { :id :lisix   :image "HomeSystem/Tile-HS-L1z1x.gif" }
    { :id :lazax   :image "HomeSystem/Tile-HS-Lazax.gif" }
    { :id :letnev   :image "HomeSystem/Tile-HS-Letnev.gif" }
    { :id :lun   :image "HomeSystem/Tile-HS-Lun.gif" }
    { :id :mahact-sodality   :image "HomeSystem/Tile-HS-Mahact_Sodality.gif" }
    { :id :maquis   :image "HomeSystem/Tile-HS-Maquis.gif" }
    { :id :meklars   :image "HomeSystem/Tile-HS-Meklars.gif" }
    { :id :mentak   :image "HomeSystem/Tile-HS-Mentak.gif" }
    { :id :minbari   :image "HomeSystem/Tile-HS-Minbari.gif" }
    { :id :mirssen   :image "HomeSystem/Tile-HS-Mirssen.gif" }
    { :id :moritani   :image "HomeSystem/Tile-HS-Moritani.gif" }
    { :id :mrrshan   :image "HomeSystem/Tile-HS-Mrrshan.gif" }
    { :id :muaat   :image "HomeSystem/Tile-HS-Muaat.gif" }
    { :id :mystics   :image "HomeSystem/Tile-HS-Mystics.gif" }
    { :id :naalu   :image "HomeSystem/Tile-HS-Naalu.gif" }
    { :id :narn   :image "HomeSystem/Tile-HS-Narn.gif" }
    { :id :necron   :image "HomeSystem/Tile-HS-Necron.gif" }
    { :id :nekro   :image "HomeSystem/Tile-HS-Nekro.gif" }
    { :id :norr   :image "HomeSystem/Tile-HS-Norr.gif" }
    { :id :omohry   :image "HomeSystem/Tile-HS-Omohry.gif" }
    { :id :ori   :image "HomeSystem/Tile-HS-Ori.gif" }
    { :id :orion   :image "HomeSystem/Tile-HS-Orion.gif" }
    { :id :orks   :image "HomeSystem/Tile-HS-Orks.gif" }
    { :id :overmind   :image "HomeSystem/Tile-HS-Overmind.gif" }
    { :id :paradisian   :image "HomeSystem/Tile-HS-Paradisian.gif" }
    { :id :protoss   :image "HomeSystem/Tile-HS-Protoss.gif" }
    { :id :psilons   :image "HomeSystem/Tile-HS-Psilons.gif" }
    { :id :qikai   :image "HomeSystem/Tile-HS-Qikai.gif" }
    { :id :raiders   :image "HomeSystem/Tile-HS-Raiders.gif" }
    { :id :renegade   :image "HomeSystem/Tile-HS-Renegade.gif" }
    { :id :replicator   :image "HomeSystem/Tile-HS-Replicator.gif" }
    { :id :richese   :image "HomeSystem/Tile-HS-Richese.gif" }
    { :id :romulan   :image "HomeSystem/Tile-HS-Romulan.gif" }
    { :id :rrargan   :image "HomeSystem/Tile-HS-Rrargan.gif" }
    { :id :saar   :image "HomeSystem/Tile-HS-Saar.gif" :planets { :lisis2 { :res 1 :inf 0 } :ragh { :res 2 :inf 1 } } }
    { :id :sakkra   :image "HomeSystem/Tile-HS-Sakkra.gif" }
    { :id :salarian   :image "HomeSystem/Tile-HS-Salarian.gif" }
    { :id :shabbak   :image "HomeSystem/Tile-HS-Shabbak.gif" }
    { :id :shadows   :image "HomeSystem/Tile-HS-Shadows.gif" }
    { :id :silicoids   :image "HomeSystem/Tile-HS-Silicoids.gif" }
    { :id :sirkaan1   :image "HomeSystem/Tile-HS-Sirkaan_1.gif" }
    { :id :sirkaan2   :image "HomeSystem/Tile-HS-Sirkaan_2.gif" }
    { :id :slavers   :image "HomeSystem/Tile-HS-Slavers.gif" }
    { :id :sol   :image "HomeSystem/Tile-HS-Sol.gif" }
    { :id :sturmgard   :image "HomeSystem/Tile-HS-Sturmgard.gif" }
    { :id :swarm   :image "HomeSystem/Tile-HS-Swarm.gif" }
    { :id :systems-alliance   :image "HomeSystem/Tile-HS-SystemsAlliance.gif" }
    { :id :tau   :image "HomeSystem/Tile-HS-Tau.gif" }
    { :id :tauri   :image "HomeSystem/Tile-HS-Tauri.gif" }
    { :id :tellarite   :image "HomeSystem/Tile-HS-Tellarite.gif" }
    { :id :terran   :image "HomeSystem/Tile-HS-Terran.gif" }
    { :id :tlet   :image "HomeSystem/Tile-HS-Tlet.gif" }
    { :id :tokra   :image "HomeSystem/Tile-HS-Tokra.gif" }
    { :id :traelyn   :image "HomeSystem/Tile-HS-Traelyn.gif" }
    { :id :triacterial   :image "HomeSystem/Tile-HS-Triacterial.gif" }
    { :id :trilarians   :image "HomeSystem/Tile-HS-Trilarians.gif" }
    { :id :trill   :image "HomeSystem/Tile-HS-Trill.gif" }
    { :id :trillarians   :image "HomeSystem/Tile-HS-Trillarians.gif" }
    { :id :turian   :image "HomeSystem/Tile-HS-Turian.gif" }
    { :id :tyranid   :image "HomeSystem/Tile-HS-Tyranid.gif" }
    { :id :vernius   :image "HomeSystem/Tile-HS-Vernius.gif" }
    { :id :vorlon   :image "HomeSystem/Tile-HS-Vorlon.gif" }
    { :id :vulcan   :image "HomeSystem/Tile-HS-Vulcan.gif" }
    { :id :winnu   :image "HomeSystem/Tile-HS-Winnu.gif" }
    { :id :xel-naga   :image "HomeSystem/Tile-HS-XelNaga.gif" }
    { :id :xindi   :image "HomeSystem/Tile-HS-Xindi.gif" }
    { :id :xxcha   :image "HomeSystem/Tile-HS-Xxcha.gif" }
    { :id :yin   :image "HomeSystem/Tile-HS-Yin.gif" }
    { :id :yssaril   :image "HomeSystem/Tile-HS-Yssaril.gif" }
    { :id :zzedajin   :image "HomeSystem/Tile-HS-Zzedajin.gif" }

    { :id :empty :image "Special/Tile-Empty.gif" }
    { :id :galactic-storm :image "Special/Tile-Galactic_Storm.gif" }
    { :id :gravity-well :image "Special/Tile-Gravity_Well.gif" }
    { :id :gravity-rift :image "Special/Tile-Gravity_rift.gif" }
    { :id :ion-storm :image "Special/Tile-Ion_Storm.gif" }
    { :id :ancient-minefield :image "Special/Tile-Ancient_Minefield.gif" }
    { :id :asteroid-field :image "Special/Tile-Asteroid_Field.gif" }
    { :id :blackHole :image "Special/Tile-BlackHole.gif" }
    { :id :pulsar :image "Special/Tile-Pulsar.gif" }
    { :id :nebula, :type :nebula, :image "Special/Tile-Nebula.gif" }

    { :id :babylon5 :image "Special/Tile-Babylon_5.gif" }
    { :id :citadel :image "Special/Tile-Citadel.gif" }
    { :id :cormund :image "Special/Tile-Cormund.gif" }
    { :id :mecatol-rex :image "Special/Tile-Mecatol_Rex.gif" }
    { :id :mecatol-rex09 :image "Special/Tile-Mecatol_Rex09.gif" }
    { :id :muaat-supernova :image "Special/Tile-Muaat-Supernova.gif" }
    { :id :old-mecatol-rex :image "Special/Tile-OldMecatolRex.gif" }
    { :id :orion2 :image "Special/Tile-Orion2.gif" }
    { :id :quantum-singularity :image "Special/Tile-Quantum_Singularity.gif" }
    { :id :summer-palace :image "Special/Tile-Summer_Palace.gif" }
    { :id :Supernova :image "Special/Tile-Supernova.gif" }
    { :id :wormhole-a :image "Special/Tile-Wormhole_A.gif" }
    { :id :wormhole-b :image "Special/Tile-Wormhole_B.gif" }

    { :id :setup-dark-blue, :type :setup, :image "Setup/Tile-Setup-DarkBlue.gif" }
    { :id :setup-light-blue, :type :setup, :image "Setup/Tile-Setup-LightBlue.gif" }
    { :id :setup-medium-blue, :type :setup, :image "Setup/Tile-Setup-MediumBlue.gif" }
    { :id :setup-red, :type :setup, :image "Setup/Tile-Setup-Red.gif" }
    { :id :setup-yellow, :type :setup, :image "Setup/Tile-Setup-Yellow.gif" } ] )
    { :id :hs-back, :type :setup,   :image "HomeSystem/Tile-HS-Back.gif" }

(def all-systems (index-by-id all-systems-arr))

(defn random-system-id [] (:id (rand-nth all-systems-arr)))

(defn get-system [ id ] (all-systems id))

(defn get-planets [ system-id ] (get (get-system system-id) :planets {}))

(defn has-planet? [system-id planet] (contains? (get-planets system-id) planet))
