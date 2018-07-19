(ns clojang.component.system
  (:require
    [clojang.component.components.config :as config]
    [clojang.component.components.default-node :as default-node]
    [clojang.component.components.logging :as logging]
    [com.stuartsierra.component :as component]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Common Configuration Components   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def cfg
  {:config (config/create-component)})

(def log
  {:logging (component/using
             (logging/create-component)
             [:config])})

(def node
  {:default-node (component/using
                  (default-node/create-component)
                  [:config :logging])})

(def node-without-logging
  {:default-node (component/using
                  (default-node/create-component)
                  [:config])})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Initializations   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn initialize-config-only
  []
  (component/map->SystemMap cfg))

(defn initialize-bare-bones
  []
  (component/map->SystemMap
    (merge cfg
           log)))

(defn initialize-with-node
  []
  (component/map->SystemMap
    (merge cfg
           log
           node)))

(defn node-without-logging
  []
  (component/map->SystemMap
    (merge cfg
           node-without-logging)))

(def init-lookup
  {:basic #'initialize-bare-bones
   :testing-config-only #'initialize-config-only
   :testing #'node-without-logging
   :node #'initialize-with-node})

(defn init
  ([]
    (init :node))
  ([mode]
    ((mode init-lookup))))
