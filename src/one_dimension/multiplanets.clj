(ns one-dimension.multiplanets
    (:require [quil.core :as q]
              [quil.middleware :as m]))

(def G 0.0000474292)
(def h (bigdec 1))

(defn calc-position [p v] (+ p (* v h)))
(defn calc-velo [v a] (+ v (* a h)))
(defn calc-radius [x y] (Math/sqrt (+ (* x x) (* y y))))
(defn calc-speed [vx vy] (Math/sqrt (+ (* vx vx) (* vy vy))))
(defn calc-max-speed [s maxs] (max s maxs))

(def mercury {:y 0 :vx 0 :t 0 :m 0.553 :semi-ma 9.675 :ecc 0.2056 :type :planet})
(def venus   {:y 0 :vx 0 :t 0 :m 08.15 :semi-ma 18.075 :ecc 0.0068 :type :planet})
(def earth   {:y 0 :vx 0 :t 0 :m 10 :semi-ma 25 :ecc 0.0934 :type :planet})
(def mars    {:y 0 :vx 0 :t 0 :m 1.07 :semi-ma 38.1 :ecc 0.0167 :type :planet}) 
(def jupiter {:y 0 :vx 0 :t 0 :m 3178.3 :semi-ma 130.075 :ecc 0.0484 :type :planet})
(def saturn  {:y 0 :vx 0 :t 0 :m 951.59 :semi-ma 238.425 :ecc 0.0542 :type :planet})
(def uranus  {:y 0 :vx 0 :t 0 :m 145.36 :semi-ma 479.775 :ecc 0.0472 :type :planet})
(def neptune {:y 0 :vx 0 :t 0 :m 171.147 :semi-ma 751.725 :ecc 0.0472 :type :planet})
(def sun     {:x 0 :y 0 :vy 0 :vx 0 :t 0 :m 3330000 :semi-ma 0 :ecc 0 :type :sun})

(def start-system [mercury venus earth mars jupiter saturn uranus neptune sun])
(def planets [mercury venus earth mars jupiter saturn uranus neptune])

(defn init-planet [sun p] 
  (let [x (* (p :semi-ma) (+ 1 (p :ecc)))
        vy (Math/sqrt (* (* G (+ (sun :m) (p :m))) (- (/ 2 x) (/ 1 (p :semi-ma)))))]
    (assoc p :x x :vy vy :r x)))

(defn calc-force [other-m this-m r p] 
 (let [r (double r)] 
    (/ (* G other-m this-m p) (* r r r))))

(defn total-accel [this other]
  (let 
    [m  (this :m)
     x  (- (other :x) (this :x))
     y  (- (other :y) (this :y)) 
     r  (calc-radius x y)
     xf (calc-force (other :m) m r x)
     yf (calc-force (other :m) m r y)] 
    [(/ xf m) (/ yf m)]))

(defn calc-total-accel [this others] 
  (reduce #(vector (+ (first %) (first %2)) (+ (second %) (second %2))) 
          [0 0] 
          (for [other others] (total-accel this other))))

(defn update-planet [others planet] 
  (if (= (:type planet) :planet)
      (let [{x :x y :y t :t vx :vx vy :vy r :r m :m semi-ma :semi-ma ecc :ecc type :type} planet 
          t  (bigdec (+ t h)) 
          r  (calc-radius x y)
          a  (calc-total-accel planet others)
          vy (calc-velo vy (second a)) 
          vx (calc-velo vx (first a))
          y  (calc-position y vy) 
          x  (calc-position x vx)]
      {:x x :y y :t t :vx vx :vy vy :r r :m m :semi-ma semi-ma :ecc ecc :type type})
      planet))

(defn update-system [system]
    (map #(let [others (filter (fn [planet] (not= planet %)) system)] 
           (update-planet others %)) system))

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
  (q/background 147 147 147)
  ; Set star color.
  (q/fill 255 255 0)
  (q/ellipse (/ (q/width) 2) (/ (q/height) 2) 15 15)
  ; Move origin point to the center of the sketch.
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
   (doseq [planet (filter #(= (% :type) :planet) state)] 
         (let [x (planet :x) 
               y (planet :y)]
      (q/fill 0 0 255)
      (q/ellipse x y 7 7)))))

(q/defsketch euler
  :title "Solar System"
  :size [1600 900]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-system is called on each iteration before draw-state.
  :update update-system
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

