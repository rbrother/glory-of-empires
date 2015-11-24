(defproject glory-of-empires "0.1.0-SNAPSHOT"
  :description "Space strategy board game"
  :url "http://brotherus.net/fixme"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [  [org.clojure/clojure "1.7.0"]
                   [ring "1.4.0"] ]
  :main ^:skip-aot glory-of-empires.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
