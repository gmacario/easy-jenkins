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

# -----------------------------------------------------------------------------
# Check version "maj.min.pat"
#
# Parameters:
#   * have_maj_min_pat: Installed version - example: "0.5.6"
#   * want_maj_min_pat: Required version  - example: "0.6.0"
#
# Return:
#   * 0 if installed version is >= than required version
#   * 1 otherwise
#
is_version_ok () {
  result=1
  # echo "DEBUG: line $LINENO: is_version_ok(have_maj_min_pat=$1, want_maj_min_pat=$2)"

  have_maj=$(echo $1 | sed -e 's/\..*//')
  have_min_pat=$(echo $1 | sed -e 's/^[0-9]*\.//')
  have_min=$(echo $have_min_pat | sed -e 's/\..*//')
  have_pat=$(echo $have_min_pat | sed -e 's/^[0-9]*\.//')
  # echo "DEBUG: line $LINENO: have_maj=$have_maj, have_min=$have_min, have_pat=$have_pat"

  want_maj=$(echo $2 | sed -e 's/\..*//')
  want_min_pat=$(echo $2 | sed -e 's/^[0-9]*\.//')
  want_min=$(echo $want_min_pat | sed -e 's/\..*//')
  want_pat=$(echo $want_min_pat | sed -e 's/^[0-9]*\.//')
  # echo "DEBUG: line $LINENO: want_maj=$want_maj, want_min=$have_min, want_pat=$want_pat"

  if [ $have_maj -gt $want_maj ]; then
    # echo "DEBUG: line $LINENO: Case have_maj $have_maj > $want_maj ==> OK"
    result=0
  elif [ $have_maj -eq $want_maj ]; then
    # echo "DEBUG: line $LINENO: Case have_maj $have_maj == $want_maj"
    if [ $have_min -gt $want_min ]; then
      # echo "DEBUG: line $LINENO: Case have_min $have_min > $want_min ==> OK"
      result=0
    elif [ $have_min -eq $want_min ]; then
      # echo "DEBUG: line $LINENO: Case have_min $have_min == 5"
      if [ $have_pat -ge $want_pat ]; then
        # echo "DEBUG: line $LINENO: Case have_pat $have_pat >= $want_pat ==> OK"
        result=0
      fi
    fi
  fi
  # echo "DEBUG: line $LINENO: Return result=$result"
  return $result
}

# -----------------------------------------------------------------------------

set -e

# docker-machine
if ! which docker-machine >/dev/null; then
    echo "WARNING: Cannot find docker-machine - assuming environment variables are already defined"
    USE_DOCKER_MACHINE=false
fi

if [[ "${VM}" = "" ]]; then
    # If VM is not defined, try setting it to active docker-machine
    # otherwise just pick a default name
    if ${USE_DOCKER_MACHINE}; then
        VM=$(docker-machine active) || VM=easy-jenkins
    fi
fi
[[ "${VM_NUM_CPUS}" = "" ]] && VM_NUM_CPUS=2
[[ "${VM_MEM_SIZEMB}" = "" ]] && VM_MEM_SIZEMB=2048
[[ "${VM_DISK_SIZEMB}" = "" ]] && VM_DISK_SIZEMB=50000
[[ "${USE_DOCKER_MACHINE}" = "" ]] && USE_DOCKER_MACHINE=true

# Check prerequisites

# docker-machine
if ${USE_DOCKER_MACHINE}; then
  result=$(docker-machine --version)
  # echo "DEBUG: line $LINENO: result=$result"
  have_maj_min_pat=$(echo $result | sed -e 's/^.*version //' | sed -e 's/\,.*$//')
  # echo "DEBUG: line $LINENO: have_maj_min_pat=$have_maj_min_pat"
  want_maj_min_pat="0.6.0"
  # echo "DEBUG: line $LINENO: want_maj_min_pat=$want_maj_min_pat"
  if ! is_version_ok $have_maj_min_pat $want_maj_min_pat; then
      echo "ERROR: Should install docker-machine >= $want_maj_min_pat (have $have_maj_min_pat)"
      exit 1
  fi
fi    # if ${USE_DOCKER_MACHINE}

# docker
if ! which docker >/dev/null; then
    echo "ERROR: Cannot find docker - Please see https://docs.docker.com/engine/installation/"
    exit 1
