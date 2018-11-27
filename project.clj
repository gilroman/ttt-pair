(def localhost-address "")
(defproject tic_tac_toe_pair "0.1.0-SNAPSHOT"
  :description "Tic Tac Toe"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async  "0.4.490"]
                 [cheshire "5.8.1"]
                 [cljs-http "0.1.45"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.4"]]
  :main ^:skip-aot tic-tac-toe-pair.core
  :ring {:handler tic-tac-toe-pair.api/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[com.jakemccrary/lein-test-refresh "0.23.0"]
                             [lein-doo "0.1.10"]]
                   :dependencies [[ring/ring-mock "0.3.2"]
                                  [binaryage/devtools "0.9.9"]
                                  [org.clojure/clojurescript "1.10.439"]
                                  [lein-cljsbuild "1.1.7"]
                                  [reagent "0.8.1"]
                                  [com.bhauman/figwheel-main "0.1.9"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]]
                   :source-paths ["src/clj" "test/clj" "src/cljs" "test/cljs"]
                   :resource-paths ["resources" "target"]}}

  :cljsbuild {:builds {:test      {:source-paths ["src/cljs" "test/cljs"]
                                   :compiler {:main tic-tac-toe-pair.doo-runner
                                              :optimizations :simple
                                              :output-to "out/test-main.js"}}}}
  
  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]
            "build-dev" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "build-test" ["trampoline" "run" "-m" "figwheel.main" "-b" "test" "-r"]})
