(ns ^:figwheel-always tic-tac-toe-pair.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [tic-tac-toe-pair.header :refer [header]]
            [tic-tac-toe-pair.board-cljs :refer [board]]
            [tic-tac-toe-pair.modal :refer [game-ending-modal]]
            [tic-tac-toe-pair.player-options :refer [player-options]]))

(enable-console-print!)

(def localhost-address "http://localhost:3449")

(def initial-game-state {:text "Tic Tac Toe"
                         :player {:player-turn nil
                                  :player-1-mark nil}
                         :game {}
                         :modal-hidden false
                         :loading false})

(defonce app-state (atom initial-game-state))

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
          
(defn update-player-turn [turn]
      (swap! app-state #(assoc-in % [:player :player-turn] turn)))

(defn update-player-mark [mark]
      (swap! app-state #(assoc-in % [:player :player-1-mark] mark)))

(def post-player (fn []
    (go
      (let [request-body (:player @app-state)
            response     (<! (http/post (str localhost-address "/game")
                           {:with-credentials? false
                            :json-params request-body}))]
        (update-game (:body response))))))

(defn show-loader []
    (swap! app-state assoc :loading? true))

(defn hide-modal []
  (swap! app-state assoc :modal-hidden true))

(defn start-new-game [] (swap! app-state (fn [] initial-game-state)))


(defn tic-tac-toe []
  (let [game-over?           (get-in @app-state [:game :game-over])
        show-player-options? (empty? (:game @app-state))
        header-text          (:text @app-state)
        board-state          (get-in @app-state [:game :board])
        player-state         (:player @app-state)
        modal-message        (get-in @app-state [:game :message])
        modal-hidden?        (:modal-hidden @app-state)
        loading?             (:loading? @app-state)]
  [:div
   [header header-text]
   [:div {:class "game-container"}
     [:div {:class "card"}
       (if show-player-options? 
         [player-options player-state loading? post-player show-loader 
          update-player-mark update-player-turn]
         [:div {:class "board-container"} 
          [board board-state post-move] 
          (when game-over? [game-ending-modal modal-message modal-hidden? 
                            hide-modal start-new-game])])]]]))

(reagent/render-component [tic-tac-toe]
                          (. js/document (getElementById "app")))
