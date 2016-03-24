#!/bin/bash

sleep 5

# To use host Docker daemon, comment out line `service docker start`
# and run container with `--volume=/var/run/docker.sock:/var/run/docker.sock`
# service docker start

gosu jenkins /usr/local/bin/jenkins.sh

# EOF
