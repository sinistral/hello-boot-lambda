
(ns hellolambda.core
  (:require [cljs.nodejs :as nodejs]))

;;; ----------------------------------------------------------------------- ;;;

(defn- ->jsonstr
  [x]
  (.stringify js/JSON x))

(defn- wrap-invocation
  [f]
  (try
    {:succeeded? true :failed? false :result (f)}
    (catch :default e
      {:suceeded false :failed? true :result e})))

(defn- unpack-input
  [x]
  [x])

(defn- pack-output
  [x]
  (-> x (clj->js) (->jsonstr)))

;;; ----------------------------------------------------------------------- ;;;

(defn- λ
  [event]
  (str "hello, " (:name event)))

(defn ^{:export true} main
  [event context callback]
  (let [result (-> {:available-execution-time
                    {:quantity (.getRemainingTimeInMillis context) :units "ms"}
                    :began-at
                    (.toISOString (js/Date.))}
                   (merge (wrap-invocation #(apply λ (unpack-input event))))
                   (assoc :ended-at (.toISOString (js/Date.))))]
    (if (:succeeded? result)
      (callback nil result)
      (callback result nil))))

(set! *main-cli-fn* identity)
