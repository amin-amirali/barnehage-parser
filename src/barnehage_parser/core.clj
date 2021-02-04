(ns barnehage-parser.core
  (:require [net.cgrand.enlive-html :as html]))

(defn -main
  [& args]
  (let
    [url "https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/rosenborg-barnehage/"
     content (html/html-resource (java.net.URL. url))
     keys (->> (html/select content [:table])
               first
               :content
               (last)
               :content
               (map #(-> %
                         :content
                         first
                         :content
                         first
                         (clojure.string/replace #" " "")
                         (clojure.string/replace #"-" "_")
                         (clojure.string/replace #"ø" "o")
                         (clojure.string/lower-case)
                         keyword)))
     values (->> (html/select content [:table])
                 first
                 :content
                 (last)
                 :content
                 (map #(-> % :content))
                 (map #(-> % second :content first)))
     keys-and-values (zipmap keys values)
     total-price (->> (html/select content [:div.io-tile-preschool-prices])
                      first
                      :content
                      second
                      :content
                      second
                      :content
                      first)
     food-price (-> (html/select content [:div.io-tile-preschool-prices])
                    first
                    :content
                    (nth 2)
                    :content
                    second
                    :content
                    first)
     food-description (-> (html/select content [:div.io-tile-preschool-prices])
                          first
                          :content
                          last
                          :content
                          first)
     name (-> (html/select content [:h1.title]) first :content first)
     res (assoc keys-and-values :total_price total-price
                                :food_price food-price
                                :food_description food-description
                                :name name
                                :url url)]
    res))