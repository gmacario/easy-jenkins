#!/bin/bash

MACHINE=easy-jenkins

set -e

docker-machine ls

if docker-machine ls | grep ${MACHINE} >/dev/null; then
    echo "WARNING: Docker machine ${MACHINE} exists, skipping docker-machine create"
else
    docker-machine create --driver virtualbox \
      --virtualbox-cpu-count "2" \
      --virtualbox-disk-size "50000" \
      --virtualbox-memory "3048" \
      ${MACHINE}
fi

docker-machine env ${MACHINE}

# EOF
