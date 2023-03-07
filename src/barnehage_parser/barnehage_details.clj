(ns barnehage-parser.barnehage-details
  (:require [net.cgrand.enlive-html :as html]))

(def sorted-keys [:name
                  :address
                  :url
                  :ages
                  :total_price
                  :food_price
                  :food_description
                  :barnets_utvikling
                  :barnets_trivsel
                  :total_tilfredshet
                  :area_per_kid
                  :kids_count
                  :ute__og_innemiljo
                  :informasjon
                  :svarprosent
                  :kids_per_employee
                  :pct_employees_with_degree
                  :vacation_weeks
                  :opening_hours])

(defn- get-vacation-weeks
  [content]
  (if-let [vacation-text (->> (html/select content [:div.io-tile-common.io-tile-vacations :p])
                              (map html/text)
                              first)]
    (re-find (re-matcher #"uke [\d\d[, | og ]]*" vacation-text))))

(defn- get-opening-hours
  [content]
  (if-let [schedule-text (->> (html/select content [:div.io-openinghours__content :ul :li])
                              (map html/text)
                              first)]

    (re-find (re-matcher #"0.*" schedule-text))))

(defn- get-key-info
  [content]
  (let [key-info-str (->> (html/select content [:div.io-tile-preschool-information :ul :li])
                          (map html/text)
                          (clojure.string/join "\n"))
        kids-count (-> (re-find (re-matcher #"Antall barn: ([\d|,]*)" key-info-str))
                       second)
        area-per-kid (-> (re-find (re-matcher #"Inneareal: ([\d|,]*)" key-info-str))
                         second)
        kids-per-employee (-> (re-find (re-matcher #"Antall barn per ansatt: ([\d|,]*)" key-info-str))
                              second)
        pct-employees-degreed (-> (re-find (re-matcher #"barnehagelærerutdanning: ([\d|,]*)" key-info-str))
                                  second)
        ages (-> (re-find (re-matcher #"\n(.*år.*)" key-info-str))
                 second)]
    {:ages ages
     :kids_count kids-count
     :area_per_kid area-per-kid
     :kids_per_employee kids-per-employee
     :pct_employees_with_degree pct-employees-degreed}))

(defn- get-address
  [content]
  (let [address (->> (html/select content [:div.osg-contactbox__content :div.osg-contactbox__value])
                     (map html/text)
                     last
                     (clojure.string/trim)
                     )
        parsed-address (re-find (re-matcher #"(.*)\n(.*)" address))]
    (str (clojure.string/trim (nth parsed-address 1))
         " "
         (clojure.string/trim (nth parsed-address 2)))))

(defn- get-food-description
  [content]
  (-> (html/select content [:div.io-tile-preschool-prices :span])
      first
      html/text))

(defn- get-food-price
  [content]
  (-> (html/select
        content
        [:div.io-tile-preschool-prices :strong])
      second
      html/text))

(defn- get-total-price
  [content]
  (-> (html/select
        content
        [:div.io-tile-preschool-prices :p :strong])
      first
      html/text))

(defn- get-ratings-from-table
  [content]
  (let [keys (->> (html/select content [:div.io-tile-survey :div :table :tbody :tr :th])
                  (map #(-> %
                            html/text
                            (clojure.string/replace #" " "_")
                            (clojure.string/replace #"-" "_")
                            (clojure.string/replace #"__" "_")
                            (clojure.string/replace #"ø" "o")
                            (clojure.string/lower-case)
                            keyword)))
        values (->> (html/select content [:div.io-tile-survey :div :table :tbody :tr :td])
                    (map html/text))
        values-filtered (map #(nth values %) (filter even? (range (count values))))]
    (zipmap keys values-filtered)))

(defn- get-name
  [content]
  (->> (html/select content [:h1.io-text-preset-1])
       first
       html/text))

(defn get-details
  [url]
  (let [_ (println (str "url: " url))
        content (html/html-resource (java.net.URL. url))]
    (-> (get-ratings-from-table content)
        (merge (get-key-info content))
        (assoc :total_price (get-total-price content)
               :food_price (get-food-price content)
               :food_description (get-food-description content)
               :name (get-name content)
               :url url
               :address (get-address content)
               :opening_hours (get-opening-hours content)))))
