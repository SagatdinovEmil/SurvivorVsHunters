#!/usr/bin/env bash
set -e

mvn package -f ./pom.xml
scp target/SurvVsHunts-1.0.jar mc@ateam.undo.it:/home/mc/speedrunnners2/plugins/