(ns tic-tac-toe-pair.modal
  (:require [tic-tac-toe-pair.svg-icons :refer [feather-icon-x-svg]]))

(defn game-ending-modal [message modal-hidden? hide-modal-function 
                         start-new-game-function]
  [:div {:class (if (not modal-hidden?) "modal" "modal modal--hidden")}
   [:div {:class "modal__close-button-section"}
     [:button {:class "modal__close-button"
               :on-click hide-modal-function} [feather-icon-x-svg]]]
   [:div {:class "modal__message-section"}
     [:p {:class "modal__message"} message]]
   [:div {:class "modal__new-game-button-section"}
     [:button {:class "modal__new-game-button"
               :on-click start-new-game-function} "New Game"]]])
