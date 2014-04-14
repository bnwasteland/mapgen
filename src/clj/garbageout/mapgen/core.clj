(ns garbageout.mapgen.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn repl-host-page []
  "
<!doctype html>
<html lang='en'>
<head>
    <meta charset='utf-8'>
    <title>Simple CLJS</title>
</head>
<body>
    <div id='app'/>
    <script src='http://fb.me/react-0.9.0.js'></script>
    <script src='js/out/goog/base.js' type='text/javascript'></script>
    <!-- pointing to cljsbuild generated js file -->
    <script src='js/mapgen.js'></script>
    <script type='text/javascript'>goog.require('garbageout.mapgen.app');</script>
</body>
</html>
")

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
  (GET "/" [] (repl-host-page))
  (route/resources "/")
  (route/not-found "Page not found"))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site #'app-routes))
