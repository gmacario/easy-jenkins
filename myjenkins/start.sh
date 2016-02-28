#!/bin/bash

sleep 5
service docker start
gosu jenkins /usr/local/bin/jenkins.sh

# EOF
