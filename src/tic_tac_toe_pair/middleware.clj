(ns tic-tac-toe-pair.middleware
  (:require [ring.middleware.json :as ring-json]
            [ring.util.response :as ring-response]))

(defn wrap-500-catchall [handler]
    (fn [request]
        (try (handler request)
        (catch Exception e
        (-> (ring-response/response (.getMessage e))
            (ring-response/status 500)
            (ring-response/content-type "application/json")
            (ring-response/charset "utf-8"))))))
