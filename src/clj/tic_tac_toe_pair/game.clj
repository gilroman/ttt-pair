(ns tic-tac-toe-pair.game
  (:require [tic-tac-toe-pair.rules :refer [get-winning-token is-game-over? is-move-valid?]]
            [tic-tac-toe-pair.computer-player :refer [get-ai-move]]
            [tic-tac-toe-pair.board :refer [fill-location]]
            [tic-tac-toe-pair.console :refer
              [draw-intro draw-main get-move-location get-player-mark get-player-turn keyword-to-token build-congratulations-message build-losing-message]]))

(def default-game
  {:current-token nil
   :player-1-token nil
   :player-2-token nil
   :board [nil nil nil nil nil nil nil  nil nil]})

(defn set-current-token [turn game]
    (assoc game :current-token (if (= 1 turn) :player-1-token :player-2-token)))

(defn set-player-tokens [player-1-token game]
  (assoc game
    :player-1-token player-1-token
    :player-2-token (if (= :x player-1-token) :o :x)))

(defn initialize-game
  ([] (let [player-1-mark (get-player-mark default-game)
        player-turn (get-player-turn default-game)]
    (set-current-token player-turn
      (set-player-tokens player-1-mark default-game))))
  ([player-turn player-1-mark] (set-current-token player-turn
    (set-player-tokens player-1-mark default-game))))

(defn- update-current-player [game]
  (assoc game :current-token
    (if (= (:current-token game) :player-1-token)
      :player-2-token
      :player-1-token)))

(defn- get-current-token [game] ((:current-token game) game))

(defn- update-board [game location]
  (assoc game :board (fill-location (:board game) location (get-current-token game))))

(defn- update-board-and-player [game location]
  ((comp update-current-player update-board) game location))

(defn update-game [game location]
  (if (is-move-valid? (:board game) location)
    (update-board-and-player game location)
    game))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board game))]
    (cond
      (nil? winner) "This game ended in a tie!"
      (= (:player-1-token game) winner) (build-congratulations-message winner)
      :else (build-losing-message winner))))

(defmulti get-move :current-token)
(defmethod get-move :player-1-token [game]
  (get-move-location game))
(defmethod get-move :player-2-token [game]
  (get-ai-move game))

(defmulti get-api-move :current-token)
(defmethod get-api-move :player-1-token [game location]
  (update-game game location))
(defmethod get-api-move :player-2-token [game]
  (update-game game (get-ai-move game)))


(defn play [game]
  (loop [history  [game]]
    (let [game (last history)]
      (if (is-game-over? (:board game))
        (do
          (draw-main game (get-game-end-message game))
          history)
        (let [new-game (update-game game (get-move game))]
         (recur (conj history new-game)))))))


(defn add-game-over-and-message-keys-to-game-map
  ([game] (let [game-over (is-game-over? (:board game))
                game-with-game-over-key (assoc game :game-over game-over)] 
            (add-game-over-and-message-keys-to-game-map 
               game-with-game-over-key (if game-over
                      (get-game-end-message game)
                      nil))))
  ([game message]
     (assoc game :message message)))
