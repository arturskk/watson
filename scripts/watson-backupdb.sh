#!/bin/bash

pushd /opt/watson
echo "Kopiowanie event logu watson"
TARGET="events.log.$(date '+%Y%m%d-%H%M%S')"
cp -a events.log "backup/$TARGET"

echo "Event log skopiowany do /opt/watson/backup/$TARGET"
ls -l "/opt/watson/backup/$TARGET"