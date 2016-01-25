(ns one-dimension.multiplanets
    (:require [quil.core :as q]
            [quil.middleware :as m]))
(def G 1)
(def h (bigdec 10))

(defn calc-position [p v] (+ p (* v h)))
(defn calc-velo [v a] (+ v (* a h)))
(defn calc-radii [x y] (Math/sqrt (+ (* x x) (* y y))))

(def earth {:y 0 :vx 0 :t 0 :m 70 :semi-ma 100 :ecc 0 :type :planet})
(def mars {:y 0 :vx 0 :t 0 :m 7 :semi-ma 340 :ecc 0 :type :planet}) 
(def sun {:x 0 :y 0 :vy 0 :vx 0 :t 0 :m 700 :semi-ma 0 :ecc 0 :type :sun})
(def start-system [earth mars sun])

(defn init-planet [sun p] 
  (let [x (* (p :semi-ma) (+ 1 (p :ecc)))]
    (assoc p :x x
             :vy (Math/sqrt (* (* G (+ (sun :m) (p :m))) (- (/ 2 x) (/ 1 (p :semi-ma))))))))


(defn calc-force [other-m this-m r p] 
 (let [r (double r)] 
    (/ (* G other-m this-m p) (* r r r))))
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

(defn update-planet [planet others] 
  (if (= (:type planet) :planet)
      (let [{x :x y :y t :t vx :vx vy :vy m :m semi-ma :semi-ma ecc :ecc type :type} planet 
          t (bigdec (+ t h)) 
          r (calc-radii x y)
          a (calc-total-accel planet others)
          vy (calc-velo vy (second a)) 
          vx (calc-velo vx (first a))
          y (calc-position y vy) 
          x (calc-position x vx)]
      {:x x :y y :t t :vx vx :vy vy :m m :semi-ma semi-ma :ecc ecc :type type})
      planet))

(defn update-system [system]
    (map #(let [others (filter (fn [planet] (not= planet %)) system)] 
            (update-planet % others)) system))
(defn setup-system [] 
  (let [sun (first (filter #(= (% :type) :sun) start-system))
        planets (filter #(not= (% :type) :sun) start-system)
        init-fn (partial init-planet sun)
        new-planets (map init-fn planets)]
    (conj new-planets sun)) )

(defn setup [] 
  (q/frame-rate 30)
  (setup-system))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 0)
  ; Set star color.
  (q/fill 255 255 0)
  (q/ellipse (/ (q/width) 2) (/ (q/height) 2) 70 70)
  ; Move origin point to the center of the sketch.
      (q/with-translation [(/ (q/width) 2)
                           (/ (q/height) 2)]
  (doseq [planet (filter #(= (% :type) :planet) state)] 
         (let [x (planet :x) 
               y (planet :y)]
      (q/fill 0 0 255)
      (q/ellipse x y 30 30)))))

(q/defsketch euler
  :title "Solar System"
  :size [800 800]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-system
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
