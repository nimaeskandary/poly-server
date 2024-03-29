This is a playground to test building a clojure monorepo managed by the polylith tool. Goals are to have multiple
build artifacts, e.g. uberjars for an http server and lambda handlers, that are able to share common code

The Polylith documentation can be found here:

- The [high-level documentation](https://polylith.gitbook.io/polylith)
- The [poly tool documentation](https://cljdoc.org/d/polylith/clj-poly/CURRENT)

# Table of Contents

* [Development](#development)
  * [Dependencies](#dependencies)
  * [Getting things working with your IDE](#getting-things-working-with-your-ide)
  * [Running locally](#running-locally)
* [Build](#build)
* [Polylith cheatsheet](#polylith-cheatsheet)

# Development

## Dependencies

* java - https://adoptium.net/installation/
* clojure - `brew install clojure/tools/clojure`
* docker - https://docs.docker.com/desktop/install/mac-install/
* poly - `brew install polyfy/polylith/poly`
* clj-kondo - `brew install borkdude/brew/clj-kondo`
* zprint - `brew install --cask zprint`
* cdktf - `brew install cdktf`

## Getting things working with your IDE

* see https://cljdoc.org/d/polylith/clj-poly/0.2.19/doc/development
* some notes for Cursive
  * select the Aliases `dev, test, build` in the Clojure Deps tool window to resolve all dependencies you have to work with
  * Go to `Settings→Languages & Frameworks→Clojure→Project Specific Options and check "Resolve over whole project"` to make things resolve correctly across different subprojects with our root `deps.edn` setup

## Running locally

* start a clojure repl (ie `clj -A:dev:test:build`)
* the dev system will start on its own via loading `development/src/user.clj`
* go to http://localhost:9000/api-docs/ to see endpoints and send requests from your browser

## Linting and formatting

You can create a pre commit hook to lint and format files before they are committed

* `touch .git/hooks/pre-commit`
* add contents
```bash
#!/usr/bin/env bash
if ! (./bin/zprint-diff.sh)
then
  exit 1
fi
./bin/kondo-diff.sh
exit $?
```
* `chmod +x .git/hooks/pre-commit`

You can force a commit through if you want with the `--no-verify` flag

# Build

## http server uberjar

* `bin/build.sh http-server`
* run it `java -jar projects/http-server/target/http-server.jar`

## lambda uberjar

* `bin/build.sh lambda`

# Polylith cheatsheet

Polylith in a nutshell is an opinionated tool to help manage a monorepo. The guardrails in place help the monorepo share
libraries of common code across multiple build artifacts, e.g. webservers, lambdas, cli tools. At a high level, 
individual libs of common code are called `components` by the tool. Each component exposes an `interface` package. Outside
of each component, e.g. from another component, you may only access what is inside the `interface` package of another
component, everything else is private. The root `deps.edn` is so your local development environment and REPL can
resolve all the code in the repo. In `projects/...` will be `deps.edn` files for individual build targets, and will only
include components required to build that project.


Running `poly check` will give you warnings like your project depends on something else in the monorepo that it is not using,
or errors like you are trying to use something from a namespace that is not in an interface package

### testing

* `poly test`

This can include options like what bases, components, or projects you want to run tests for, with nothing will run tests
for bases and components your projects in `projects/` depend on

If you use Intellij with the Cursive plugin, you can also execute tests like normal in your REPL

### Libraries

* `poly libs` - see project dependencies
* `poly libs :outdated` - see outdated dependencies
* `poly libs :update` - update dependencies

### Creating a component

building block that encapsulates a specific domain or part of the system

* `poly create component name:user`
* add component to `<root>/deps.edn`
```clojure
{:aliases  {:dev {:extra-paths ["development/src"]
                  ;; to extra dev deps
                  :extra-deps {poly/user {:local/root "components/user"}}}}
            ;; to extra test paths
            :test {:extra-paths ["components/user/test"]}} 
```

### creating a base

building block that exposes a public API to the outside world, e.g., external systems and users

* `poly create base name:web`
* add base to `<root>/deps.edn`
```clojure
:aliases  {:dev {:extra-paths ["development/src"]
                  ;; to extra dev deps
                  :extra-deps {poly/web {:local/root "bases/cli"}}}
            ;; to extra test paths
            :test {:extra-paths ["bases/cli/test"]}}
```

### creating a project

used to build a deployable artifact

* `poly create project name:backend`
* add project to `<root>/workspace.edn`
```clojure
{:projects {"backend" {:alias "backend"}}}
```
* add components and bases to `<root>/projects/backend/deps.edn` e.g.
```clojure
{:deps {poly/user {:local/root "../../components/user"} 
        poly/web  {:local/root "../../bases/cli"}}}
```
