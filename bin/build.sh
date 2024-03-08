#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
CD "$SCRIPT_DIR/.." || exit 1

clojure -T:build uberjar :project "$1"
