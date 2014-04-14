(ns garbageout.mapgen.voronoi.geom)

(defrecord Point2D [x y])
(defn point [x y] (Point2D. x y))

(def origin (point 0 0))

(defn distance [a b]
  (let [dx (- (:x b) (:x a))
        dy (- (:y b) (:y a))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn add [a b]
  (point (+ (:x a) (:x b)) (+ (:y b) (:y b))))

(defn sub [a b]
  (point (- (:x a) (:x b)) (- (:y b) (:y b))))

(defn div [pt scalar]
  (point (/ (:x pt) scalar) (/ (:y pt) scalar)))

(defn polar [pt]
  {:magnitude (distance origin pt)
   :angle (Math/atan2 (:y pt) (:x pt))})

(defrecord Circle2D [center radius])

(defn circle [center radius] (Circle2D. center radius))

(defn circle-contains? [circle point]
  (< (distance (:center circle) point)
     (:radius circle)))

(defn circumcircle [a b c]
  (let [{ax :x ay :y} a
        {bx :x by :y} b
        {cx :x cy :y} c
        A (- bx ax)
        B (- by ay)
        C (- cx ax)
        D (- cy ay)
        E (+ (* A (+ ax bx))
             (* B (+ ay by)))
        F (+ (* C (+ ax cx))
             (* D (+ ay cy)))
        G (* 2
             (- (* A (- cy by))
                (* B (- cx bx))))
        center-x (/ (- (* D E) (* B F))
                    G)
        center-y (/ (- (* A F) (* C E))
                    G)
        center (point center-x center-y)
        radius (distance center a)]
    (circle center radius)))

(defn edges-coincident? [a [b1 b2]]
  (or (= a [b1 b2])
      (= a [b2 b1])))

(defrecord Triangle2D [verts edges circumcircle])

(defn normalize-triangle-verts [center verts]
  (vec (sort-by (fn [vert] (:angle (polar (sub vert center)))) verts)))

(defn circumcircle-contains? [triangle point]
  (circle-contains? (:circumcircle triangle) point))

(defn triangle [a b c]
  (let [circle (circumcircle a b c)
        center (:center circle)
        normalized-verts (normalize-triangle-verts center [a b c])
        [A B C] normalized-verts
        edges [[A B] [B C] [C A]]]
    (Triangle2D. normalized-verts edges circle)))
