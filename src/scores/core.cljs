(ns ^:figwheel-hooks scores.core
  (:require
   [scores.view :as view]
   [scores.reframe]
   ;; libraries: 
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]  
   [re-frame.core :as rf]))

(println "This text is printed from src/scores/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element [] 
  (gdom/getElement "app"))

(defn mount [el]
  (rdom/render [view/selector] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (rf/dispatch-sync [:init-db])    	
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)


;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
	(mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
