(ns blink.app.main
  (:require
   ["process" :as process]
   ["yargs/yargs" :as yargs]))


(def api-token (.. process -env -BIBLE_API_KEY))


(defn main
  []
  (js/console.log "hello world!"))


(comment
  api-token
  
  (+ 2 3))