(defproject glory-of-empires "0.1.2-SNAPSHOT"
  :description "Space strategy board game"
  :url "http://brotherus.net/fixme"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [  [org.clojure/clojure "1.9.0-alpha17"]
                   [ring "1.6.3"]
                 [hiccup "1.0.5"]
                 [org.clojars.rbrother/clojure-common "0.1.3"] ]
  :main ^:skip-aot glory-of-empires.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
