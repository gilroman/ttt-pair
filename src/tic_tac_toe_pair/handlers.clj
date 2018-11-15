(ns tic-tac-toe-pair.handlers
  (:require [clojure.walk :as walk]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as ring-response]
            [tic-tac-toe-pair.game :refer
              [add-game-over-and-message-keys-to-game-map get-api-move initialize-game]]))

(defn- keywordize-game-vals [game]
  (assoc game :current-token (keyword (:current-token game))
              :player-1-token (keyword (:player-1-token game))
              :player-2-token (keyword (:player-2-token game))
              :board (into [] (map (fn [element] (keyword element)) (:board game)))))

(def move-handler (ring-json/wrap-json-response
  (fn [request]
    (let [request-body (:body request)
          game (get request-body "game")
          keywordized-game (keywordize-game-vals (walk/keywordize-keys game))
          location (get-in request-body ["move" "location"])
          game-with-player-move (get-api-move keywordized-game location)
          computer-turn? (= :player-2-token (:current-token
            game-with-player-move))]
      (if computer-turn?
        (let [game-with-ai-move (get-api-move game-with-player-move)]
          (-> (ring-response/response
               (add-game-over-and-message-keys-to-game-map game-with-ai-move))))
          (-> (ring-response/response
               (add-game-over-and-message-keys-to-game-map game-with-player-move))))))))

(def game-handler (ring-json/wrap-json-response
  (fn [request]
    (let [request-body (:body request)
          player-1-mark (keyword (get request-body "player-1-mark"))
          player-turn (get request-body "player-turn")]
      (-> (ring-response/response (add-game-over-and-message-keys-to-game-map (initialize-game player-turn player-1-mark))))))))
