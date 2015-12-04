(ns glory-of-empires.systems)

(def all-systems
  [ { :id :acheron :image "1planet/Tile-Acheron.gif" }
    { :id :aeon :image "1planet/Tile-Aeon.gif" }
    { :id :aker :image "1planet/Tile-Aker.gif" }
    { :id :ammit :image "1planet/Tile-Ammit.gif" }
    { :id :amun :image "1planet/Tile-Amun.gif" }
    { :image "1planet/Tile-Andjety.gif" }
    { :image "1planet/Tile-Anhur.gif" }
    { :image "1planet/Tile-Ankh.gif" }
    { :image "1planet/Tile-Anuket.gif" }
    { :image "1planet/Tile-Apis.gif" }
    { :image "1planet/Tile-Asgard.gif" }
    { :image "1planet/Tile-Asgard_III.gif" }
    { :image "1planet/Tile-Astennu.gif" }
    { :image "1planet/Tile-Aten.gif" }
    { :image "1planet/Tile-Babi.gif" }
    { :image "1planet/Tile-Bakha.gif" }
    { :image "1planet/Tile-Bast.gif" }
    { :image "1planet/Tile-Beriyil.gif" }
    { :image "1planet/Tile-Bes.gif" }
    { :image "1planet/Tile-Capha.gif" }
    { :image "1planet/Tile-Chensit.gif" }
    { :image "1planet/Tile-Chnum.gif" }
    { :image "1planet/Tile-Chuuka.gif" }
    { :image "1planet/Tile-Cicerus.gif" }
    { :image "1planet/Tile-Coruscant.gif" }
    { :image "1planet/Tile-Dedun.gif" }
    { :image "1planet/Tile-Deimo.gif" }
    { :image "1planet/Tile-Discworld.gif" }
    { :image "1planet/Tile-Dune.gif" }
    { :image "1planet/Tile-Elnath.gif" }
    { :image "1planet/Tile-Everra.gif" }
    { :image "1planet/Tile-Faunus.gif" }
    { :image "1planet/Tile-Fiorina.gif" }
    { :image "1planet/Tile-FloydIV.gif" }
    { :image "1planet/Tile-Garbozia.gif" }
    { :image "1planet/Tile-Heimat.gif" }
    { :image "1planet/Tile-Hopes_End.gif" }
    { :image "1planet/Tile-Inaak.gif" }
    { :image "1planet/Tile-Industrex.gif" }
    { :image "1planet/Tile-Iskra.gif" }
    { :image "1planet/Tile-Ithaki.gif" }
    { :image "1planet/Tile-Kanite.gif" }
    { :image "1planet/Tile-Kauket.gif" }
    { :image "1planet/Tile-Kazenoeki.gif" }
    { :image "1planet/Tile-Khepri.gif" }
    { :image "1planet/Tile-Khnum.gif" }
    { :image "1planet/Tile-Klendathu.gif" }
    { :image "1planet/Tile-Kobol.gif" }
    { :image "1planet/Tile-Laurin.gif" }
    { :image "1planet/Tile-Lesab.gif" }
    { :image "1planet/Tile-Lodor.gif" }
    { :image "1planet/Tile-Lv426.gif" }
    { :image "1planet/Tile-Medusa_V.gif" }
    { :image "1planet/Tile-Mehar_Xull.gif" }
    { :image "1planet/Tile-Mirage.gif" }
    { :image "1planet/Tile-Myrkr.gif" }
    { :image "1planet/Tile-Nanan.gif" }
    { :image "1planet/Tile-Natthar.gif" }
    { :image "1planet/Tile-Nef.gif" }
    { :image "1planet/Tile-Nexus.gif" }
    { :image "1planet/Tile-Niiwa-Sei.gif" }
    { :image "1planet/Tile-Pakhet.gif" }
    { :image "1planet/Tile-Parzifal.gif" }
    { :image "1planet/Tile-Perimeter.gif" }
    { :image "1planet/Tile-Petbe.gif" }
    { :image "1planet/Tile-Primor.gif" }
    { :image "1planet/Tile-Ptah.gif" }
    { :image "1planet/Tile-Qetesh.gif" }
    { :image "1planet/Tile-Quann.gif" }
    { :image "1planet/Tile-Radon.gif" }
    { :image "1planet/Tile-Saudor.gif" }
    { :image "1planet/Tile-Sem-Lore.gif" }
    { :image "1planet/Tile-Shai.gif" }
    { :image "1planet/Tile-Shool.gif" }
    { :image "1planet/Tile-Solitude.gif" }
    { :image "1planet/Tile-Sulako.gif" }
    { :image "1planet/Tile-Suuth.gif" }
    { :image "1planet/Tile-Tarmann.gif" }
    { :image "1planet/Tile-Tefnut.gif" }
    { :image "1planet/Tile-Tempesta.gif" }
    { :image "1planet/Tile-Tenenit.gif" }
    { :image "1planet/Tile-Theom.gif" }
    { :image "1planet/Tile-Thibah.gif" }
    { :image "1planet/Tile-Ubuntu.gif" }
    { :image "1planet/Tile-Vefut_II.gif" }
    { :image "1planet/Tile-Wadjet.gif" }
    { :image "1planet/Tile-Wellon.gif" }
    { :image "1planet/Tile-Wepwawet.gif" }
    { :image "2planet/Tile-Abyz-Fria.gif" }
    { :image "2planet/Tile-Achill.gif" }
    { :image "2planet/Tile-AeternaTammuz.gif" }
    { :image "2planet/Tile-Arinam-Meer.gif" }
    { :image "2planet/Tile-Arnor-Lor.gif" }
    { :image "2planet/Tile-Aztlan-Senhora.gif" }
    { :image "2planet/Tile-Bereg-Lirta_IV.gif" }
    { :image "2planet/Tile-Bondar-MacLean.gif" }
    { :image "2planet/Tile-Cato.gif" }
    { :image "2planet/Tile-Centauri-Gral.gif" }
    { :image "2planet/Tile-Coorneeq-Resculon.gif" }
    { :image "2planet/Tile-Custos-Xhal.gif" }
    { :image "2planet/Tile-Dal_Bootha-Xxehan.gif" }
    { :image "2planet/Tile-Garneau-Hadfield.gif" }
    { :image "2planet/Tile-Gilea-Xerel_Wu.gif" }
    { :image "2planet/Tile-Gurmee-Uguza.gif" }
    { :image "2planet/Tile-Hana.gif" }
    { :image "2planet/Tile-Hercalor-Tiamat.gif" }
    { :image "2planet/Tile-Kazaros-Mirabar.gif" }
    { :image "2planet/Tile-Lazar-Sakulag.gif" }
    { :image "2planet/Tile-Lisis-Velnor.gif" }
    { :image "2planet/Tile-MachallSorkragh.gif" }
    { :image "2planet/Tile-Masada-Jamiah.gif" }
    { :image "2planet/Tile-Massada-Iuddaea.gif" }
    { :image "2planet/Tile-Mellon-Zohbat.gif" }
    { :image "2planet/Tile-Nadir-Lucus.gif" }
    { :image "2planet/Tile-New_Albion-Starpoint.gif" }
    { :image "2planet/Tile-Nummantia-Hisspania.gif" }
    { :image "2planet/Tile-OthorAiel.gif" }
    { :image "2planet/Tile-Pankow-Prenzlberg.gif" }
    { :image "2planet/Tile-Perro-Senno.gif" }
    { :image "2planet/Tile-Qucenn-Rarron.gif" }
    { :image "2planet/Tile-Renenutet-Osiris.gif" }
    { :image "2planet/Tile-Renpet-Tawaret.gif" }
    { :image "2planet/Tile-Resheph-Ogdoad.gif" }
    { :image "2planet/Tile-SalemIogu.gif" }
    { :image "2planet/Tile-Shiva-Wishnu.gif" }
    { :image "2planet/Tile-SullaMartinez.gif" }
    { :image "2planet/Tile-Sumerian-Arcturus.gif" }
    { :image "2planet/Tile-Temujin-Avanti.gif" }
    { :image "2planet/Tile-Tequran-Torkan.gif" }
    { :image "2planet/Tile-Thoth-Mafdet.gif" }
    { :image "2planet/Tile-Tsion-Bellatrix.gif" }
    { :image "2planet/Tile-Vega.gif" }
    { :image "2planet/Tile-Verne-Quebec.gif" }
    { :image "2planet/Tile-Vortekai-Deguzz.gif" }
    { :image "Special/Tile-3-Ashtroth-Loki-Abaddon.gif" }
    { :image "Special/Tile-3-Rigel.gif" }
    { :image "Special/Tile-Ancient_Minefield.gif" }
    { :image "Special/Tile-Ashtroth-Loki-Abaddon.gif" }
    { :image "Special/Tile-Asteroid_Field.gif" }
    { :image "Special/Tile-Asteroid_Field2.gif" }
    { :image "Special/Tile-Asteroid_Nexus.gif" }
    { :image "Special/Tile-Babylon_5.gif" }
    { :image "Special/Tile-BlackHole.gif" }
    { :image "Special/Tile-China.gif" }
    { :image "Special/Tile-Citadel.gif" }
    { :image "Special/Tile-Cormund.gif" }
    { :image "Special/Tile-Elder-Card.gif" }
    { :image "Special/Tile-Elder-Uhuru-Amani.gif" }
    { :image "Special/Tile-Empty.gif" }
    { :image "Special/Tile-Galactic_Storm.gif" }
    { :image "Special/Tile-Gravity Rift.gif" }
    { :image "Special/Tile-Gravity_Well.gif" }
    { :image "Special/Tile-Gravity_rift.gif" }
    { :image "Special/Tile-Ion_Storm.gif" }
    { :image "Special/Tile-Ion_Storm2.gif" }
    { :image "Special/Tile-Mecatol_Rex.gif" }
    { :image "Special/Tile-Mecatol_Rex09.gif" }
    { :image "Special/Tile-Muaat-Supernova.gif" }
    { :image "Special/Tile-Nebula.gif" }
    { :image "Special/Tile-Nebula_Nexus.gif" }
    { :image "Special/Tile-OldMecatolRex.gif" }
    { :image "Special/Tile-Orion2.gif" }
    { :image "Special/Tile-Pulsar.gif" }
    { :image "Special/Tile-Quantum_Singularity.gif" }
    { :image "Special/Tile-Rigel.gif" }
    { :image "Special/Tile-Summer_Palace.gif" }
    { :image "Special/Tile-Supernova.gif" }
    { :image "Special/Tile-Tianshang-Tiangu-Changtian.gif" }
    { :image "Special/Tile-Wormhole_A.gif" }
    { :image "Special/Tile-Wormhole_B.gif" }
    { :id :setup-dark-blue :image "Setup/Tile-Setup-DarkBlue.gif" }
    { :image "Setup/Tile-Setup-LightBlue.gif" }
    { :image "Setup/Tile-Setup-MediumBlue.gif" }
    { :image "Setup/Tile-Setup-Red.gif" }
    { :image "Setup/Tile-Setup-Yellow.gif" } ] )

; TODO: make this hash-map instead (use index)
(defn get-system [ id ]
  (first (filter #(= (:id %) id) all-systems)))
