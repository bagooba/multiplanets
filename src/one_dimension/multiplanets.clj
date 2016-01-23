(ns one-dimension.multiplanets
    (:require [quil.core :as q]
            [quil.middleware :as m]
            [one-dimension.core :as g]))
(def G 1)

#_(defn create-planet [p] 
  ())

(defn calc-radii [x y] (Math/sqrt (+ (* x x) (* y y))))

(defn calc-force [other-m this-m r p] 
 (let [r (double r)] 
    (- (/ (* G other-m this-m p) (* r r r)))))

(defn total-accel [this other]
  (let 
    [m (this :m)
     x (- (other :x) (this :x))
     y (- (other :y) (this :y)) 
     r (calc-radii x y)
     xf (calc-force (other :m) m r x)
     yf (calc-force (other :m) m r y)] 
    [(/ xf m) (/ yf m)]))
(defn calc-total-accel [this others] 
  (reduce #(vector (+ (first %) (first %2)) (+ (second %) (second %2))) 
          [0 0] 
          (for [other others] (total-accel this other))))

#_(defn setup [] 
  (q/frame-rate 100)
  (let [p1 {}
        p2 {}
        cp1 (create-planet p1)
        cp2 (create-planet p2)] 
    [cp1 cp2]))


