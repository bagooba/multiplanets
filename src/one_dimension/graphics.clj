(ns one-dimension.graphics
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [one-dimension.core :as g]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 1)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ;(q/color-mode :hsb)
  ; setup function returns initial state. It contains
 {:x g/r :y 0 :vx 0 :vy (g/initial-velo g/M g/r) :t 0})

(defn update-state [state]
   (g/update state))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 0)
  ; Set star color.
  (q/fill 255 255 0)
  (let [x (state :x) 
        y (state :y)]
(q/text (str "x: " x " :vx " (state :vx) " y: " y " vy: " (state :vy)) 20 20    )    
; Move origin point to the center of the sketch.
    (q/ellipse (/ (q/width) 2) (/ (q/height) 2) 70 70)
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ; Draw the planet.
      (q/fill 0 0 255)
      (q/ellipse x y 30 30))))

(q/defsketch euler
  :title "Orbiting Objects"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update g/update
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
