(ns scores.reframe
  (:require [re-frame.core :as rf]
            [goog.object :as gobj]
			[lambdaisland.fetch :as fetch]
            [scores.s-log :refer [log!]]
			[clojure.walk :as walk]))

(rf/reg-event-db 
 :init-db
 (fn [_ _]
   (log! :i "DB Init")
   {:screen :scoresheet
    :coeff "no"
    :jump-params "ooo"
    :score "fujj"
    :r-dict {:jpoints "Punkty Sedziowskie"
             :wind "Rekom. za wiatr"
             :distance "Punkty za dystans"
             :height "Punkty za wysokość"
             :total "Pkty W Sumie"}}))

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









































