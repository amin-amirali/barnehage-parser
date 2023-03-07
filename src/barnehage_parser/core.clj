(ns barnehage-parser.core
  (:gen-class)
  (:require [barnehage-parser.barnehage-list :as barnehage-list]
            [barnehage-parser.barnehage-details :as details]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn write-csv [path row-data]
  (let [columns  details/sorted-keys
        headers (map name columns)
        rows (mapv #(mapv % columns) row-data)]
    (with-open [file (io/writer path)]
      (csv/write-csv file (cons headers rows)))))

(defn -main
  [& args]
  (let
    [all-urls (barnehage-list/get-barnehage-url-list)
     result (map details/get-details all-urls)]
    (write-csv "/tmp/results.csv" result)))
