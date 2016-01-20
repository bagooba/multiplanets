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
(defn calc-kepler2 [x y vx vy] (* (Math/sqrt (+ (* x x) (* y y))) (Math/sqrt (+ (* vx vx) (* vy vy)))))

(defn update-state [state] 
  (let [{x :x y :y t :t vx :vx vy :vy} state 
         t (bigdec (+ t h) ) 
         r (calc-radii x y)
         ax (calc-accel r x) 
         ay (calc-accel r y ) 
         vy (calc-velo vy ay) 
         vx (calc-velo vx ax)
         y (calc-position y vy) 
         x (calc-position x vx)
         k2 (calc-kepler2 x y vx vy)] 
    {:x x :y y :vx vx :vy vy :t t :k2 k2}))


;(take-while check-state (iterate update {:r 1 :v 2 :t 1}))
;(def results (take 5 (iterate update {:x r :y 0 :vx 0 :vy (initial-velo M r) :t 0})) )
;(def radii5 (map #(Math/sqrt (+ (* (% :x) (% :x)) (* (% :y) (% :y)))) results))
;(/ (reduce + radii5) 5)
(def kepler-2 (map :k2 (take 5 
                    (iterate update-state (let [x (init-p sa e)
                                                vy (init-v M m x sa)] 
                                            {:x x :y 0 :vx 0 :vy vy :t 0 :k2 (* x vy) :s vy :cmis vy :cmas vy}) ))) )
(/ (reduce + kepler-2) 5)

