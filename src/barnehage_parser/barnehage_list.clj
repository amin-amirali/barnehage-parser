(ns barnehage-parser.barnehage-list
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(def barnehage-front-url "https://www.oslo.kommune.no/xmlhttprequest.php?category=2772&rootCategory=2772&template=25&service=filterList.render&offset=")
(def num-pages 26)
(def num-results-per-curl-call 27)

(defn get-urls-from-page [page-url]
  (let [res (client/post page-url)
        parsed-res (json/read-str (:body res) :key-fn keyword)]
   (map #(get-in % [:url]) (get-in parsed-res [:data :items]))))

(defn get-barnehage-url-list []
  (let [_ (println "gathering urls")
        list-offsets (map #(* num-results-per-curl-call %) (range num-pages))
        url-list-to-curl (map #(str barnehage-front-url %) list-offsets)
        all-urls (map #(get-urls-from-page %) url-list-to-curl)]
    (flatten all-urls)))
