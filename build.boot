
(set-env!
 :dependencies '[[org.clojure/clojure         "1.7.0"]
                 [org.clojure/clojurescript   "1.7.228"]
                 [adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                 [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
                 [com.cemerick/piggieback     "0.2.1"          :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"         :scope "test"] ;; needed by piggieback
                 [sinistral/mantle            "0.3.0"          :scope "test"]
                 [stencil                     "0.5.0"          :scope "test"]])

(require
 '[clojure.java.io             :as    io]
 '[clojure.string              :as    str]
 '[adzerk.boot-cljs            :refer [cljs]]
 '[boot.repl                   :as    repl]
 '[boot.util                   :refer [info]]
 '[cemerick.piggieback         :refer [cljs-repl]]
 '[cljs.repl.node              :refer [repl-env]]
 '[crisptrutski.boot-cljs-test :refer [exit! test-cljs]]
 '[mantle.io                   :refer [fmtstr]]
 '[stencil.core                :refer [render-file]])

(swap! repl/*default-middleware* conj 'cemerick.piggieback/wrap-cljs-repl)

(set-env!
 :source-paths   #{"build-resources" "source" "test"}
 :resource-paths #{"build-resources"})

(defn start-cljs-repl
  []
  (cljs-repl (repl-env)))

(deftask +test
  []
  (task-options! test-cljs {:js-env :node})
  (test-cljs))

(deftask handler
  "Injects the into main `.js` file the NodeJS `exports` assignment that
  exposes the handler function to AWS Lambda."
  [i ids SET #{str} "The builds for which handlers are to be defined."]
  (let [tmpdir (tmp-dir!)]
    (with-pre-wrap fileset
      (empty-dir! tmpdir)
      (letfn [(cljfn->jsfn [name]
                (str/replace name #"/" "."))
              (render-insert [handler-data]
                (render-file "templates/handler-nodejs-export" handler-data))
              (emit-handler [fs id]
                (let [edn-file (first (by-name [(str id ".cljs.edn")] (input-files fs)))
                      edn      (read-string (slurp (tmp-file edn-file)))
                      fn-name  (cljfn->jsfn (str (:aws-lambda/handler edn)))
                      tmpl-dat {:export-as "handler" :export-fn fn-name}
                      insert   (render-insert tmpl-dat)
                      js-file  (first (by-name [(str id ".js")] (input-files fs)))
                      out-file (io/file tmpdir (tmp-path js-file))]
                  (info "Defining handler using spec: %s%n" tmpl-dat)
                  (spit out-file (str (slurp (tmp-file js-file)) insert))
                  (-> fs
                      (rm [edn-file js-file])   ; remove defunct .js
                      (add-resource tmpdir))))] ; add updated .js
        (commit! (reduce emit-handler fileset ids))))))

(deftask +build
  []
  (comp
   (cljs    :ids #{"hellolambda"}
            :optimizations :none
            :compiler-options {:target :nodejs})
   (handler :ids #{"hellolambda"})
   (jar     :file "hellolambda.zip")
   (target  :dir #{"target"})))
