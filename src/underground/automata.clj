(ns underground.automata
  (:require [clojure.string :as str]))

(defn maze-universe
  "Create an initial maze universe, seeded in the middle with a grid of random ones and zeroes."
  [max-width max-height init-width init-height p]
  (with-meta
    (vec (for [i (range max-height)]
           (vec (for [j (range max-width)]
                  (if (and (<= (- (/ max-width 2) (/ init-width 2)) j (dec (+ (/ max-width 2) (/ init-width 2))))
                           (<= (- (/ max-height 2) (/ init-height 2)) i (dec (+ (/ max-height 2) (/ init-height 2))))
                           (< (rand) p))
                    1
                    0)))))
    {:width  max-width
     :height max-height}))

(defn str->nums
  "Create a set of numbers out of a numeric string."
  [numeric-string]
  (into #{}
        (map (fn [s] (- (int s) (int \0)))
             numeric-string)))

(defn rule
  "Create a cellular automata rule from strings of the form [sb]\\d+."
  [rule-string]
  (let [[rule conditions] (map (partial apply str)
                               (split-at 1 rule-string))]
    (condp = rule
      "b" [:birth (str->nums conditions)]
      "s" [:survive (str->nums conditions)])))

(defn get-in-universe
  "Extract the value in cell [i j]."
  [universe [i j]]
  (nth (nth universe j) i))

(defn neighbours
  [universe [i j]]
  (let [{:keys [width height]} (meta universe)]
    (for [s (map (partial + i) [-1 0 1])
          t (map (partial + j) [-1 0 1])
          :when (and (not= [s t] [i j])
                     (<= 0 s (dec width))
                     (<= 0 t (dec height)))]
      (get-in-universe universe [s t]))))

(defn automata
  "Create a cellular automata with the options
    :birth set-of-birth-values
    :survive set-of-survive-values"
  [& {:keys [birth survive]}]
  (fn [universe]
    (let [{:keys [width height]} (meta universe)]
      (with-meta
        (vec (for [j (range height)]
               (vec (for [i (range width)]
                      (let [cell (get-in-universe universe [i j])
                            live-neighbours (apply + (neighbours universe [i j]))]
                        (if (= 0 cell)
                          (if (birth live-neighbours)
                            1 0)
                          (if (survive live-neighbours)
                            1 0)))))))
        {:width  width
         :height height}))))

(defn automata-from-str
  "Create a cellular automata from a rule-string."
  [rule-string]
  (let [{:keys [birth survive]} (into {}
                                      (map rule
                                           (str/split (str/lower-case rule-string)
                                                      #"/")))]
    (automata :birth birth
              :survive survive)))

(def maze-1 (automata-from-str "b1/s1234"))
(def maze-2 (automata-from-str "b1/s12345"))

;; (def conways-game-of-life (automata-from-str "b3/s23"))

(def block "██")

(defn format-number [n]
  (if (= 0 n)
    block
    "  "))

(defn render-universe! [universe]
  (let [{:keys [width height]} (meta universe)]
    (println (apply str (repeat (+ 2 width) block)))
    (doseq [row universe]
      (println (str block (apply str (map format-number row)) block)))
    (println (apply str (repeat (+ 2 width) block))))
  (println ""))

#_(loop [[state & future-state] (iterate maze-1
                                       (maze-universe 40 40 10 10 0.5))]
  (render-universe! state)
  (Thread/sleep 1000)
  (recur future-state))


