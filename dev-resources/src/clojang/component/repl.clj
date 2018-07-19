(ns clojang.component.repl
  "The Clojang component development namespace."
  (:require
    [clojang.component.core :as core]
    [clojang.component.system]
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.set :as set]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojusc.dev.system.core :as system-api]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]
    [trifl.java :refer [show-methods]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constants   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def system-ns "clojang.component.system")
(def refresh-callback 'clojang.component.repl/startup)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(logger/set-level! '[clojang] :debug)

(def ^:dynamic *mgr* nil)

(defn banner
  []
  (println (slurp (io/resource "text/banner.txt")))
  :ok)

(defn mgr-arg
  []
  (if *mgr*
    *mgr*
    (throw (new Exception
                (str "A state manager is not defined; "
                     "have you run (startup)?")))))

(defn system-arg
  []
  (if-let [state (:state *mgr*)]
    (system-api/get-system state)
    (throw (new Exception
                (str "System data structure is not defined; "
                     "have you run (startup)?")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   State Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn startup
  []
  (log/trace "Creating state manager ...")
  (alter-var-root #'*mgr* (constantly (system-api/create-state-manager)))
  (log/trace "State manager was created:" *mgr*)
  (log/trace "Setting system namespace ...")
  (system-api/set-system-ns (:state *mgr*) system-ns)
  (log/trace "Set system namespace to:" (system-api/get-system-ns (:state *mgr*)))
  (log/trace "System status:" (system-api/get-status (:state *mgr*)))
  (log/trace "Starting system ...")
  (system-api/startup *mgr*))

(defn shutdown
  []
  (when *mgr*
    (let [result (system-api/shutdown (mgr-arg))]
      (alter-var-root #'*mgr* (constantly nil))
      result)))

(def system system-arg)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Reloading Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn reset
  []
  (shutdown)
  (repl/refresh :after refresh-callback))

(def refresh #'repl/refresh)
