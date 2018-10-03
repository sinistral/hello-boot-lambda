
(ns hellolambda.boot
  (:refer-clojure :exclude [test])
  (:require [boot.core                   :as core]
            [boot.task.built-in          :as task]
            [adzerk.boot-cljs            :as cljs       :refer [cljs]]
            [cljs.repl.node              :as cljs-node  :refer [repl-env]]
            [crisptrutski.boot-cljs-test :as cljs-test  :refer [test-cljs]]
            [boot-aws-lambda-kit.core    :as lambda-kit :refer [handler:cljs]]))

(core/deftask test
  []
  (core/task-options! test-cljs {:exit? true :js-env :node})
  (test-cljs))

(core/deftask build
  []
  (comp (cljs :ids #{"hellolambda"}
              :optimizations :none
              :compiler-options {:target :nodejs})
        (handler:cljs :ids  #{"hellolambda"})
        (task/jar :file "hellolambda.zip")
        (task/target :dir #{"target"})))
