(ns clojang.component.components.epmd
  (:require
    [clojang.component.components.config :as config]
    [clojure.java.shell :as shell]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]
    [trifl.ps :as ps]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constants   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def epmd-executable "epmd")
(def epmd-arg "-daemon")
(def started-message "Started Erlang Port Mapper Daemon component.")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-epmd-process
  []
  (->> (ps/get-ps-info)
       (filter #(= (:comm %) epmd-executable))
       first))

(defn stop-process
  [process-info]
  (shell/sh "kill" "-15" (str (:pid process-info))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Epmd [
  already-running?
  process-info])

(defn start
  "This component tracks whether the component started epmd or if it was
  already started. If the component starts epmd, it also tracks the process
  data (such as the pid for epmd). If the process was already started, it
  does not track the process data."
  [this]
  (log/info "Starting Erlang Port Mapper Daemon component ...")
  (if (seq (get-epmd-process))
    (let [updated (assoc this :already-running? true)]
      (log/debug "Erlang Port Mapper Daemon process is running; skipping ...")
      (log/debug started-message)
      updated)
    (do
      (shell/sh epmd-executable epmd-arg)
      (let [process-info (get-epmd-process)]
        (if (seq process-info)
          (let [updated (assoc this :already-running? false
                                    :process-info process-info)]
            (log/debug "Started the Erlang Port Mapper Daemon process.")
            (log/debug started-message)
            updated)
          (let [updated (assoc this :already-running? false
                                    :process-info nil)]
            (log/warn "There was a problem starting the Erlang Port Mapper Daemon")
            (log/debug started-message)
            updated))))))

(defn stop
  "If the component started the epmd process, it will also terminate it. If it
  did not start it, it just leaves it alone."
  [this]
  (log/info "Stopping Erlang Port Mapper Daemon component ...")
  (when-not (:already-running? this)
    (stop-process (get-epmd-process))
    (let [process-info (get-epmd-process)]
      (if (seq process-info)
        (log/warn "There was a problem stopping the Erlang Port Mapper Daemon.")
        (log/debug "Stopped the Erlang Port Mapper Daemon process."))))
  (log/debug "Stopped Erlang Port Mapper Daemon component.")
  (Thread/sleep 1000)
  (assoc this :already-running? nil
              :process-info nil))

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend Epmd
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  []
  (map->Epmd {}))
