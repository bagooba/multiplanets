(ns one-dimension.core)

(def h 1N)
(def scale 1E-9)
(def G (* 6.67E-11 scale))
(def M (* 1.98E30 scale))
(def m (* 5.97E24 scale))
(def r (* 149.6E9 scale))
(def e 0.0167)
(def sa (/ r (+ e 1)))

(defn force [M m r p] 
  (let [r (double r)] 
    (- (/ (* G M m p) (* r r r)))))
(defn calc-accel [r p] (/ (force M m r p) m))
(defn calc-position [p v] (+ p (* v h)))
(defn calc-radii [x y] (Math/sqrt (+ (* x x) (* y y))))
(defn calc-velo [v a] (+ v (* a h)))
(defn init-v [M m r sa] (Math/sqrt (* (* G (+ M m)) (- (/ 2 r) (/ 1 sa)))))
(defn init-p [sa e] (* sa (+ 1 e)))
(defn calc-kepler2 [x y vx vy] (* (Math/sqrt (+ (* x x) (* y y))) (Math/sqrt (+ (* vx vx) (* vy vy)))))
(defn calc-speed [vx vy] (Math/sqrt (+ (* vy vy) (* vx vx))))
(defn calc-min-speed [s cmis]  (min s cmis))
(defn calc-max-speed [s cmas]  (max s cmas))

(defn check-min [state]
  (false? (< (state :cmis) (state :s))))
(defn check-max [state]
  (false? (> (state :cmas) (state :s))))

(defn update-state [state] 
  (let [{x :x y :y t :t vx :vx vy :vy k2 :k2 s :s cmis :cmis cmas :cmas} state 
         t (bigdec (+ t h) ) 
         r (calc-radii x y)
         ax (calc-accel r x) 
         ay (calc-accel r y ) 
         vy (calc-velo vy ay) 
         vx (calc-velo vx ax)
         y (calc-position y vy) 
         x (calc-position x vx)
         k2 (calc-kepler2 x y vx vy)
         s (calc-speed vx vy)
         cmis (calc-min-speed s cmis)
         cmas (calc-max-speed s cmas)] 
    {:x x :y y :vx vx :vy vy :t t :k2 k2 :s s :cmis cmis :cmas cmas}))

(def current-map (let [x (init-p sa e) 
                       vy (init-v M m x sa)] 
                                            {:x x :y 0 :vx 0 :vy vy :t 0 :k2 (* x vy) :s vy :cmis vy :cmas vy}) )
;(take-while check-state (iterate update-state {:r 1 :v 2 :t 1}))
(def kepler-2 (map :k2 (take 5 
                    (iterate update-state (let [x (init-p sa e)
                                                vy (init-v M m x sa)] 
                                           {:x x :y 0 :vx 0 :vy vy :t 0 :k2 (* x vy) :s vy :cmis vy :cmas vy}) ))) )
(/ (reduce + kepler-2) 5)
filter
