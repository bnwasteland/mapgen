(ns garbageout.mapgen.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [garbageout.mapgen.voronoi.geom :as g]
            [garbageout.mapgen.rendering :as render]))


(defn random-point []
  (g/point (rand) (rand)))

(defn random-circle []
  (g/circle (random-point) (rand)))

(defn random-triangle []
  (g/triangle (random-point) (random-point) (random-point)))

(defn random-object []
  ((rand-nth [random-point random-circle random-triangle])))

(defn random-objects [] (repeatedly random-object))

(def app-state (atom {:text "Tesallate!"
                      :objects-to-render []}))

(defn svg-plot [objects owner]
  (om/component
   (dom/section nil
     (apply dom/svg #js {:width 500 :height 500}
       (dom/rect #js {:x 0 :y 0 :width 500 :height 500 :fill "silver"})
       (map render/svg objects)))))

(defn object-list [objects owner]
  (om/component
    (apply dom/ul nil
      (map #(dom/li nil (render/text %)) objects))))

(defn voronoi-app [app owner]
  (om/component
   (dom/section nil
     (dom/h2 nil (:text app))
     (om/build svg-plot (:objects-to-render app))
     (dom/button nil "Add Random Points")
     (om/build object-list (:objects-to-render app)))))

(om/root
  voronoi-app
  app-state
  {:target (. js/document (getElementById "app"))})

(swap! app-state assoc :objects-to-render (vec (take 5 (repeatedly random-triangle))))
