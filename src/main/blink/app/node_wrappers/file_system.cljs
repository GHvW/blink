(ns blink.app.node-wrappers.file-system
  (:require
   ["fs" :as fs]
   [cljs.core.async :refer [go chan <! put! >! close! reduce]]))


(defn write-file
  [file-name data]
  (let [out-chan (chan 1)]
    (.writeFile fs file-name data
                (fn [err]
                  (if-some [e err]
                    [:error (str "unable to write file " file-name " " e)]
                    [:ok nil])))
    out-chan))


(defn append
  []
  ())