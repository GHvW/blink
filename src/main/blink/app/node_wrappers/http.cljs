(ns blink.app.node-wrappers.http
  (:require
   ["https" :as https]
   [cljs.core.match :refer-macros [match]]
   [cljs.core.async :refer [go chan <! put! take! close! reduce]]))


(defn deserialize
  [data]
  (.parse js/JSON data))


(defn node-get
  ([url] (node-get url #js {}))
  ([url options]
  (let [out-chan (chan)]
    (println (str "url is: " url))
    (-> (.get https url
              ;;  options
               (fn [res]
                ;;  (println (str "res is " res))
                 (let [status-code (.-statusCode res)]
                   (if (not= status-code 200)
                     (do
                      ;;  (println "status code not 200")
                       (put! out-chan [:error {:code status-code :message "error"}])
                       (.resume res)
                       (close! out-chan))
                     (do
                      ;;  (println "status code 200")
                       (.on res "data" (fn [chunk] (put! out-chan [:data chunk])))
                       (.on res "end" (fn [] (close! out-chan))))))))
        (.on "error" (fn [err]
                      ;;  (println (str "error: " err))
                       (put! out-chan [:error "cant perform request"])
                       (close! out-chan))))
    out-chan)))


(defn get-request
  ([url] (get-request url nil))
  ([url options]
   (println (str "got " url " with " options))
   (->> (node-get url options)
        (reduce (fn [buff next]
                  (println next)
                  (match [next]
                    [[:error message]] (do (println message) message)
                    [[:data data]] (str buff data)))
                ""))))

(comment
  (not= 1 2)

  (deserialize "{ \"person\": { \"name\": \"Garrett\" } }")

  (js->clj (deserialize "{ \"person\": { \"name\": \"Garrett\" } }"))

  (+ 1 2))