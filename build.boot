
(set-env!
 :source-paths   #{"build-resources/cljs-config"
                   "source/clj"
                   "source/cljs"
                   "test"})

(set-env!
 :dependencies '[[org.clojure/clojure         "1.9.0"          :scope "test"]
                 [org.clojure/clojurescript   "1.10.191"       :scope "test"]
                 [adzerk/boot-cljs            "2.1.4"          :scope "test"]
                 [boot-aws-lambda-kit         "0.1.0-SNAPSHOT" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.4"          :scope "test"]
                 [familiar                    "0.1.0"          :scope "test"]])

(require '[hellolambda.boot :refer :all])
