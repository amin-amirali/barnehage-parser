(defproject barnehage-parser "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [enlive "1.1.6"]                           ; webscraper
                 [org.clojure/data.csv "1.0.0"]]
  :main ^:skip-aot barnehage-parser.core
  :aot [barnehage-parser.core]
  :profiles {:uberjar {:uberjar-name "barnehage-parser.jar"}})
