(ns blink.app.main
  (:require
   ["process" :as process]
   ["yargs" :as yargs]
   [cljs.core.async :refer [go chan <! put! >!]]
   [blink.app.node-wrappers.http :refer [get-request]]))


(def api-token (.. process -env -BIBLE_API_KEY))
(def base-bible-api-url "https://api.scripture.api.bible")

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
  (get-request (str base-bible-api-url "/v1/bibles")
               #js {:headers #js {"api-key" api-token}}))

  
(def language-filter (filter (get coll "language")))


(defn main
  []
  (let [args (-> yargs
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
                 (.command "versions [language]"
                           "get a list of bible versions available"
                           (fn [yrgs] 
                             (.positional yrgs "language" #js {:type "string"
                                                               :default nil
                                                               :describe "filter results by language translation"}))
                           (fn [argv]
                             (go
                               (println (<! (get-versions))))))
                 (.help)
                 (.epilog "Thank you Bible.API")
                 (.-argv))]
    (println args)))


(comment
  api-token

  (+ 2 3))