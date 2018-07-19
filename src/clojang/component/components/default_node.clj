(ns clojang.component.components.default-node
  (:require
    [clojang.component.components.config :as config]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord DefaultNode [])

(defn start
  [this]
  (log/info "Starting default node component ...")
  (let [log-level (config/log-level this)
        log-nss (config/log-nss this)]
    ;; XXX TBD
    (log/debug "Started default node component.")
    this))

(defn stop
  [this]
  (log/info "Stopping default node component ...")
  (log/debug "Stopped default node component.")
  this)

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend DefaultNode
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  []
  (map->DefaultNode {}))
