(ns todo.view
  (:require [cljs.pprint :refer [cl-format]]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
			[todo.reframe :as reframki]
			[todo.s-log :refer [log!]]
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

(defn welcome-screen []
(let [inp (reagent/atom {:name "" :placeholder "Write User Number"})]
	(fn [] 
	[:div
		[:h1 "Please enter user name"]
		[:input {:type :text
            :value (@inp :name)
			:name "user-name"
            :on-change (fn [e]
						(log! :i (-> e .-target .-value))
                         (swap! inp assoc :name (-> e .-target .-value)))}]
		[:button.button {:on-click (fn [_] (rf/dispatch [:try-login (@inp :name)]))} "Find User"]
        [:button.button {:on-click (fn [_] (rf/dispatch [:try-create (@inp :name)]))} "Create User - not work yet"]		
		]))
)

(defn draw-user [{:keys [name]}]
    (log! :i "Here is User: " name)
	[:ul.menu-list 
	[:li {:on-click (fn [_] (rf/dispatch [:active :user]))} [( if (= (first @(rf/subscribe [:active])) :user ) :a.is-active :a) name ]]]
)

(defn project-element [name id]
	(log! :i  "active: " @(rf/subscribe [:active]) " id: " id)
	[:ul.menu-list {:on-click (fn [_] (rf/dispatch [:active :project id]))} 
		[(if (= (second @(rf/subscribe [:active])) id) :a.is-active :a) name]]
)

(defn side-menu [] 
(let [us @(rf/subscribe [:user])
		projects @(rf/subscribe [:projects])]
(log! :i "us: " us)
	[:aside.menu.mt-3.ml-4 
		[:p.menu-label "User:"]
		[draw-user us]
		[:p.menu-label "Projects:"]
		(for [{:keys [name id]} projects]
			[project-element name id]
		)
		;(for )
	]
		
))

(defn user-component []
	(let [user-name (:name @(rf/subscribe [:user]))
		  project-count (count @(rf/subscribe [:projects]))]
		[:div.has-text-centered
			[:div.is-size-3 (str "Hello " user-name)]
			[:div "wellcome to the jungle"]
			(if (zero? project-count) 
				[:div "Start project please!!!"] 
				[:div (str "you have " project-count " projects!" )])
			[:div
				 [:button.button.mx-2 "Create Project"]
				 [:button.button.is-danger.mx-2 "Delete User"]
			]
		]
	)
)

(defn central-page []
	(case (first @(rf/subscribe [:active]))
		:user [user-component]
		:project "project component"
		:event "event component"
		"something wrong" 	
	))	

(defn main-screen []
	(let [hmm @(rf/subscribe [:active])]
	[:div
		[:section.hero.is-primary.has-text-centered [:div.hero-body 
			[:p.title "Here be dragons"] 
			[:p.subtitle "No other page is better than this"]]]
		[:div.columns
			[:div.column.is-one-third [side-menu]]
			[:div.column [central-page]]]
	]
))

(defn failure-screen []
	[:div "something went wrong!"])

(defn selector []
	(let [page @(rf/subscribe [:screen])]
  [:div
  
   (log! :i page)
   (case page
   :login [welcome-screen]
   :user [main-screen]
   [failure-screen]
   )
   
   [:button.button {:on-click (fn [_] (rf/dispatch [:futch "gazeta.pl"]))} "send"]
   [:pre (with-out-str (pprint @rfdb/app-db))]]))

