(ns tic-tac-toe-pair.handlers-test
    (:require [cheshire.core :as json]
              [clojure.test :refer :all]
              [ring.mock.request :as mock]
              [tic-tac-toe-pair.api :refer [app]]))

(deftest app-test
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= 200 (:status response)))))
  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= 404 (:status response))))))

(deftest game-endpoint-test
  (testing "the /game endpoint"
    (testing "when provided with a valid JSON body"
      (let [player-info-map {:player-turn 1
                             :player-1-mark :x}
            player-info-json (json/encode player-info-map)
            response (app (-> (mock/request :post "/game" player-info-json)
                              (mock/content-type "application/json")))]
        (testing "returns a 200 status"
          (is (= 200 (:status response)))
        (testing "returns a valid game map"
          (let [expected-response-map {:current-token :player-1-token
                                       :player-1-token :x
                                       :player-2-token :o
                                       :board [nil nil nil nil nil nil nil  nil nil]
                                       :game-over false
                                       :message nil}
                expected-response-json (json/encode expected-response-map)]
            (is (= expected-response-json (:body response))))))))
        (testing "when provided with a valid JSON body and a player turn of 2nd"
          (let [player-info-map {:player-turn 2
                                  :player-1-mark :x}
                player-info-json (json/encode player-info-map)
                response (app (-> (mock/request :post "/game" player-info-json)
                                  (mock/content-type "application/json")))
                board-on-response (get (json/decode (:body response)) "board")]
            (testing "returns a 200 status"
              (is (= 200 (:status response))))
            (testing "board has a move from the ai"
              (is (= 1 (get (frequencies board-on-response) "o"))))))
    (testing "when provided with invalid JSON"
      (let [response (app (-> (mock/request :post "/game" "*@!")
                              (mock/content-type "application/json")))]
        (testing "returns a 400 status"
          (is (= 400 (:status response))))))))

(deftest move-endpoint-test
  (testing "the /move endpoint"
    (testing "records the player's move when provided with a valid JSON body"
      (let [move-map {:move {:location 0}
                      :game {:current-token :player-1-token
                             :player-1-token :x
                             :player-2-token :o
                             :board [nil nil nil nil nil nil nil nil nil]
                             :game-over nil
                             :message nil}}
            move-json (json/encode move-map)
            response  (app (-> (mock/request :post "/move" move-json)
                               (mock/content-type "application/json")))
            board-on-response  (get (json/decode (:body response)) "board")]
        (testing "returns a 200 status"
          (is (= 200 (:status response))))
        (testing "returns a board with the player mark on the right location"
          (is (= :x (keyword (first board-on-response)))))
        (testing "returns a board with an ai move if the game is not over"
          (is (= 1 (get (frequencies board-on-response) "o")))))))
    (testing "reports when the game is over"
      (let [move-map {:move {:location 0}
                      :game {:current-token :player-1-token
                              :player-1-token :x
                              :player-2-token :o
                              :board [nil :o nil :x :o nil :x nil nil]
                              :game-over nil
                              :message nil}}
            move-json (json/encode move-map)
            response  (app (-> (mock/request :post "/move" move-json)
                                (mock/content-type "application/json")))]
        (testing "returns a 200 status"
          (is (= 200 (:status response))))
        (testing "returns a board with game over true and a massage of who won the game"
          (let [expected-response-map {:current-token :player-2-token
                                        :player-1-token :x
                                        :player-2-token :o
                                        :board [:x :o nil :x :o nil :x nil nil]
                                        :game-over true
                                        :message "Congratulations! X won the game!"}
                expected-response-json (json/encode expected-response-map)]
                (is (= expected-response-json (:body response))))))))

(deftest tied-game-test
  (testing "move endpoint can handle tied game"
      (let [move-map {:move {:location 0}
                      :game {:current-token :player-1-token
                      :player-1-token :x
                      :player-2-token :o
                      :board [nil :o :o :o :x :x :x :x :o]
                      :game-over nil
                      :message nil}}
            move-json (json/encode move-map)
            response  (app (-> (mock/request :post "/move" move-json)
                        (mock/content-type "application/json")))
            board-on-response  (get (json/decode (:body response)) "board")]
        (testing "returns a 200 status"
          (is (= 200 (:status response))))
    )))

(deftest location-taken-test
  (testing "move endpoint can handle a location taken"
    (let [move-map {:move {:location 0}
                    :game {:current-token :player-1-token
                           :player-1-token :x
                           :player-2-token :o
                           :board [:x nil nil nil nil nil nil nil nil]
                           :game-over nil
                           :message nil}}
          move-json (json/encode move-map)
          response  (app (-> (mock/request :post "/move" move-json)
                             (mock/content-type "application/json")))
          message-on-response (get-in (json/decode (:body response)) ["body" "message"])]
      (testing "returns a 400 status"
        (is (= 400 (:status response))))
      (testing "message key on JSON response says Invalid"
        (is (= "Invalid move." message-on-response))))))
