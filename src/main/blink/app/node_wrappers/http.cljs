(ns blink.app.node-wrappers.http
  (:require
   ["https" :as https]
   [cljs.core.match :refer-macros [match]]
   [cljs.core.async :refer [go chan <! put! >! close! reduce]]))


(defn deserialize
  [data]
  (.parse js/JSON data))


(defn node-get
  ([url] (node-get url #js {}))
  ([url options]
  (let [out-chan (chan)]
    (-> (.get https 
              url
              options
              (fn [res]
                (let [status-code (.-statusCode res)]
                  (if (not= status-code 200)
                    (do
                      (put! out-chan [:error {:code status-code :message "error"}])
                      (close! out-chan) ;; close channel first in case .resume creates a data event
                      (.resume res))
                    (do
                      (.setEncoding res "utf8")
                      (.on res "data" (fn [chunk] (put! out-chan [:data chunk])))
                      (.on res "end" (fn [] (close! out-chan))))))))
        (.on "error" (fn [err]
                       (put! out-chan [:error "cant perform request"])
                       (close! out-chan))))
    (reduce (fn [buff next]
              (match [next]
                [[:error message]] (conj buff message)
                [[:data data]] (conj buff data)))
            []
            out-chan))))


(defn get-request
  ([url] (get-request url nil))
  ([url options]
  ;; (println options)
   (let [out (chan 1)]
     (go
       (>! out (apply str (<! (node-get url options)))))
     out)))


(comment
  (not= 1 2)

  (deserialize "{ \"person\": { \"name\": \"Garrett\" } }")

  (js->clj (deserialize "{ \"person\": { \"name\": \"Garrett\" } }"))

  (apply str ["hello" " " "world"])

  (apply str (conj [] "hello"))

  (+ 1 2))