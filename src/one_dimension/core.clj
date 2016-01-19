(ns one-dimension.core)

(def G 1)
(def M 10)
(def m 1)
(def h (bigdec 40))
(def sa 250)
(def e 0.5)

(defn force [M m r p] 
  (let [r (double r)] 
    (- (/ (* G M m p) (* r r r)))))
(defn calc-accel [r p] (/ (force M m r p) m))
(defn calc-position [p v] (+ p (* v h)))
(defn calc-radii [x y] (Math/sqrt (+ (* x x) (* y y))))
(defn calc-velo [v a] (+ v (* a h)))
(defn init-v [M m r sa] (Math/sqrt (* (* G (+ M m)) (- (/ 2 r) (/ 1 sa)))))
(defn init-r [sa e] (* sa (+ 1 e)))

(defn update-state [state] 
  (let [{x :x y :y t :t vx :vx vy :vy} state 
         t (bigdec (+ t h) ) 
         r (calc-radii x y)
         ax (calc-accel r x) 
         ay (calc-accel r y ) 
         vy (calc-velo vy ay) 
         vx (calc-velo vx ax)
         y (calc-position y vy) 
         x (calc-position x vx)] 
    {:x x :y y :vx vx :vy vy :t t}))


;(take-while check-state (iterate update {:r 1 :v 2 :t 1}))
;(def results (take 5 (iterate update {:x r :y 0 :vx 0 :vy (initial-velo M r) :t 0})) )
;(def radii5 (map #(Math/sqrt (+ (* (% :x) (% :x)) (* (% :y) (% :y)))) results))
;(/ (reduce + radii5) 5)
