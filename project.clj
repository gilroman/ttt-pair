(defproject tic_tac_toe_pair "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.8.1"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.4"]]
  :main ^:skip-aot tic-tac-toe-pair.core
  :ring {:handler tic-tac-toe-pair.api/app}
  :target-path "target/%s"
  :profiles { :uberjar {:aot :all}
              :dev {:plugins [[venantius/ultra "0.5.2"]   
                              [com.jakemccrary/lein-test-refresh "0.23.0"]]
                    :dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring/ring-mock "0.3.2"]]}
  })
