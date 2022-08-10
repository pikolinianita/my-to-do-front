(ns todo.reframe
  (:require [re-frame.core :as rf]
            [goog.object :as gobj]
			[lambdaisland.fetch :as fetch]
            [todo.s-log :refer [log!]]
			[clojure.walk :as walk]))

(def api "http://localhost:8080/api/")

(rf/reg-event-db 
 :init-db
 (fn [_ _]
   (log! :i "DB Init")
   {:screen :login
    :active :login}
    ))
	
(rf/reg-event-db 
 :dev-init-db
  (fn [_ _]
   (log! :i "DB Init")
 {:screen :user,
 :active [:user nil]
 :user {:id 1, :name "Worker"},
 :projects
 [{:id 4, :name "Read"}
  {:id 3, :name "Program"} 
  {:id 5, :name "Not Started"}]}))
	

(rf/reg-event-db
	:fetch
	(fn [db [_ arg]]
		(log! :i "try to send: " arg)
		(let [response (-> (.fetch js/window "https://api.github.com/users/pikolinianita")
						(.then #(.json %))  ; Get JSON from the Response.body ReadableStream
						;(.then #(log! :e (.stringify js/JSON %)))
						;(.then #(.-login %))
						;(.then #(-> % js/JSON.stringify js/JSON.parse))
						;(.then #(js/Object.assign #js {} %))
						;(.then #(js->clj %))
						;(.then #(gobj/get % "login"  ))
						;(.then #(log! :i %))
						)
			 resp (.stringify js/JSON response)
			]
			(assoc db :response response :stringify resp)
			)
	))
	
(rf/reg-event-db
	:futch
	(fn [db [_ arg]]
	
	(-> (fetch/get "http://localhost:8080/api/user/Worker")
    (.then (fn [resp]
			(log! :i resp)
             (-> resp
                 :body
                 ;; the actual response is a js object, not a clojure map
                 ;;(gobj/get "data")
				 )))
    (.then (fn [data]
				(log! :w data)
				;(js->clj data :keywordize-keys true)
             (rf/dispatch [:data (walk/keywordize-keys data)]))))
	db
	))


(rf/reg-event-db
	:data
	(fn [db [_ arg]]
	(assoc db :data arg)
	)
)

(rf/reg-event-db
	:active
	(fn [db [_ arg id]]
	(log! :i "activate: " arg " id: " id)
	(assoc db :active [arg id])
	)
)

(rf/reg-event-db
	:try-login
	(fn [db [_ name]]
	(rf/dispatch [:fitch-get (str "user/" name) :got-user :failed-user])
	db
	)
)

(rf/reg-event-db
	:fitch-get
	(fn [db [_ name succ failure]]
		(-> (.fetch js/window (str api name) (clj->js {:mode "cors"}))
		(.then #(.json %))
		(.then #(rf/dispatch [succ (js->clj % :keywordize-keys true)]))
		(.catch #(rf/dispatch [failure]))
		
		)
	db
	
	)
)

(rf/reg-event-db
	:failed-user
	(fn [db _]
		(assoc db :screen :login)
	)
)

(rf/reg-event-db
	:got-user
	(fn [db [_ body]]
		(log! :i body)
		(assoc db :active [:user nil] :screen :user, :user (dissoc body :projects), :projects (body :projects))
	)
)

(rf/reg-sub
	:screen
	(fn [db]
		(db :screen)
	)
)

(rf/reg-sub 
	:active
	(fn [db]
		(db :active)
	)
)

(rf/reg-sub 
	:user
	(fn [db] 
	(log! :i "user sub: " db)
	(db :user)))

(rf/reg-sub  
	:projects
	(fn [db] 
	(log! :i "projects sub: " db)
	(db :projects)))




























