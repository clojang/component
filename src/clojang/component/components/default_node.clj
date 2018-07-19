(ns clojang.component.components.default-node
  (:require
    [clojang.agent.core :as agent]
    [clojang.component.components.config :as config]
    [clojang.mbox :as mbox]
    [clojang.node :as node]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord DefaultNode [
  node-name
  node
  mbox-name
  mbox])

(defn start
  [this]
  (log/info "Starting default node component ...")
  (let [data (agent/-main)]
    (log/debug "Started default node component.")
    (assoc this :node-name (get-in data [:node :name])
                :node (get-in data [:node :object])
                :mbox-name (get-in data [:mbox :name])
                :mbox (get-in data [:mbox :object]))))

(defn stop
  [this]
  (log/info "Stopping default node component ...")
  (mbox/exit (:mbox this) :normal)
  (node/close (:node this))
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
