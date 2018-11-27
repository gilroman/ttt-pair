(ns tic-tac-toe-pair.handlers
  (:require [clojure.walk :as walk]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as ring-response]
            [tic-tac-toe-pair.game :refer
             [add-game-over-and-message-keys-to-game-map get-api-move initialize-game]]
            [tic-tac-toe-pair.rules :refer [is-move-valid?]]))

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
          board (:board keywordized-game)
          move-valid? (is-move-valid? board location)]
      
      (cond
        (not (contains? [0 1 2 3 4 5 6 7 8 9] location)) (-> (ring-response/response "")
                                                             (ring-response/status 500))
        (not move-valid?) (-> (ring-response/response {})
                              (ring-response/status 400))
        :else             
          (let [game-with-player-move    
                      (add-game-over-and-message-keys-to-game-map
                            (get-api-move keywordized-game location))
                computer-turn?  (= :player-2-token (:current-token
                                                          game-with-player-move))]
          (if (and computer-turn? (not (:game-over game-with-player-move)))
            (let [game-with-ai-move (get-api-move game-with-player-move)]
              (-> (ring-response/response
                   (add-game-over-and-message-keys-to-game-map game-with-ai-move))))
            (-> (ring-response/response game-with-player-move)))))))))

(def game-handler (ring-json/wrap-json-response
  (fn [request]
    (let [request-body (:body request)
          player-1-mark (keyword (get request-body "player-1-mark"))
          player-turn (get request-body "player-turn")
          initial-game (if (= 2 player-turn)
            (get-api-move (initialize-game player-turn player-1-mark))
            (initialize-game player-turn player-1-mark))]
      (-> (ring-response/response (add-game-over-and-message-keys-to-game-map initial-game)))))))
