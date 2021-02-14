(ns blink.app.main
  (:require
   ["process" :as process]
   ["yargs" :as yargs]
   [cljs.core.async :refer [go chan <! put! >!]]
   [blink.app.node-wrappers.http :refer [get-request]]))


(def api-token (.. process -env -BIBLE_API_KEY))


(defn get-test
  []
  (js/console.log "hello world!!!!a")
  (go
    (let [it (<! (get-request "https://api.chucknorris.io/jokes/random"))]
      ;; (println (apply str it))
      (println it)
      (println (type "hi")))))

(defn get-versions
  []
  (get-request #js {:headers #js {"api-key" api-token}} ""))

(defn main
  []
  (-> yargs
      (.scriptName "blink")
      (.usage "$0 <cmd> [args]")
      (.command "greetings [name]"
                "greetings!"
                (fn [yrgs]
                  (.positional yrgs "name" #js {:type "string"
                                                :default "seeker"
                                                :describe "the name to greet"}))
                (fn [argv]
                  (println (str "Greetings " (.-name argv) "!"))))
      (.command "versions"
                "get a list of bible versions available"
                (fn [] #js {})
                (fn [_]
                  (go
                    (println (<! (get-versions))))))
      (.help)
      (.epilog "Thank you Bible.API")
      (.-argv)))


(comment
  api-token

  (+ 2 3))