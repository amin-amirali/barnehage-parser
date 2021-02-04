(ns barnehage-parser.barnehage-details
  (:require [net.cgrand.enlive-html :as html]))

(defn- get-food-description
  [content]
  (-> (html/select content [:div.io-tile-preschool-prices])
      first
      :content
      last
      :content
      first))

(defn- get-food-price
  [content]
  (-> (html/select content [:div.io-tile-preschool-prices])
      first
      :content
      (nth 2)
      :content
      second
      :content
      first))

(defn- get-total-price
  [content]
  (->> (html/select content [:div.io-tile-preschool-prices])
       first
       :content
       second
       :content
       second
       :content
       first))

(defn- get-ratings-from-table
  [content]
  (let [keys (->> (html/select content [:table])
                  first
                  :content
                  (last)
                  :content
                  (map #(-> %
                            :content
                            first
                            :content
                            first
                            (clojure.string/replace #" " "_")
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
                    (map #(-> % second :content first)))]
    (zipmap keys values)))

(defn get-details
  [url]
  (let
    [content (html/html-resource (java.net.URL. url))
     keys-and-values (get-ratings-from-table content)
     total-price (get-total-price content)
     food-price (get-food-price content)
     food-description (get-food-description content)
     name (-> (html/select content [:h1.title]) first :content first)]
    (assoc keys-and-values :total_price total-price
                           :food_price food-price
                           :food_description food-description
                           :name name
                           :url url)))
