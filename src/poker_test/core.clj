(ns poker-test.core
  (:require [clojure.java.io :refer [reader]]
            [clojure.string :refer [split]])
  (:gen-class))

(defn card-value [card]
  (case (first card)
    \A 14
    \K 13
    \Q 12
    \J 11
    \T 10
    (Integer/parseInt (str (first card)))))
#_(map card-value '("3S" "3C" "1C" "TC" "JS" "QH" "KH" "AH"))

(defn card-values [hand]
  (sort > (map card-value hand)))

(defn straight? [hand]
  (->>
    hand
    card-values
    (reduce (fn [acc x]
              (if (or (nil? (:last acc))
                      (= 1 (- (:last acc) x)))
                (-> acc (assoc :last x))
                (reduced {:descending-order false})))
            {:last nil :descending-order true})
    :descending-order))

(defn suit-values [hand]
  (set (map second hand)))

(defn flush? [hand]
  (= 1 (count (suit-values hand))))

(defn straight-flush? [hand]
  (and (straight? hand) (flush? hand)))

(defn card-values-count
  "returns sorted (low to high) list of the count of unique card values"
  [hand]
  (sort (map second (frequencies (card-values hand)))))
#_(card-values-count ["2S" "2H" "4S" "5S" "8S"])

(defn four-of-a-kind? [hand]
  (= '(1 4) (card-values-count hand)))

(defn full-house? [hand]
  (= '(2 3) (card-values-count hand)))

(defn three-of-a-kind? [hand]
  (= '(1 1 3) (card-values-count hand)))

(defn two-pairs? [hand]
  (= '(1 2 2) (card-values-count hand)))

(defn pair? [hand]
  (= '(1 1 1 2) (card-values-count hand)))

(def combination-score {:straight-flush  9
                        :four-of-a-kind  8
                        :full-house      7
                        :flush           6
                        :straight        5
                        :three-of-a-kind 4
                        :two-pairs       3
                        :pair            2
                        :highest-card    1})

(defn combination [hand]
  (cond
    (straight-flush? hand) :straight-flush
    (four-of-a-kind? hand) :four-of-a-kind
    (full-house? hand) :full-house
    (flush? hand) :flush
    (straight? hand) :straight
    (three-of-a-kind? hand) :three-of-a-kind
    (two-pairs? hand) :two-pairs
    (pair? hand) :pair
    true :highest-card))

(defn score
  "return map of ranked hands with :combi and :values"
  [hand]
  ;(println "hand " hand)
  {:combi  (combination hand)
   :values (card-values hand)})

(defn winner-index
  "given the collection of ranked hands for n players
   return index of winner or -1 for draw"
  [ranked-hands]
  (let [res (->> ranked-hands
                 (map (fn [ranked-hand]    ; create single vectors of the combination score followed by the card values (high to low)
                        (vec (cons (get combination-score (:combi ranked-hand)) (:values ranked-hand))) ))
                 (map-indexed (fn [i x] [i x]))
                 (sort-by second)
                 reverse)
        is-draw (= (second (first res)) (second (second res))) ; players have same combination and card values
        winning-idx (ffirst res)]
    (if is-draw
      -1
      winning-idx)))

(defn winning-player [acc hands]
  (let [ranked-hands (->> (split hands #" ")
                          (partition 5)
                          (map score))
        idx-winner (winner-index ranked-hands)]
    (if (= -1 idx-winner)
      (update acc :draws inc)
      (if (= 0 idx-winner)
       (update acc :player-1 inc)
       (update acc :player-2 inc)))))

(defn display [m]
  (println (format "Player 1: %d hands" (:player-1 m)))
  (println (format "Player 2: %d hands" (:player-2 m)))
  (if (pos? (:draws m))
    (println (format "Draws: %d hands" (:draws m)))))

(defn -main [& args]
  (let [f (first args)]
    (with-open [rdr (reader (or f *in*))]
      (->>
        rdr
        line-seq
        (reduce winning-player {:player-1 0 :player-2 0 :draws 0})
        display
        ))
    ))

#_(time (-main "./clojurecodingexercise/poker-hands.txt"))
#_(time (-main "./poker-hands-2.txt"))
