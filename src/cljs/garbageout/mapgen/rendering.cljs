(ns garbageout.mapgen.rendering
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [garbageout.mapgen.voronoi.geom :as g]))


(defprotocol SVGRenderable
  (render-svg [this]))

(defprotocol TextRenderable
  (render-text [this]))

(defn svg [x]
  (render-svg x))

(defn text [x]
  (render-text x))

(extend-type om.core.MapCursor
  TextRenderable
    (render-text [cursor]
      (render-text (om/value cursor)))
  SVGRenderable
    (render-svg [cursor]
      (render-svg (om/value cursor))))

(extend-type garbageout.mapgen.voronoi.geom.Point2D
  TextRenderable
    (render-text [point]
      (str "(" (:x point) ", " (:y point) ")"))
  SVGRenderable
    (render-svg [point]
      (dom/circle #js {:cx (:x point)
                       :cy (:y point)
                       :r 2
                       :fill "blue"})))

(extend-type garbageout.mapgen.voronoi.geom.Circle2D
  TextRenderable
    (render-text [c]
      (str "circle [" (render-text (:center c)) ", radius " (:radius c) "]"))
  SVGRenderable
    (render-svg [c]
      (dom/circle #js {:cx (get-in c [:center :x])
                       :cy (get-in c [:center :y])
                       :r  (:radius c)
                       :fill "transparent"
                       :stroke "black"})))

(defn render-edge [[A B]]
  (dom/line #js {:x1 (:x A) :y1 (:y A)
                 :x2 (:x B) :y2 (:y B)
                 :stroke "green"
                 :strokeWidth 2}))

(extend-type garbageout.mapgen.voronoi.geom.Triangle2D
  TextRenderable
    (render-text [this] "triangle")
  SVGRenderable
    (render-svg [this]
      (apply dom/g #js {:className "triangle"}
        (render-svg (:circumcircle this))
        (map render-edge (:edges this)))))
