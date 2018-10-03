
(set-env!
 :source-paths   #{"build-resources/cljs-config"
                   "source/clj"
                   "source/cljs"
                   "test"})

(set-env!
 :dependencies '[[org.clojure/clojure           "1.8.0"]
                 [org.clojure/clojurescript     "1.7.228"]
                 [adzerk/boot-cljs              "1.7.228-1"      :scope "test"]
                 [boot-aws-lambda-kit           "0.1.0-SNAPSHOT" :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.3.4-SNAPSHOT" :scope "test"]
                 [com.cemerick/piggieback       "0.2.1"          :scope "test"]
                 [familiar                      "0.1.0-SNAPSHOT" :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"         :scope "test"]])
                              ; needed by piggieback

(swap! boot.repl/*default-middleware* conj 'cemerick.piggieback/wrap-cljs-repl)

(require '[hellolambda.boot :refer :all])
