(ns one-dimension.core-test
  (:require [clojure.test :refer :all]
            [one-dimension.core :refer :all]))

(deftest a-test
  (testing "update beginning state"
    (is (= {:r 3.02 :v 3.01 :t 1.01} (update {:r 1 :v 2 :t 1})))))
