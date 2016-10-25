
(ns hellolambda.core-test
  (:require [cljs.test        :refer-macros [deftest testing is]]
            [hellolambda.core :refer        [main]]))

(deftype AwsLambdaContext
    [memoryLimitInMB]
  Object
  (getRemainingTimeInMillis [this] 900))

(defn jsonstr->
  [s]
  (js->clj (.parse js/JSON s) :keywordize-keys true))

(def ^{:dynamic true}
  result)

(defn ^{:private true :dynamic true}
  capture
  [& args]
  (swap! result (fn [_] args))
  nil)

(deftest test:polite?
  (binding [result (atom nil)]
    (is (= "hello, lambda"
           (let [res (jsonstr-> (main {:name "lambda"}
                                      (->AwsLambdaContext 1)
                                      capture))]
             (:result (second @result)))))))
