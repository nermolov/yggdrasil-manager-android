#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd $SCRIPT_DIR/../../yggdrasil-manager

./contrib/mobile/build -a

cd $SCRIPT_DIR/..

cp ../yggdrasil-manager/{yggdrasil.aar,yggdrasil-sources.jar} ./app/libs
