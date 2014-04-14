(defproject garbageout.mapgen "0.1.0-SNAPSHOT"
  :description "A simple browser app for building/visualizing voronoi graphs and transforming them into terrain models"
  :url "http://github.com/bnwasteland/mapgen"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2197"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.1.6"]
                 [om "0.5.3"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :profiles {:dev {:dependencies [[ring-serve "0.1.2"]]}}

  :cljsbuild {
    :builds [{:source-paths ["src/cljs"]
              :compiler {:output-to "resources/public/js/mapgen.js"
                         :output-dir "resources/public/js/out"
                         :optimizations :none
                         ;:source-map true
                         :pretty-print true}}]})
