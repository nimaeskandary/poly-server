#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
CD "$SCRIPT_DIR/.." || exit 1

# kondo lint changed files
# https://github.com/clj-kondo/clj-kondo/blob/master/doc/ci-integration.md

if git rev-parse --verify HEAD >/dev/null 2>&1
then
    against=HEAD
else
    # Initial commit: diff against an empty tree object
    against=$(git hash-object -t tree /dev/null)
fi

if ! (git diff --cached --name-only --diff-filter=AM "$against" | grep -E '.clj[cs]?$' | xargs -r clj-kondo --lint)
then
    echo
    echo "Error: new clj-kondo errors found. Please fix them and retry the commit."
    exit 1
fi

exec git diff-index --check --cached "$against" --
