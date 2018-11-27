(ns tic-tac-toe-pair.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as ring-json]
            [tic-tac-toe-pair.handlers :refer :all]))

(def json-routes
    (routes
        (POST "/game" [] (ring-json/wrap-json-body game-handler))
        (POST "/move" [] (ring-json/wrap-json-body move-handler))))


(defroutes non-body-routes
  (GET "/" [] "Tic Tac Toe")
  (route/not-found "Not Found"))

(def api-routes
  (routes json-routes non-body-routes))
