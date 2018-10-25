(ns tic-tac-toe-pair.game-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.game :refer :all]
            [tic-tac-toe-pair.rules :refer [is-game-over? get-winning-token]]))

(deftest initialize-game-test
  (testing "returns a default game map with player-1-token set to :x,  player-2-token set to :o and current-token set to player-1-token if the player chooses X as their mark and 1 as their player turn")
    (with-out-str (is (= {:current-token :player-1-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil nil nil nil nil nil]}
      (with-in-str "X\n1\n" (initialize-game)))))
  (testing "returns a default game map with player-1-token set to :x,  player-2-token set to :o and current-token set to player-1-token if the player chooses X as their mark and 1 as their player turn")
    (with-out-str (is (= {:current-token :player-1-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil nil nil nil nil nil]}
      (with-in-str "X\n1\n" (initialize-game))))))

(deftest update-game-test
  (testing "returns an updated game map if valid move input")
    (with-out-str (is (= {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil :x nil nil nil nil]}
      (update-game {:current-token :player-1-token
                    :player-1-token :x
                    :player-2-token :o
                    :board [nil nil nil nil nil nil nil nil nil]} 4))))
  (testing "returns an identical game map if move input is invalid")
    (is (= {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x :o nil nil :x nil nil nil nil]}
      (update-game
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil nil :x nil nil nil nil]}
         4))))

(deftest get-game-end-message-test
  (testing "it returns an appropriate message if player 1 wins")
    (is (= "Congratulations! X won the game!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))
  (testing "it returns an appropriate message if player 2 wins")
    (is (= "Sorry! O won the game!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil :o nil :x :o nil :x :o nil]})))
  (testing "it returns a draw message if there is no winner")
    (is (= "This game ended in a tie!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :x :o :o :o :x :x :o :x]}))))

(deftest play-test
  (testing "it returns a history where the last game contains a board where the game is over")
    (with-out-str (is (= {:current-token :player-2-token
                          :player-1-token :x
                          :player-2-token :o
                          :board [:x :x :o :o :o :x :x :o :x]} 
      (last (play {:current-token :player-2-token
                   :player-1-token :x
                   :player-2-token :o
                   :board [:x :x :o :o :o :x :x :o :x]})))))
  (testing "it returns a game history where the last game contains a board that  is won by the computer")
    (with-out-str (is (= {:current-token :player-1-token
                          :player-1-token :x
                          :player-2-token :o
                          :board [:x :x :o :o :o :x :o :o :x]}
      (last (play {:current-token :player-2-token
               :player-1-token :x
               :player-2-token :o
               :board [:x :x :o :o :o :x nil :o :x]})))))
  (testing "it returns a game history where the last game constains a board that is won by the computer")
  (with-out-str (is (= {:current-token :player-1-token
                        :player-1-token :x
                        :player-2-token :o
                        :board [:x :x :o :o :o :x :o :o nil]}
    (last (play {:current-token :player-2-token
             :player-1-token :x
             :player-2-token :o
             :board [:x :x :o :o :o :x nil :o nil]}))))))

(deftest set-player-tokens-test 
  (testing "it sets the game :player-1-token to :x if given :x")
    (is (= :x 
      (:player-1-token (set-player-tokens :x {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-2-token to :o if given :x")
    (is (= :o 
      (:player-2-token (set-player-tokens :x {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-1-token to :o if given :o")
    (is (= :o 
      (:player-1-token (set-player-tokens :o {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-2-token to :x if given :o")
      (is (= :x 
        (:player-2-token (set-player-tokens :o {:current-token :player-2-token
                                                :player-1-token :x
                                                :player-2-token :o
                                                :board [nil nil nil nil nil nil nil nil nil]})))))

(deftest set-current-token-test 
  (testing "it returns a game with :current-token set to :player-1-token if given the number 1")
  (is (= {:current-token :player-1-token
    :player-1-token nil
    :player-2-token nil
    :board [nil nil nil nil nil nil nil  nil nil]} 
    (set-current-token 1 {:current-token nil
      :player-1-token nil
      :player-2-token nil
      :board [nil nil nil nil nil nil nil  nil nil]})))
  (testing "it returns a game with :current-token set to :player-1-token if given the number 2")
  (is (= {:current-token :player-2-token
    :player-1-token nil
    :player-2-token nil
    :board [nil nil nil nil nil nil nil  nil nil]} 
    (set-current-token 2 {:current-token nil
      :player-1-token nil
      :player-2-token nil
      :board [nil nil nil nil nil nil nil  nil nil]}))))