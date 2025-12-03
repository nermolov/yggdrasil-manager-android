#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

echo "-- Building go library"
$SCRIPT_DIR/build-and-copy-yggdrasil-library.sh

echo "-- Building Android app"
cd $SCRIPT_DIR/..
./gradlew assembleYggdrasil
