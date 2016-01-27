(ns one-dimension.multiplanets-test
  (:require [clojure.test :refer :all]
            [one-dimension.multiplanets :refer :all]))

(defn check-max [state]
  (false? (< (state :y) 0)))

(def k3constant (/ (* Math/PI Math/PI 4) (* G (sun :m))))
(defn period [] (* 2 (last (map :t (take-while check-max (iterate (partial update-planet [sun]) (init-planet sun earth)))))))
(defn radii [] (/ (+ (first (map :r (iterate (partial update-planet [sun]) (init-planet sun earth))))
                 (last (map :r (take-while check-max (iterate (partial update-planet [sun]) (init-planet sun earth)))))) 2))

(defn check-kepler3-error [] (/ (- (* (period) (period)) (* k3constant (radii) (radii) (radii))) (* (period) (period)) ))
(defn check-kepler3-error2 [] (/ (- (* k3constant (radii) (radii) (radii)) (* (period) (period))) (* k3constant (radii) (radii) (radii))))
