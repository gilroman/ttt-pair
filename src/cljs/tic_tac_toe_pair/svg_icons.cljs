(ns tic-tac-toe-pair.svg-icons)

(defn feather-icon-x-svg []
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :width "46"
         :height "46"
         :viewBox "0 0 24 24"
         :stroke-width "2"
         :stroke-linecap "round"
         :stroke-linejoin "round"}
   [:line {:x1 "18"
           :y1 "6"
           :x2 "6"
           :y2 "18"}]
   [:line {:x1 "6"
           :y1 "6"
           :x2 "18"
           :y2 "18"}]])
