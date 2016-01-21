#!/bin/bash
# =============================================================================
# Project: easy-jenkins
#
# Description: Top-level script to start the Docker containers
#
# Usage examples:
#
# 1. Create Docker machine with default configuration
#     $ ./runme.sh
#
# 2. Customize Docker machine VM configuration
#     $ VM=test-vm VM_NUM_CPUS=3 VM_MEM_SIZEMB=1024 VM_DISK_SIZEMB=10000 \
#       ./runme.sh
# =============================================================================

[[ "${VM}" = "" ]] && VM=easy-jenkins
[[ "${VM_NUM_CPUS}" = "" ]] && VM_NUM_CPUS=2
[[ "${VM_MEM_SIZEMB}" = "" ]] && VM_MEM_SIZEMB=3048
[[ "${VM_DISK_SIZEMB}" = "" ]] && VM_DISK_SIZEMB=50000

F_HAVE_DOCKER_MACHINE=false

set -e

if which docker-machine >/dev/null; then
    F_HAVE_DOCKER_MACHINE=true
else
    echo "WARNING: Could not find docker-machine - Please set environment variables manually"
fi
if ! which docker-compose >/dev/null; then
    echo "ERROR: Must install docker-compose"
    exit 1
fi

if "${F_HAVE_DOCKER_MACHINE}"; then
    # docker-machine ls
    if docker-machine ls | grep ${VM} >/dev/null; then
        echo "WARNING: Docker host ${VM} exists, skipping docker-machine create"
    else
        echo "INFO: Creating VirtualBox VM ${VM} (cpu:${VM_NUM_CPUS}, memory:${VM_MEM_SIZEMB} MB, disk:${VM_DISK_SIZEMB} MB)"
        docker-machine create --driver virtualbox \
          --virtualbox-cpu-count "${VM_NUM_CPUS}" \
          --virtualbox-memory "${VM_MEM_SIZEMB}" \
          --virtualbox-disk-size "${VM_DISK_SIZEMB}" \
          ${VM}
    fi
    if docker-machine status ${VM} | grep -v Running >/dev/null; then
        docker-machine start ${VM}
    fi
    # docker-machine env ${VM}
    eval $(docker-machine env ${VM})
fi

docker-compose up -d

if "${F_HAVE_DOCKER_MACHINE}"; then
    echo "INFO: Browse http://$(docker-machine ip ${VM}):9080/ to access the Jenkins dashboard"
    echo "INFO: Run the following command to configure your shell:"
    echo "INFO: eval \$(docker-machine env ${VM})"
else
    JENKINS_URL="http://localhost:9080/"
    echo "INFO: Browse ${JENKINS_URL} to access the Jenkins dashboard"
fi

# EOF
