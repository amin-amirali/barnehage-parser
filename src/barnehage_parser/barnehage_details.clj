(ns barnehage-parser.barnehage-details
  (:require [net.cgrand.enlive-html :as html]))

(defn- get-key-info
  [content]
  (let [all-key-info (->> (html/select content [:div.io-tile-common.io-tile-preschool.io-tile-preschool-information
                                                :div.io-tile-content
                                                :ul :li])
                          (map html/text))
        selected-key-info (mapv
                            (vec all-key-info)
                            [0 5 6 7 8 9])
        keys [:barnehage_type :ages :kids_count :area_per_kid :kids_per_employee :pct_employees_with_degree]
        keys-and-vals (zipmap keys selected-key-info)
        clean-kids-count (update keys-and-vals :kids_count #(clojure.string/replace % #"Antall barn: " ""))
        clean-area-kids (update clean-kids-count :area_per_kid #(re-find (re-matcher #"\d,\d+" %)))
        clean-kids-employee (update clean-area-kids :kids_per_employee #(re-find (re-matcher #"\d,\d+" %)))
        clean-pct-employees (update clean-kids-employee :pct_employees_with_degree #(re-find (re-matcher #"\d\d,\d+" %)))]
    clean-pct-employees))

(defn- get-address
  [content]
  (->> (html/select content [:div.contact-info :p :span])
       (map html/text)
       (clojure.string/join " ")))

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
     address (get-address content)
     name (-> (html/select content [:h1.title]) first :content first)
     key-info-map (get-key-info content)]
    (-> keys-and-values
        (assoc :total_price total-price
               :food_price food-price
               :food_description food-description
               :name name
               :url url
               :address address)
        (merge key-info-map))))
