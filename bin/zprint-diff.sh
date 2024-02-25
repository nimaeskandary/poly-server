#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
CD "$SCRIPT_DIR/.." || exit 1

if git rev-parse --verify HEAD >/dev/null 2>&1
then
    against=HEAD
else
    # Initial commit: diff against an empty tree object
    against=$(git hash-object -t tree /dev/null)
fi

FILES=$(git diff --cached --name-only --diff-filter=AM "$against" | grep -E '.clj[cs]?$')
[ -z "$FILES" ] && exit 0

if ! (echo "$FILES" | xargs zprint)
then
    echo
    echo "Error: zprint errors found. Please fix them and retry the commit."
    exit 1
fi

(echo "$FILES" | git add)
