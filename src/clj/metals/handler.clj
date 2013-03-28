(ns metals.handler
  (:require [compojure.core         :refer :all]
            [ring.middleware.file   :refer :all]
            [ring.middleware.params :refer :all]
            [compojure.handler     :as handler]
            [compojure.route       :as route]
            [metals.layout         :as layout]
            [metals.data           :as data]
            [clojure.tools.logging :as log]))

(defroutes app-routes
  (GET "/" [] (layout/render))
  (GET "/data" [currency]
    (let [currency (if-not (#{"rub" "dollar"} currency) "rub" currency)]
      (data/json-table (keyword currency))))
  (GET "/populate" [days]
    (data/populate (or (data/to-i days) 1))
    "ok")
  (route/not-found (layout/not-found)))

(def app
  (-> app-routes
      handler/site
      wrap-params
      (wrap-file "js")))

;(defn populate-bg []
  ;(try
    ;(data/populate 1)
    ;(catch Exception e
      ;(log/info (str "Daemon Thread Exception: " (.getMessage e)))))
  ;(Thread/sleep (* 1000 60 60 10))
  ;(recur))

;(doto
  ;(Thread. populate-bg)
  ;(.setDaemon true)
  ;(.start))