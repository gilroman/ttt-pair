(ns tic-tac-toe-pair.api
  (:require [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [tic-tac-toe-pair.routes :refer [api-routes]]
            [tic-tac-toe-pair.middleware :refer [wrap-500-catchall]]))

(def app
  (-> api-routes
      (wrap-defaults api-defaults)
      wrap-500-catchall))
