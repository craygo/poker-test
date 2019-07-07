(ns poker-test.core-test
  (:require [clojure.test :refer :all]
            [poker-test.core :refer :all]))

(deftest straight-flush-test
  (is (= :straight-flush (combination ["2S" "3S" "4S" "5S" "6S"])))
  (is (= :straight-flush (combination ["TS" "JS" "QS" "KS" "AS"]))))

(deftest flush-test
  (is (= :flush (combination ["2S" "3S" "4S" "5S" "7S"]))))

(deftest straight-test
  (is (= :straight (combination ["2S" "3S" "4S" "5C" "6S"])))
  (is (= :straight (combination ["TS" "JS" "QS" "KC" "AS"]))))

(deftest four-of-a-kind-test
  (is (= :four-of-a-kind (combination ["2S" "2H" "2C" "2D" "6S"]))))

(deftest full-house-test
  (is (= :full-house (combination ["2S" "2H" "2C" "6D" "6S"]))))

(deftest three-of-a-kind-test
  (is (= :three-of-a-kind (combination ["2S" "2H" "2C" "5D" "6S"]))))

(deftest two-pairs-test
  (is (= :two-pairs (combination ["2S" "2H" "5C" "5D" "6S"]))))

(deftest pair-test
  (is (= :pair (combination ["2S" "2H" "5C" "7D" "6S"]))))

(deftest highest-card-test
  (is (= :highest-card (combination ["AS" "3S" "4S" "5C" "6S"]))))

(deftest winner-index-test
  (is (= -1 (winner-index [(score ["AS" "3S" "4S" "5C" "6S"])
                           (score ["AH" "3H" "4H" "5D" "6D"])])))
  (is (= 0 (winner-index [(score ["AS" "3S" "4S" "5C" "7S"])
                          (score ["AH" "3H" "4H" "5D" "6D"])])))
  (is (= 1 (winner-index [(score ["AS" "3S" "4S" "5C" "6S"])
                          (score ["AH" "3H" "4H" "5D" "7D"])])))
  )

#_(run-tests)