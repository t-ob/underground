(ns underground.playground)

;; (defn neighbours* [m n [i j]]
;;   (vec (filter (fn [[x y]]
;;                  (and (<= 0 x (- m 1))
;;                       (<= 0 y (- n 1))))
;;                (list [(+ i 1) j]
;;                      [i (- j 1)]
;;                      [(- i 1) j]
;;                      [i (+ j 1)]))))

;; (defn graph [m n]
;;   (into {}
;;         (let [neighbours (partial neighbours* m n)]
;;           (for [i (range m) j (range n)]
;;             (vector [i j]
;;                     (neighbours [i j]))))))

;; (defn incident-edges [graph [i j]]
;;   (get-in graph
;;           [:data [i j]]))

;; (incident-edges (graph 4 5) [3 4])

;; (defn random-edge [tree])

;; (defn remove-cell [walls cell]
;;   (vec (remove #{cell} walls)))

;; (defn maze-iter [graph walls maze]
;;   (let [cell (rand-nth walls)]
;;     (if (get maze cell)
;;       maze
;;       (vector walls cell (incident-edges graph cell)))))

;; Remove a wall edge, add to cells

;; A common notation used to describe these automata is referred to as "S/B", which is known as its rule (or rulestring). S (for survival) is a list of all the numbers of ON cells that cause an ON cell to remain ON. B (for birth) is a list of all the numbers of ON cells that cause an OFF cell to turn on. If 0 is in the list, then blank regions of the universe will turn on in one generation. S/B is often referred to as the rule or rulestring of the given cellular automata.

;; Mazecetric B3/S1234

;; (defn maze-world
;;   [max-width max-height init-width init-height p]
;;   (with-meta
;;     (vec (for [i (range max-height)]
;;            (vec (for [j (range max-width)]
;;                   (if (and (<= (- (/ max-width 2) (/ init-width 2)) j (dec (+ (/ max-width 2) (/ init-width 2))))
;;                            (<= (- (/ max-height 2) (/ init-height 2)) i (dec (+ (/ max-height 2) (/ init-height 2))))
;;                            (< (rand) p))
;;                     1
;;                     0)))))
;;     {:width  max-width
;;      :height max-height}))

;; (defn str->nums [numeric-string]
;;   (into #{}
;;         (map (fn [s] (- (int s) (int \0)))
;;              numeric-string)))

;; (defn rule [s]
;;   (let [[r c] (map (partial apply str)
;;                    (split-at 1 s))]
;;     (condp = r
;;       "b" [:birth (str->nums c)]
;;       "s" [:survive (str->nums c)])))

;; (defn get-in-universe [universe [i j]]
;;   (nth (nth universe j) i))

;; (defn neighbours [universe [i j]]
;;   (let [{:keys [width height]} (meta universe)]
;;     (for [s (map (partial + i) [-1 0 1])
;;           t (map (partial + j) [-1 0 1])
;;           :when (and (not= [s t] [i j])
;;                      (<= 0 s (dec width))
;;                      (<= 0 t (dec height)))]
;;       (get-in-universe universe [s t]))))

;; (defn automata [& {:keys [birth survive]}]
;;   (fn [universe]
;;     (let [{:keys [width height]} (meta universe)]
;;       (with-meta
;;         (vec (for [j (range height)]
;;                (vec (for [i (range width)]
;;                       (let [cell (get-in-universe universe [i j])
;;                             live-neighbours (apply + (neighbours universe [i j]))]
;;                         (if (= 0 cell)
;;                           (if (birth live-neighbours)
;;                             1 0)
;;                           (if (survive live-neighbours)
;;                             1 0)))))))
;;         {:width  width
;;          :height height}))))

;; (defn automata-from-str [rule-string]
;;   (let [{:keys [birth survive]} (into {}
;;                                       (map rule
;;                                            (str/split (str/lower-case rule-string)
;;                                                       #"/")))]
;;     (automata :birth birth
;;               :survive survive)))

;; (def maze
;;   (automata-from-str "b3/s1234"))

;; (def block "██")

;; (defn format-n [n]
;;   (if (= 0 n)
;;     block
;;     "  "))

;; (defn render-universe! [universe]
;;   (let [{:keys [width height]} (meta universe)]
;;     (println (apply str (repeat (+ 2 width) block)))
;;     (doseq [row universe]
;;       (println (str block (apply str (map format-n row)) block)))
;;     (println (apply str (repeat (+ 2 width) block))))
;;   (println ""))

;; (def gol
;;   (automata-from-str "b3/s23"))

;; (def tob-gol-blinker
;;   (with-meta
;;     [[0 0 0 0 0]
;;      [0 0 0 0 0]
;;      [0 1 1 1 0]
;;      [0 0 0 0 0]
;;      [0 0 0 0 0]]
;;     {:width 5
;;      :height 5}))

;; #_(loop [[state & future-state] (iterate maze (maze-world 50 25 10 10 0.5))]
;;   (render-universe! state)
;;   (Thread/sleep 1000)
;;   (recur future-state))

;; #_(loop [[state & future-state] (iterate gol tob-gol-blinker)]
;;   (render-universe! state)
;;   (Thread/sleep 1000)
;;   (recur future-state))

;; #_(let [maze-states (take 10 (iterate maze (maze-world 50 25 10 10 0.5)))]
;;   (render-universe! (last maze-states)))

;; (defn generate-maze [iterations width height]
;;   (last (take iterations
;;               (iterate (automata-from-str "b3/s12345") (maze-world width height (/ width 5) (/ height 5) 0.5)))))

;; #_(loop [[state & future-state] (iterate (automata-from-str "b3/s1234")
;;                                        (maze-world 80 40 10 10 0.5))]
;;   (render-universe! state)
;;   (Thread/sleep 1000)
;;   (recur (drop 9 future-state)))

;; #_(loop [[state & future-state] (iterate (automata-from-str "b3/s23")
;;                                        (maze-world 80 40 30 30 0.5))]
;;   (render-universe! state)
;;   (Thread/sleep 1000)
;;   (recur (drop 9 future-state)))
