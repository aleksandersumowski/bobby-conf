(defproject bobby-conf "0.0.2-SNAPSHOT"
  :description "Simple configuration for Clojure apps"
  :url "http://github.com/rsslldnphy/bobby-conf"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[expectations "2.0.7"]]
                   :plugins [[lein-expectations "0.0.8"]
                             [lein-autoexpect "1.0"]]}})
