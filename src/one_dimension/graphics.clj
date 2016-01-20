(ns one-dimension.graphics
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [one-dimension.core :as g]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ;(q/color-mode :hsb)
  ; setup function returns initial state. It contains
 (let [x (g/init-r g/sa g/e)
       vy (g/init-v g/M g/m x g/sa) ] 
   {:x x :y 0 :vx 0 :vy vy :t 0 :k2 (* x vy)}))


(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 0)
  ; Set star color.
  (q/fill 255 255 0)
  (let [x (state :x) 
        y (state :y)]
    ; Move origin point to the center of the sketch.
    (q/ellipse (/ (q/width) 2) (/ (q/height) 2) 70 70)
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ; Draw the planet.
      (q/fill 0 0 255)
      (q/ellipse x y 30 30))
    
      (q/fill 255 255 0)  
      (q/text (str "x:  " x) 5 20)
      (q/text (str "vx: " (state :vx)) 5 30)
      (q/text (str "y:  " y) 5 40)
      (q/text (str "vy: " (state :vy)) 5 50)))

(q/defsketch euler
  :title "Orbiting Objects"
  :size [800 800]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update g/update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
