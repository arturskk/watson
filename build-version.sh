#!/bin/bash

set -e

./mvnw versions:set -DremoveSnapshot
./mvnw versions:commit
RELEASE_VERSION=`./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v '\['`

./mvnw clean package

git add .
git commit -m "Release: $RELEASE_VERSION"
git tag "v$RELEASE_VERSION"

./mvnw build-helper:parse-version versions:set -DnewVersion="${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}-SNAPSHOT"
./mvnw versions:commit
NEXT_SNAPSHOT_VERSION=`./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v '\['`

git add .
git commit -m "Preparing next snapshot: $NEXT_SNAPSHOT_VERSION"

git push
git push --tags
