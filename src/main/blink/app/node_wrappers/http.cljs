(ns blink.app.node-wrappers.http
  (:require
   ["http" :as http]
   [cljs.core.async :refer [go chan <! put! close!]]))


(defn deserialize
  [data]
  (.parse js/JSON data))


(defn node-get
  ([url] (get url nil))
  ([url options]
  (let [out-chan (chan)]
    (http/get url
              options
              (fn [response]
                (let [status-code (.-statusCode response)]
                  (if (not= status-code 200)
                    (do
                      (put! out-chan [:error {:code status-code :message "error"}])
                      (.resume response))
                    (do
                      (.on response "data" (fn [chunk] (put! [:data chunk])))
                      (.on response "end" (fn [] (close! out-chan))))))))
    out-chan))



(comment
  (not= 1 2)

  (deserialize "{ \"person\": { \"name\": \"Garrett\" } }")

  (js->clj (deserialize "{ \"person\": { \"name\": \"Garrett\" } }"))

  (+ 1 2))