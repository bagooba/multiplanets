(ns one-dimension.multiplanets-kepler3
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [one-dimension.multiplanets :as g]))

(defn check-max [state]
  (false? (< (state :y) 0)))

(defn period [planet] 
  (* 2 (last (map :t (take-while check-max (iterate (partial g/update-planet [g/sun]) (g/init-planet g/sun planet)))))))
(defn radii [planet] 
  (/ (+ (first (map :r (iterate (partial g/update-planet [g/sun]) (g/init-planet g/sun planet))))
        (last (map :r (take-while check-max (iterate (partial g/update-planet [g/sun]) (g/init-planet g/sun planet)))))) 2))

(defn draw-axes []
  (q/line [0 0] [0 (q/height)])
  (q/line [0 (q/height)] [(q/width) (q/height)]))

(defn setup []
  (q/frame-rate 20)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ;(q/color-mode :hsb)
  ; setup function returns initial state. It contains
 (let [x (map #(* % %) (map period g/planets)) 
       y (map #( * % % %) (map radii g/planets))] 
   {:T-squared x :r-cubed y}))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 0)
  ; Set star color.
  (q/stroke 255 255 0)
 (let [xs (state :T-squared) 
       ys (state :r-cubed)
       points (map vector xs ys)
       w (q/width)
       h (q/height)
       x-factor 1.8830121463018065E-6 #_(/ w (- (last xs) (first xs))) 
       y-factor ;0.001468849376840652 ;2.373256249970334E-4
      ;7.540767273070035E-6 
      (/ h (- (last ys) (first ys)))
       ]
    ; Move origin point to the center of the sketch.
     (draw-axes) 
     (q/fill 255 255 0)
       ; Draw the planet.
       (q/with-translation [0 h] ;[10 (- h 10)] [(/ w 2) (/ h 2)]
         (doseq [point (partition 2 1 points)] 
                (q/stroke 0 255 255)
                (q/stroke-weight 10)
                ;(q/point (* (first point) x-factor) (- (* (second point) y-factor)))
                (q/line (* (first (first point)) x-factor) (- (* (second (first point)) y-factor)) (* (first (second point)) x-factor) (- (* (second (second point)) y-factor))) 
                (q/no-stroke)) )))

(q/defsketch kepler
  :title "Orbiting Objects"
  :size [800 800]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update (fn [state] state)  
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