fi
result=$(docker --version)
# echo "DEBUG: line $LINENO: result=$result"
have_maj_min_pat=$(echo $result | sed -e 's/^.*version //' | sed -e 's/\,.*$//')
# echo "DEBUG: line $LINENO: have_maj_min_pat=$have_maj_min_pat"
want_maj_min_pat="18.02.0"
# echo "DEBUG: line $LINENO: want_maj_min_pat=$want_maj_min_pat"
if ! is_version_ok $have_maj_min_pat $want_maj_min_pat; then
    echo "ERROR: Should install docker >= $want_maj_min_pat (have $have_maj_min_pat)"
    exit 1
fi

# docker-compose
if ! which docker-compose >/dev/null; then
    echo "ERROR: Cannot find docker-compose - Please see https://docs.docker.com/compose/install/"
    exit 1
fi
result=$(docker-compose --version)
# echo "DEBUG: line $LINENO: result=$result"
have_maj_min_pat=$(echo $result | sed -e 's/^.*version //' | sed -e 's/\,.*$//')
# echo "DEBUG: line $LINENO: have_maj_min_pat=$have_maj_min_pat"
want_maj_min_pat="1.18.0"
# echo "DEBUG: line $LINENO: want_maj_min_pat=$want_maj_min_pat"
if ! is_version_ok $have_maj_min_pat $want_maj_min_pat; then
    echo "ERROR: Should install docker-compose >= $want_maj_min_pat (have $have_maj_min_pat)"
    exit 1
fi

if ${USE_DOCKER_MACHINE}; then
    result=$(docker-machine --version)
    # echo "DEBUG: line $LINENO: result=$result"
    have_maj_min_pat=$(echo $result | sed -e 's/^.*version //' | sed -e 's/\,.*$//')
    # echo "DEBUG: line $LINENO: have_maj_min_pat=$have_maj_min_pat"
    want_maj_min_pat="0.6.0"
    # echo "DEBUG: line $LINENO: want_maj_min_pat=$want_maj_min_pat"
    if ! is_version_ok $have_maj_min_pat $want_maj_min_pat; then
      echo "ERROR: Should install docker-machine >= $want_maj_min_pat (have $have_maj_min_pat)"
      exit 1
    fi

    # docker-machine ls
    if docker-machine ls | grep -w ${VM} >/dev/null; then
        echo "INFO: Docker machine ${VM} exists, skipping docker-machine create"
    else
        echo "INFO: Creating VirtualBox VM ${VM} (cpu:${VM_NUM_CPUS}, memory:${VM_MEM_SIZEMB} MB, disk:${VM_DISK_SIZEMB} MB)"
        docker-machine create --driver virtualbox \
          --virtualbox-cpu-count "${VM_NUM_CPUS}" \
          --virtualbox-memory "${VM_MEM_SIZEMB}" \
          --virtualbox-disk-size "${VM_DISK_SIZEMB}" \
          ${VM}
    fi
    echo "INFO: Using Docker machine ${VM}"
    if docker-machine status ${VM} | grep -v Running >/dev/null; then
        docker-machine start ${VM}
    fi

    # docker-machine env ${VM}
    eval $(docker-machine env ${VM})
fi    # if ${USE_DOCKER_MACHINE} ...

if [ "${CASC_JENKINS_CONFIG}" = "" ]; then
    CASC_JENKINS_CONFIG="casc_configs/$(hostname).yaml"
    if [ ! -e "${CASC_JENKINS_CONFIG}" ]; then
        echo "WARNING: Could not find ${CASC_JENKINS_CONFIG} -- using default"
        CASC_JENKINS_CONFIG="casc_configs/default.yaml"
    fi
    export CASC_JENKINS_CONFIG="/var/jenkins_home/${CASC_JENKINS_CONFIG}"
fi
echo "DEBUG: CASC_JENKINS_CONFIG=${CASC_JENKINS_CONFIG}"

# echo "DEBUG: Inspecting environment variables"
# printenv | sort

docker-compose up -d

# Wait a reasonable time to make sure initialAdminPassword is generated
sleep 30

if ${USE_DOCKER_MACHINE}; then
    echo "INFO: Run the following command to configure your shell:"
    echo "INFO: eval \$(docker-machine env ${VM})"
    echo "INFO: Browse http://$(docker-machine ip ${VM}):9080/ to access the Jenkins dashboard"
fi    # if ${USE_DOCKER_MACHINE} ...

INITIAL_PASS=$(docker-compose exec -T myjenkins sh -c \
    "[ -e /var/jenkins_home/secrets/initialAdminPassword ] && cat /var/jenkins_home/secrets/initialAdminPassword")

if [ "${INITIAL_PASS}" != "" ]; then
    echo "INFO: Initial administrator password: ${INITIAL_PASS}"
fi

# EOF
