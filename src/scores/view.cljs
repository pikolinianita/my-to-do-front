(ns scores.view
  (:require [cljs.pprint :refer [cl-format]]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [cljs.pprint :refer [pprint cl-format]]
            [re-frame.db :as rfdb]))

;http://html2hiccup.buttercloud.com/

(defn menu []
  [:aside.menu
   [:p.menu-label]
   [:ul.menu-list
    [:li
     [:a "Dashboard"]]
    [:li
     [:a "Customers"]]]
   [:p {:class "menu-label"}]
   [:ul {:class "menu-list"}
    [:li
     [:a "Team Settings"]]
    [:li
     [:a {:class "is-active"} "Manage Your Team"]
     [:ul
      [:li
       [:a "Members"]]
      [:li
       [:a "Plugins"]]
      [:li
       [:a "Add a member"]]]]
    [:li
     [:a "Invitations"]]
    [:li
     [:a "Cloud Storage Environment Settings"]]
    [:li
     [:a "Authentication"]]]
   [:p {:class "menu-label"}]
   [:ul {:class "menu-list"}
    [:li
     [:a "Payments"]]
    [:li
     [:a "Transfers"]]
    [:li
     [:a "Balance"]]]]
  )

(defn selector []
  [:div
   "Here will be dragons"
   
   [:button.button {:on-click (fn [_] (rf/dispatch [:futch "gazeta.pl"]))} "send"]
   [:pre (with-out-str (pprint @rfdb/app-db))]])

