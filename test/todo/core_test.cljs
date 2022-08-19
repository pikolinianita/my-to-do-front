(ns todo.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [todo.core :refer [multiply]]
	 [malli.core :as m] 
	 [malli.error :as me]))

(deftest multiply-test
  (is (= (* 1 2) (multiply 1 2))))

(deftest multiply-test-2
  (is (= (* 75 10) (multiply 10 75))))

(def start-data 
{:screen :user,
 :active :user,
 :user {:id 1, :name "Worker"},
 :projects
 [{:id 4, :name "Read"}
  {:id 3, :name "Program"} 
  {:id 5, :name "Not Started"}]})
  
(def other-data 
{:screen :user,
 :active :user, 
 :user {:id 2, :name "Lazy Guy"}, 
 :projects []})  