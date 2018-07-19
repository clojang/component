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
  )
