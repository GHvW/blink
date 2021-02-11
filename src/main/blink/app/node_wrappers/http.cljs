(ns blink.app.node-wrappers.http
  (:require
   ["http" :as http]
   [cljs.core.async :refer [go chan <! put!]]))


(defn get-request
  ([url] (get url nil))
  ([url options]
  (let [out-chan (chan)]
    (http/get url
              options
              (fn [response]
                (let [status-code (.-statusCode response)]
                  (if (not= status-code)
                    (put! out-chan [status-code "error"])
                    ())))))))



(comment
  (not= 1 2)
  (+ 1 2))