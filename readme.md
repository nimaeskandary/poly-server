<img src="logo.png" width="30%" alt="Polylith" id="logo">

The Polylith documentation can be found here:

- The [high-level documentation](https://polylith.gitbook.io/polylith)
- The [poly tool documentation](https://cljdoc.org/d/polylith/clj-poly/CURRENT)
- The [RealWorld example app documentation](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app)

You can also get in touch with the Polylith Team on [Slack](https://clojurians.slack.com/archives/C013B7MQHJQ).

<h1>poly-server</h1>

# Table of Contents

* [Development](#development)
  * [Dependencies](#dependencies)
  * [Getting things working with your IDE](#getting-things-working-with-your-ide)
  * [Running locally](#running-locally)
  * [Polylith commands](#polylith-commands)

# Development

## Dependencies

* docker
* poly - `brew install polyfy/polylith/poly`
* psql - `brew install postgresql`
* clj-kondo - `brew install borkdude/brew/clj-kondo`
* zprint - `brew install --cask zprint`

## Getting things working with your IDE

* see https://cljdoc.org/d/polylith/clj-poly/0.2.19/doc/development
* some notes for Cursive
  * select the Aliases `dev, test`
  * Go to `Settings→Languages & Frameworks→Clojure→Project Specific Options and check "Resolve over whole project"` to make things resolve correctly across different subprojects with our root `deps.edn` setup

## Running locally

* run `bin/docker-up.sh`
* start a clojure nrepl with opts `-A:dev:test`
* the dev system will start on its own via loading `components/development/user.clj`
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

## Polylith commands

### Creating a component

building block that encapsulates a specific domain or part of the system

* `poly shell`
* `create component name:user`
* add component to `<root>/deps.edn`
```clojure
{:aliases  {:dev {:extra-paths ["development/src"]
                  ;; to extra dev deps
                  :extra-deps {poly/user {:local/root "components/user"}}}}
            ;; to extra test paths
            :test {:extra-paths ["components/user/test"]}} 
```
* `info`

### creating a base

building block that exposes a public API to the outside world, e.g., external systems and users

* `poly shell`
* `create base name:web`
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

* `poly shell`
* `create project name:backend`
* add project to `<root>/workspace.edn`
```clojure
{:projects {"backend" {:alias "backend"}}}
```
* add components and bases to `<root>/projects/backend/deps.edn`
```clojure
{:deps {poly/user {:local/root "../../components/user"} 
        poly/web  {:local/root "../../bases/cli"}}}
```
