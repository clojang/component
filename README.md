# component

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]

*A Clojang life-cycle implementation of the Component library for use in large/complex applications*

[![Project logo][logo]][logo-large]


#### Contents

* [About](#about-)
* [Usage](#usage-)
* [Donating](#donating-)
* [License](#license-)


## About [&#x219F;](#contents)

While the [Clojang agent][agent] is useful for emulating some of the convenience of
the Erlang shell and LFE REPL, it may be too fragile for systems deployments, or
developer may prefer to use more common idioms in Clojure deployments, namely the
use of the Component library.

This project provides the same functionality that the agent does, but in a form
that can be easily integrated with other Clojure components as part of a larger
system. In particular, a default node component is provided -- this may be either
used as is, or as the basis of a customized component, specially suited for your
needs.


## Usage [&#x219F;](#contents)

Basic usage is as follows:

1) Start up the REPL:

```
$ lein repl
```
```

  ___| |      _)
 |     |  _ \  |  _` | __ \   _` |
 |     | (   | | (   | |   | (   |
\____|_|\___/  |\__,_|_|  _|\__, |
           ___/             |___/
----------      ------------     -
-  c o m p o n e n t  ------------
----------------------------------

Clojure 1.9.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_161-b12
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

[clojang.component.repl] λ=>
```

2) Start the Clojang default node component, which is part of the
sample system:

```clj
[clojang.component.repl] λ=> (startup)
2018-07-19T15:43:06.935 [nREPL-worker-0] INFO clojang.component.components.config:39 - Starting config component ...
2018-07-19T15:43:06.970 [nREPL-worker-0] DEBUG clojang.component.components.config:40 - Started config component.
2018-07-19T15:43:06.976 [nREPL-worker-0] INFO clojang.component.components.logging:16 - Starting logging component ...
2018-07-19T15:43:06.977 [nREPL-worker-0] DEBUG clojang.component.components.logging:19 - Setting up logging with level :debug
2018-07-19T15:43:06.978 [nREPL-worker-0] DEBUG clojang.component.components.logging:20 - Logging namespaces: [clojang com.ericsson.otp.erlang jiface]
2018-07-19T15:43:06.982 [nREPL-worker-0] DEBUG clojang.component.components.logging:22 - Started logging component.
2018-07-19T15:43:06.983 [nREPL-worker-0] INFO clojang.component.components.default-node:23 - Starting default node component ...
2018-07-19T15:43:06.985 [nREPL-worker-0] INFO clojang.agent.startup:22 - Bringing up OTP node on clojang@spacemac ...
2018-07-19T15:43:06.996 [nREPL-worker-0] INFO clojang.agent.startup:26 - Registered nodes with message boxes: ["default"]
:running
```

3) Make calls using the convencient API at `clojang.component.core`:

```clj
[clojang.component.repl] λ=> (core/node-name)
"clojang@spacemac"
[clojang.component.repl] λ=> (core/node)
#object[com.ericsson.otp.erlang.OtpNode 0x5afcf7e7 "clojang@spacemac"]
[clojang.component.repl] λ=> (core/mbox)
#object[com.ericsson.otp.erlang.OtpMbox 0x30cccc6c "com.ericsson.otp.erlang.OtpMbox@d8fa7fbe"]
[clojang.component.repl] λ=> (core/mbox-name)
"default"
```

Note that this example component makes use of the same JVM options that the
Clojang agent does, as defined in the `project.clj` (`:dev` profile, `:jvm-opts`).


## Donating [&#x219F;](#contents)

A donation account for supporting development on this project has been set up
on Liberapay here:

* [https://liberapay.com/clojang/donate](https://liberapay.com/clojang/donate)

You can learn more about Liberapay on its [Wikipedia entry][libera-wiki] or on the
service's ["About" page][libera-about].

[libera-wiki]: https://en.wikipedia.org/wiki/Liberapay
[libera-about]: https://liberapay.com/about/


## License [&#x219F;](#contents)

```
Copyright © 2018 The Clojang Project

Distributed under the Apache License Version 2.0.
```


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojang/component
[travis-badge]: https://travis-ci.org/clojang/component.png?branch=master
[deps]: http://jarkeeper.com/clojang/component
[deps-badge]: http://jarkeeper.com/clojang/component/status.svg
[clojars]: https://clojars.org/clojang/component
[clojars-badge]: https://img.shields.io/clojars/v/clojang/component.svg
[logo]: https://github.com/clojang/resources/blob/master/images/logo-5-250x.png
[logo-large]: https://github.com/clojang/resources/blob/master/images/logo-5-1000x.png
[agent]: https://github.com/clojang/agent
