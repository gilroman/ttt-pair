(ns tic-tac-toe-pair.player-options
  (:require [clojure.string :as string]))

(defn player-mark-button [player-1-mark mark inactive-button-class-name 
                          update-player-mark-function]
  (let [keywordized-mark (keyword (string/lower-case mark))
        active (= keywordized-mark player-1-mark)]
    [:button {:class (if active (str inactive-button-class-name " "
                                     inactive-button-class-name "--active") inactive-button-class-name)
              :on-click (fn [] (update-player-mark-function keywordized-mark))}
     mark]))

(defn player-options-choose-mark [player-1-mark update-player-mark-function]
  [:div {:class "card__section"}
   [:p {:class "menu-label"} "Choose your mark"]
   [:div
    [player-mark-button player-1-mark "X" "card__menu-button" 
     update-player-mark-function]
    [player-mark-button player-1-mark "O" "card__menu-button" 
     update-player-mark-function]]])

(defn player-turn-button [player-turn turn inactive-button-class-name 
                          update-player-turn-function]
  (let [active (= player-turn turn)]
    [:button {:class (if active (str inactive-button-class-name " " inactive-button-class-name "--active") inactive-button-class-name)
              :on-click (fn [] (update-player-turn-function turn))} (str turn)]))

(defn player-options-choose-turn [player-turn update-player-turn-function]
  [:div {:class "card__section"}
   [:p {:class "menu-label"} "Choose your turn"]
   [player-turn-button player-turn 1 "card__menu-button" 
    update-player-turn-function]
   [player-turn-button player-turn 2 "card__menu-button" 
    update-player-turn-function]])

(defn player-options-start-button [loading? 
                                   start-button-click-function 
                                   show-loader-function]
  [:div {:class "card__section card__section--centered"}
   [:button {:class (if (not loading?) "card__start-button"
                        "card__start-button card__start-button--active")
             :on-click (fn [] (start-button-click-function) 
                              (show-loader-function))} 
            (if (not loading?) "Start" "Loading")]])

(defn player-options [player-state loading? start-button-click-function 
                      show-loader-function update-player-mark-function update-player-turn-function]
  [:div {:class "player-options-container"}
   [player-options-choose-mark (:player-1-mark player-state)
    update-player-mark-function]
   [player-options-choose-turn (:player-turn player-state) 
    update-player-turn-function]
   [player-options-start-button loading? start-button-click-function 
    show-loader-function]])
