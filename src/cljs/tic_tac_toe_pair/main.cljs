(ns ^:figwheel-always tic-tac-toe-pair.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def localhost-address "http://localhost:3449")

(defonce app-state (atom {:text "Tic Tac Toe"
                          :player {
                            :player-turn nil
                            :player-1-mark nil
                          }
                          :game {}}))

(defn update-game [game]
  (swap! app-state #(assoc % :game game)))

(def post-move (fn [move-location]
  (go
    (let [request-body {:move {:location move-location}
                        :game (:game @app-state)}
          response     (<! (http/post (str localhost-address "/move")
                         {:with-credentials? false
                          :json-params request-body}))]
      (update-game (:body response))))))

(defn cell-button [cell-number]
  [:button {:class "board__cell-button" :on-click (fn [] (post-move cell-number))}])

(defn cell-mark [mark]
  [:div {:class "board__cell-mark"} 
    [:div mark]])

(defn board-cell [cell-number cell-value]
  (if (nil? cell-value)
    [cell-button cell-number]
    [cell-mark cell-value]))

(defn board [board-array]
    [:div {:class "board"}
      (doall
        (map-indexed 
          (fn [index item]
            ^{:key index}  
            [:div {:class "board__cell"} [board-cell index item]]) board-array))])
          
(def update-player-turn
  (fn [turn]
    (do
      (swap! app-state #(assoc-in % [:player :player-turn] turn)))))

(def update-player-mark
  (fn [mark]
    (do 
      (swap! app-state #(assoc-in % [:player :player-1-mark] mark)))))

(def post-player (fn []
    (go
      (let [request-body (:player @app-state)
            response     (<! (http/post (str localhost-address "/game")
                           {:with-credentials? false
                            :json-params request-body}))]
        (update-game (:body response))))))

(defn player-options-choose-mark []
  [:div {:class "card__section"}
   [:p "Choose a mark:"]
   [:div
     [:button {:class "card__menu-button" :on-click (fn [] (update-player-mark :x))} "x"]
     [:button {:class "card__menu-button" :on-click (fn [] (update-player-mark :o))} "o"]]])

(defn player-options-choose-turn []
  [:div {:class "card__section"}
   [:p "Choose your turn:"]
   [:button {:class "card__menu-button" :on-click (fn [] (update-player-turn 1))} "1"]
   [:button {:class "card__menu-button" :on-click (fn [] (update-player-turn 2))} "2"]])

(defn player-options-start-button []
  [:div
    [:button {:class "card__start-button" :on-click post-player} "START"]])

(defn player-options []
  [:div {:class "player-options-container"}
    [player-options-choose-mark]
    [player-options-choose-turn]
    [player-options-start-button]])

(defn game-ending-message []
  [:div {:class "message"} (get-in @app-state [:game :message])])

(defn header [text]
  [:header
    [:h1 (:text @app-state)]])

(defn tic-tac-toe []
  (let [game-over?           (get-in @app-state [:game :game-over])
        show-player-options? (empty? (:game @app-state))
        header-text          (:text @app-state)
        board-state          (get-in @app-state [:game :board])]
  [:div
   [header header-text]
   [:div {:class "game-container"}
    [:div {:class "card"}
      (if show-player-options? 
        [player-options]
        [:div {:class "board-container"} [board board-state]])
      (when game-over? [game-ending-message])]]]))

(reagent/render-component [tic-tac-toe]
                          (. js/document (getElementById "app")))
