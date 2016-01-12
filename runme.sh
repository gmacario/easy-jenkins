#!/bin/bash

[[ "${VM}" = "" ]] && VM=easy-jenkins

set -e

docker-machine ls

if docker-machine ls | grep ${VM} >/dev/null; then
    echo "WARNING: Docker machine ${VM} exists, skipping docker-machine create"
else
    docker-machine create --driver virtualbox \
      --virtualbox-cpu-count "2" \
      --virtualbox-disk-size "50000" \
      --virtualbox-memory "3048" \
      ${VM}
fi
if docker-machine status ${VM} | grep -v Running; then
    docker-machine start ${VM}
fi

docker-machine env ${VM}

eval $(docker-machine env ${VM})
docker-compose up -d

# EOF
