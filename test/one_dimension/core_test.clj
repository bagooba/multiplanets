(ns one-dimension.core-test
  (:require [clojure.test :refer :all]
            [one-dimension.core :refer :all]))

#_(deftest a-test
  (testing "update beginning state"
    (is (= {:r 3.02 :v 3.01 :t 1.01} (update {:r 1 :v 2 :t 1})))))

(def k3constant (/ (* Math/PI Math/PI 4) (* G M)))
(def period (* 2 (last (map :t (take-while check-max (iterate update-state initial-state))))))
(def radii (/ (+ (first (map :r (iterate update-state initial-state))) (last (map :r (take-while check-max (iterate update-state initial-state))))) 2))

(def check-kepler3-error (/ (- (* period period) (* k3constant radii radii radii)) (* period period) ))
(def check-kepler3-error2 (/ (- (* k3constant radii radii radii) (* period period)) (* k3constant radii radii radii)))

(def kepler-2 (map :k2 (take 50000 
                    (iterate update-state (let [x (init-p sa e)
                                                vy (init-v m m x sa)] 
                                            {:x x :y 0 :vx 0 :vy vy :t 0 :k2 (* x vy) :r x :s vy :maxs vy :mins vy})))))
(/ (reduce + kepler-2) 50000)

(defn calc-total-energy [state] 
  (map #(- (/ (* m (% :s) (% :s)) 2) (/ (* G M m) (% :r)))
       (take 500000 (iterate update-state state))))

(defn check-semiminor [state] 
  (false? (> 0 (state :vy))))
(def semiminor (last (map :y (take-while check-semiminor (iterate update-state initial-state)))))
(def origin-y 0)
(def origin-x (last (map :x (take-while check-semiminor (iterate update-state initial-state)))))
(def semimajor radii)
(defn check-k1-ellipse [state]
  (+ (/ (* (- (state :x) origin-x) (- (state :x) origin-x)) (* semimajor semimajor)) (/ (* (state :y) (state :y)) (* semiminor semiminor)))) 
(def check-k1-error (/ (- 1 (check-k1-ellipse (first (iterate update-state initial-state)))) 1))
