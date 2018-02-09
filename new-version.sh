#!/bin/bash

set -e

RV=$1
SV=$2

if [ "x$RV" = "x" ] || [ "x$SV" = "x" ]; then
    echo "Podaj wersje:"
    echo "  new-version.sh wersja_release wersja_snapshot"
    exit 1
fi

./mvnw versions:set -DnewVersion="$RV"
./mvnw versions:commit

./mvnw clean install

git add .
git commit -m "Release: $RV"
git tag "v$RV"

./mvnw versions:set -DnewVersion="$SV"
./mvnw versions:commit

git add .
git commit -m "Snapshot: $SV"

git push
git push --tags
