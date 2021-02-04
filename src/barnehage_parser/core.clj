(ns barnehage-parser.core
  (:gen-class)
  (:require [barnehage-parser.barnehage-details :as details]))

(defn -main
  [& args]
  (let
    [url "https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/hamna-barnehage/"]
    (details/get-details url)))
