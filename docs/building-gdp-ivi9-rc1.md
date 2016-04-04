# Building GDP-ivi9 RC1 using easy-jenkins

## Introduction

<!-- (2016-04-01 16:00 CEST) -->

This document explains how to build from sources the [GENIVI Demo Platform](https://at.projects.genivi.org/wiki/x/aoCw) using [Jenkins](https://jenkins-ci.org/).

The actual build is executed inside a [Docker custom build environment](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Custom+Build+Environment+Plugin) which is automatically spun by the Jenkins server.

The following instructions have been tested on:

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.10.2)
* Docker engine: alm-gm-ubu15 (Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.3, Docker Compose 1.6.2)

## Preparation

* Install and configure [easy-jenkins](https://github.com/gmacario/easy-jenkins) - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Create folder `GENIVI`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `GENIVI`
* Type: **Folder**

then click **OK**. Inside the project configuration page, review configuration, then click **OK**.

### Create project `build_gdp_ivi9_rc1`

<!-- (2016-04-01 16:07 CEST) -->

Browse `${DOCKER_URL}/job/GENIVI`, then click **New Item**

* Name: `build_gdp_ivi9_rc1`
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
    - Branch Specifier (blank for 'any'): `*/qemux86-64-ivi9-rc1`
  - Repository browser: (Auto)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto-genivi`
    - Advanced...
      - force Pull: Yes
      - Verbose: Yes
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
bitbake genivi-demo-platform

# EOF
```

* Post-build Actions
  - Archive the artifacts
    - Files to archive: `gdp-src-build/tmp/deploy/images/*/*.manifest`

then click **Save**.

### Build project `build_gdp_ivi9_rc1a`

Browse `${JENKINS_URL}/job/GENIVI/job/build_gdp_ivi9_rc1/`, then click **Build Now**.

You may watch the build logs at `${JENKINS_URL}/job/GENIVI/job/build_gdp_ivi9_rc1/lastBuild/console`

<!-- (2016-04-01 16:10 CEST) http://alm-gm-ubu15.solarma.it:9080/job/GENIVI/job/build_gdp_ivi9_rc1/1/console -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_rc1
Cloning the remote Git repository
Cloning repository git://git.projects.genivi.org/genivi-demo-platform.git
 > git init /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_rc1 # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/genivi-demo-platform.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/genivi-demo-platform.git +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url git://git.projects.genivi.org/genivi-demo-platform.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url git://git.projects.genivi.org/genivi-demo-platform.git # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/genivi-demo-platform.git
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/genivi-demo-platform.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/qemux86-64-ivi9-rc1^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/qemux86-64-ivi9-rc1^{commit} # timeout=10
Checking out Revision e8d3761271cf25ead9e404e4cf4fff08b45d40ff (refs/remotes/origin/qemux86-64-ivi9-rc1)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f e8d3761271cf25ead9e404e4cf4fff08b45d40ff
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto-genivi from repository ...
$ docker pull gmacario/build-yocto-genivi
Using default tag: latest
latest: Pulling from gmacario/build-yocto-genivi
...
[156A[JCurrently 1 running tasks (4442 of 4447):
0: python-pyqt-5.3.1-r1 do_package_write_rpm (pid 30551)
[99A[JNo currently running tasks (4442 of 4447)
[42A[JNo currently running tasks (4443 of 4447)
[42A[JNo currently running tasks (4444 of 4447)
[42A[JNo currently running tasks (4445 of 4447)
[42A[JNo currently running tasks (4446 of 4447)
[42A[JCurrently 1 running tasks (4446 of 4447):
0: genivi-demo-platform-1.3+snapshot-20160401-r0 do_rootfs (pid 31227)
[113A[JNo currently running tasks (4446 of 4447)
[42A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 4447 tasks of which 22 didn't need to be rerun and all succeeded.[0m

Summary: There were 28 WARNING messages shown.
Stopping Docker container after build completion
$ docker kill 952e99c8486c5e465c8248d7e95eae5db31be60e6da9ad60552fd5795734002d
952e99c8486c5e465c8248d7e95eae5db31be60e6da9ad60552fd5795734002d
$ docker rm --force 952e99c8486c5e465c8248d7e95eae5db31be60e6da9ad60552fd5795734002d
952e99c8486c5e465c8248d7e95eae5db31be60e6da9ad60552fd5795734002d
Archiving artifacts
Notifying upstream projects of job completion
Finished: SUCCESS
```

**NOTE**: A full build starting from an empty workspace takes about 6 hours to complete (Docker Engine running on a quad-core Intel(R) Xeon(TM) CPU X6550 @2.00GHz, 4 GB RAM + 16 GB swap).

![Artifacts of project build_gdp_ivi9_rc1](images/TODO.png)

Browse `${JENKINS_URL}/job/GENIVI/job/build_gdp_ivi9_rc1/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

![Workspace of project build_gdp_ivi9_beta](images/TODO.png)

<!-- EOF -->
