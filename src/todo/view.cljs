(ns todo.view
  (:require [cljs.pprint :refer [cl-format]]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
			[todo.reframe :as reframki]
			[todo.s-log :refer [log!]]
            [cljs.pprint :refer [pprint cl-format]]
            [re-frame.db :as rfdb]))

;http://html2hiccup.buttercloud.com/

(defn new-user-modal [] 
	(when (= :user @(rf/subscribe [:modal]))
	(let [inp (reagent/atom {:name "" :placeholder "Write User Number"})]
	(fn [] [(if (= :user @(rf/subscribe [:modal])) :div.modal.is-active :div.modal)
		[:div.modal-background]
		[:div.modal-card
			[:header.modal-card-head
			[:p.modal-card-title "Create user"]
			[:button {:class "delete", :aria-label "close" :on-click (fn [_] (rf/dispatch [:modal-off]))}]]
	[:section.modal-card-body 
			[:div "Enter user name:"]
			[:input {:type :text
            :value (@inp :name)
			:name "user-name"
            :on-change (fn [e]
						(log! :i (-> e .-target .-value))
                         (swap! inp assoc :name (-> e .-target .-value)))}] ]
	[:footer.modal-card-foot
		[:button.button.is-success {:on-click (fn [_] (rf/dispatch [:create-user (@inp :name)]))} "Save changes"]
		[:button.button {:on-click (fn [_] (rf/dispatch [:modal-off]))} "Cancel"]]]])))
)

(defn welcome-screen []
(let [inp (reagent/atom {:name "" :placeholder "Write User Number"})]
	(fn [] 
	[:div
		[:h1 "Please enter user name"]
		[new-user-modal]
		[:input {:type :text
            :value (@inp :name)
			:name "user-name"
            :on-change (fn [e]
						(log! :i (-> e .-target .-value))
                         (swap! inp assoc :name (-> e .-target .-value)))}]
		[:button.button {:on-click (fn [_] (rf/dispatch [:try-login (@inp :name)]))} "Find User"]
        [:button.button {:on-click (fn [_] (rf/dispatch [:modal :user]))} "Create User - not work yet"]		
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
	]		
))



(defn new-project-modal []
	(when (= :project @(rf/subscribe [:modal])) 
	[:div.modal.is-active
		[:div.modal-background]
		[:div.modal-card.
			[:header.modal-card-head.
			[:p.modal-card-title "Modal title"]
			[:button {:class "delete", :aria-label "close" :on-click (fn [_] (rf/dispatch [:modal-off]))}]]
	[:section.modal-card-body  "<!-- Content ... -->" ]
	[:footer.modal-card-foot
		[:button.button.is-success "Save changes"]
		[:button.button {:on-click (fn [_] (rf/dispatch [:modal-off]))} "Cancel"]]]])

)

(defn user-component []
	(let [user-name (:name @(rf/subscribe [:user]))
		  project-count (count @(rf/subscribe [:projects]))]
		[:div.has-text-centered
			[:div.is-size-3 (str "Hello " user-name)]
			[new-project-modal]
			[:div "wellcome to the jungle"]
			(if (zero? project-count) 
				[:div "Start project please!!!"] 
				[:div (str "you have " project-count " projects!" )])
			[:div
				 [:button.button.mx-2 {:on-click (fn [_] (rf/dispatch [:modal :project]))} "Create Project"]
				 [:button.button.is-warning.mx-2 {:on-click (fn [_] (rf/dispatch [:init-db]))} "Log out"]
				 [:button.button.is-danger.mx-2 {:on-click (fn [_] 
					(rf/dispatch [:fitch-delete (str "user/" (@(rf/subscribe [:user]) :id)) :init-db :failed-user]))} 
					"Delete User"]
;;;;-------------------!!!!!!!!!!!!!!!!!!!!-----------------------------
;;;; FIX DELETE USER SERVER SIDE  ------------------					
			]
		]
	)
)



(defn project-component []
	(let [active-project @(rf/subscribe [:active-project])]
		[:div (str active-project)]
	)
)

(defn central-page []
	(case (first @(rf/subscribe [:active]))
		:user [user-component]
		:project [project-component]
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
			[:div.column.is-one-quarter [side-menu]]
			[:div.column.is-half [central-page]]
			[:div.column.is-one-quarter [:div "kill space"]]]
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

