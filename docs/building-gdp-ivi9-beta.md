# Building GDP ivi9 beta using easy-jenkins

**WORK-IN-PROGRESS**

## Introduction

<!-- (2016-02-23 16:00 CET) -->

This document explains how to build from sources the [GENIVI Demo Platform](http://projects.genivi.org/genivi-demo-platform/home) using Jenkins. 

Rather then performing the build inside a Jenkins dumb slave, the build is performed inside a custom Docker container.

The instructions inside this document were tested on

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.10.0)
* Docker engine: mv-linux-powerhorse (Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.0, Docker Compose 1.6.0)

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Create project `build_gdp_ivi9_beta`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `build_gdp_ivi9_beta`
* Type: **Freestyle project**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 2
* Source Code Management: Git
  - Repositories
    - Repository URL: `git://git.projects.genivi.org/genivi-demo-platform.git`
    - Credentials: - none -
  - Branches to build
    - Branch Specifier (blank for 'any'): `*/qemux86-64-ivi9-beta`
  - Repository browser: (Auto)
  - Additional Behaviours
    - Disable submodules processing: No
    - Recursively update submodules: **Yes**
    - Update tracking submodules to tip of branch: No (TODO?)
    - Path of the reference repo to use during submodule update: (none)
    - Timeout (in minutes) for submodules operations: (none)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto-genivi`
* Build
  - Execute shell
    - Command
```
#!/bin/bash -xe

# DEBUG
id
pwd
ls -la
printenv | sort

# Configure git
git config --global user.name "easy-jenkins"
git config --global user.email "$(whoami)@$(hostname)"

# Configure the build
source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Perform the actual build
bitbake genivi-demo-platform    # ERROR in navit_svn.bb, do_compile

# EOF
```

then click **Save**.

### Build project `build_gdp_ivi9_beta`

<!-- (2016-02-23 09:27 CET) -->

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_gdp_ivi9_beta/lastBuild/console`

<!-- (2016-02-23 16:00 CET) -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/genivi-demo-platform
 > git init /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/dev-udooneo-jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/dev-udooneo-jethro^{commit} # timeout=10
Checking out Revision c83152c2cc1e87d0f6adfcc45358331635f34618 (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f c83152c2cc1e87d0f6adfcc45358331635f34618
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto from repository ...
$ docker pull gmacario/build-yocto
...
TODO
```

**NOTE**: A full build takes about TODO hours to complete (Docker Engine running on a dual-core AMD Opteron(TM) Processor 6276 CPU X5450 @2300 MHz, 4 GB RAM).

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

TODO: Attach screenshot

<!-- EOF -->
