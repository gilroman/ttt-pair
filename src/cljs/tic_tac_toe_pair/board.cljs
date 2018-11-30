(ns tic-tac-toe-pair.board-cljs)

(defn cell-button [cell-number cell-click-function]
  [:button {:class "board__cell-button" 
            :on-click (fn [] (cell-click-function cell-number))}])

(defn cell-mark [mark]
  [:div {:class "board__cell-mark"}
   [:div mark]])

(defn board-cell [cell-number cell-value cell-click-function]
  (if (nil? cell-value)
    [cell-button cell-number cell-click-function]
    [cell-mark cell-value]))

(defn board [board-array cell-click-function]
  [:div {:class "board"}
   (doall
    (map-indexed
     (fn [index item]
       ^{:key index}
       [:div {:class "board__cell"} [board-cell index item cell-click-function]]) board-array))])
