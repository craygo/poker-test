(ns poker-test.generator
  (:require [clojure.math.combinatorics :refer [combinations]]
            [poker-test.core :refer [score]]
            [clojure.java.io :refer [writer]]
            [clojure.string :refer [join]]))

(def deck (for [v (conj (vec (range 2 10)) \T \J \Q \K \A)
                s [\S \D \H \C]]
            (str v s)))

(def hands (combinations deck 5))

; determine the frequency of the various hands from our score function
; seems to compare well against https://stattrek.com/poker/poker-probability.aspx?tutorial=prob
; given the difference that the ace here is never used as the lower card in a straight.
#_(->>
  hands
  (map score)
  (map :combi)
  frequencies
  time)

; generate a file with 2-player hands from the complete shuffled possible combinations in a deck
#_(let [random-hands (shuffle hands)]
    (with-open [w (writer "file:poker-hands-2.txt")]
      (doseq [p2-hand (partition 2 random-hands)]
        (.write w (str (join " " (apply concat p2-hand)) "\n")))))