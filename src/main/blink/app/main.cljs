(ns blink.app.main
  (:require
   ["process" :as process]
  ;;  ["yargs/yargs" :as yargs]
   [cljs.core.async :refer [go chan <! put! >!]]
   [blink.app.node-wrappers.http :refer [get-request]]))


(def api-token (.. process -env -BIBLE_API_KEY))


(defn main
  []
  (js/console.log "hello world!!!!a")
  (go
    (let [it (<! (get-request "https://api.chucknorris.io/jokes/random"))]
      (println it))))


(comment
  api-token
  
  (+ 2 3))