(defproject todo "0.1.0-SNAPSHOT"
  :description "Front For TODO app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.773"]
                 [reagent "1.1.0"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [re-frame "1.3.0-rc2"]
				 [lambdaisland/fetch "1.0.41"]
                 [metosin/malli "0.8.1"]
                 [day8.re-frame/test "0.1.5"]
                 [superstructor/re-frame-fetch-fx "0.2.0"]
                 [day8.re-frame/http-fx "0.2.4"]]

  :source-paths ["src"]

  :aliases {"fig"       ["run" "-m" "figwheel.main"]
            "fig:build" ["run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
			"fig:deploy" ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "deploy"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "todo.test-runner"]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.15"]
									[day8.re-frame/tracing      "0.6.2"]
                                  [day8.re-frame/re-frame-10x "1.0.2"]]
                   
                   :resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["target"]}
			:deploy {:resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                      :clean-targets ^{:protect false} ["target"]}	   
				   
				   })

