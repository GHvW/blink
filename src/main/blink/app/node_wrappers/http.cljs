(ns blink.app.node-wrappers.http
  (:require
   ["https" :as https]
   [cljs.core.match :refer-macros [match]]
   [cljs.core.async :refer [go chan <! put! take! close! reduce]]))


(defn deserialize
  [data]
  (.parse js/JSON data))


(defn node-get
  ([url] (get url #js {}))
  ([url options]
  (let [out-chan (chan)]
    (https/get url
               options
               (fn [res]
                 (let [status-code (.-statusCode res)]
                   (if (not= status-code 200)
                     (do
                       (put! out-chan [:error {:code status-code :message "error"}])
                       (.resume res)
                       (close! out-chan))
                     (do
                       (.on res "data" (fn [chunk] (put! out-chan [:data chunk])))
                       (.on res "end" (fn [] (close! out-chan))))))))
    out-chan)))


(defn get-request
  ([url] (get-request url nil))
  ([url options]
   (let [out-chan (chan 1)]
     (reduce (fn [buff next]
               (match [next]
                 [[:error message]] message
                 [[:data data]] (str buff data))) 
             "" 
             (node-get url options)))))

(comment
  (not= 1 2)

  (deserialize "{ \"person\": { \"name\": \"Garrett\" } }")

  (js->clj (deserialize "{ \"person\": { \"name\": \"Garrett\" } }"))

  (+ 1 2))