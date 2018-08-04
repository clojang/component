(ns clojang.component.core
  "Ordinarily, component-specific functions would reside in a component's
  namespace, in this case that would be
  `clojang.component.components.default-node`. However, since this entire
  library is dedicated to managing a default Clojang node. those functions are
  here, in the core namespace.

  Since this library is strictly for managing a default node in a running
  system, all functions take as their first parameter a system or component data
  structure that has `:default-node` as a key.

  If you wish to use the general Clojang functions for communicating with an
  Erlang or Clojure node, you chould be using the clojang library directly."
  (:require
    [clojang.component.system]
    [clojusc.system-manager.core :refer :all]
    [clojusc.twig :as logger]
    [taoensso.timbre :as log]
    [trifl.java :as java])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Convenience API for a Running System   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn node-name
  ([]
    (node-name (system)))
  ([system]
    (get-in system [:default-node :node-name])))

(defn node
  ([]
    (node (system)))
  ([system]
    (get-in system [:default-node :node])))

(defn mbox-name
  ([]
    (mbox-name (system)))
  ([system]
    (get-in system [:default-node :mbox-name])))

(defn mbox
  ([]
    (mbox (system)))
  ([system]
    (get-in system [:default-node :mbox])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Run the Component as an Application   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def setup-options {
  :init 'clojang.component.system/init
  :throw-errors true})

(defn init
  "This is used to set the options and any other global data.

  This is defined in a function for re-use. For instance, when a REPL is
  reloaded, the options will be lost and need to be re-applied."
  []
  (setup-manager setup-options))

(defn -main
  "For best results, run with: `lein trampoline run`

  To see the demo output, run: `lein trampoline run demo`"
  [& args]
  (init)
  (startup)
  (java/add-shutdown-handler #(do
                               (log/warn "Shutting down system ...")
                               (shutdown)))
  (case (keyword (first args))
    :demo (log/info "Demo of core function calls:"
                    (logger/pprint {:node-name (node-name)
                                    :node (node)
                                    :mbox-name (mbox-name)
                                    :mbox (mbox)}))
    :unexpected-arg)
  (java/join-current-thread))
