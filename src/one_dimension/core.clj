(ns one-dimension.core)

(def G 1)
(def M 1)
(def m 1)
(def h (bigdec 0.01 ))

(defn force [M m r] 
  (let [r (double r)] 
    (- (/ (* G M m) (* r r)))))
(defn calc-accel [r] (/ (force M m r) m))
(defn calc-position [r v] (+ r (* v h)))
(defn calc-velo [v a] (+ v (* a h)))

(defn check-state [{r :r}] 
  (pos? r))

(defn update [state] 
  (let [{r :r t :t v :v} state 
         t (bigdec (+ t h) ) 
         a (calc-accel r) 
         r (calc-position r v) 
         v (calc-velo v a)] 
    {:r r :v v :t t}))

(take-while check-state (iterate update {:r 1 :v 2 :t 1}))

(take 5 (iterate update {:r 1 :v 2 :t 1}))
