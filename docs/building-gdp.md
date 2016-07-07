# Building GDP for qemux86-64 using easy-jenkins

<!-- (2016-05-10 11:00 CEST) -->

This document explains how to build from sources the [GENIVI Development Platform](https://at.projects.genivi.org/wiki/x/aoCw) using [Jenkins](https://jenkins.io/).

The actual build is executed inside a [Docker custom build environment](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Custom+Build+Environment+Plugin) which is automatically spun by the Jenkins server.

The following instructions have been tested on:

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.11.0)
* Docker engine: alm-gm-ubu15.alm.mentorg.com (Ubuntu 14.04.4 LTS 64-bit, Docker 1.11.2, Docker Compose 1.6.2)

## Preparation

* Install and configure [easy-jenkins](https://github.com/gmacario/easy-jenkins) - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Create folder `GENIVI`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `GENIVI`
* Type: **Folder**

then click **OK**. Inside the project configuration page, review configuration, then click **OK**.

### Create project `build_gdp`

<!-- (2016-05-10 11:05 CEST) -->

Browse `${DOCKER_URL}/job/GENIVI`, then click **New Item**

* Name: `build_gdp`
* Type: **Freestyle project**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 2
* Source Code Management: Git
  - Repositories
    - Repository URL: `https://github.com/GENIVI/genivi-dev-platform.git`
    - Credentials: - none -
  - Branches to build
    - Branch Specifier (blank for 'any'): `*/master`
  - Repository browser: (Auto)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto`
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
source init.sh qemux86-64

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Perform the actual build
bitbake genivi-dev-platform

# TODO: bitbake genivi-dev-platform-sdk

# EOF
```

* Post-build Actions
  - Archive the artifacts
    - Files to archive: `gdp-src-build/tmp/deploy/images/*/*.manifest`

then click **Save**.

### Build project `build_gdp`

Browse `${JENKINS_URL}/job/GENIVI/job/build_gdp/`, then click **Build Now**.

You may watch the build logs at `${JENKINS_URL}/job/GENIVI/job/build_gdp/lastBuild/console`

<!-- (2016-07-07 16:00 CEST) http://alm-gm-ubu15.alm.mentorg.com:9080/job/GENIVI/job/build_gdp/1/console -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/GENIVI/build_gdp
Cloning the remote Git repository
Cloning repository https://github.com/GENIVI/genivi-dev-platform.git
 > git init /var/jenkins_home/workspace/GENIVI/build_gdp # timeout=10
Fetching upstream changes from https://github.com/GENIVI/genivi-dev-platform.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/GENIVI/genivi-dev-platform.git +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url https://github.com/GENIVI/genivi-dev-platform.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/GENIVI/genivi-dev-platform.git # timeout=10
Fetching upstream changes from https://github.com/GENIVI/genivi-dev-platform.git
 > git -c core.askpass=true fetch --tags --progress https://github.com/GENIVI/genivi-dev-platform.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision b173a7941f1fa18fee03b4140b047c46f8c83fb0 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f b173a7941f1fa18fee03b4140b047c46f8c83fb0
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto from repository ...
$ docker pull gmacario/build-yocto
Unable to find image 'alpine:3.2' locally
3.2: Pulling from library/alpine
bfc185be0245: Pulling fs layer
bfc185be0245: Download complete
bfc185be0245: Pull complete
Digest: sha256:9c6c40abb6a9180603068a413deca450ef13c381974b392a25af948ca87c3c14
Status: Downloaded newer image for alpine:3.2
Docker container 6b2027bdf3364402d2cfa3262e2454181e60dc86a9d7c1787dbbd1d0537ff729 started to host the build
$ docker exec --tty 6b2027bdf3364402d2cfa3262e2454181e60dc86a9d7c1787dbbd1d0537ff729 env
[build_gdp] $ docker exec --tty --user 1000:1000 6b2027bdf3364402d2cfa3262e2454181e60dc86a9d7c1787dbbd1d0537ff729 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
 b="${f%.override}";
 echo "$f" >> "$COPY_REFERENCE_FILE_LOG";
 rel="${b:23}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "$COPY_REFERENCE_FILE_LOG";
 if [[ ! -e $JENKINS_HOME/${rel} || $f = *.override ]]; then
 echo "copy $rel to JENKINS_HOME" >> "$COPY_REFERENCE_FILE_LOG";
 mkdir -p "$JENKINS_HOME/${dir:23}";
 cp -r "${f}" "$JENKINS_HOME/${rel}";
 [[ ${rel} == plugins/*.jpi ]] && touch "$JENKINS_HOME/${rel}.pinned";
 fi
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#1 BUILD_ID=1 BUILD_NUMBER=1 BUILD_TAG=jenkins-GENIVI-build_gdp-1 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/master GIT_COMMIT=b173a7941f1fa18fee03b4140b047c46f8c83fb0 GIT_URL=https://github.com/GENIVI/genivi-dev-platform.git HOME=/var/jenkins_home HOSTNAME=997b00299e13 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=718d63cf95036d01 JAVA_DEBIAN_VERSION=8u91-b14-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u91 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=718d63cf95036d01 JENKINS_SHA=35dcb66a4f4d43d9cbbb8109e57b079e1292ff5e JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins.io JENKINS_VERSION=2.11 JOB_BASE_NAME=build_gdp JOB_NAME=GENIVI/build_gdp LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=fa23d1e20732501c3bb8eeeca423c89ac80ed452 TINI_VERSION=0.9.0 WORKSPACE=/var/jenkins_home/workspace/GENIVI/build_gdp /bin/bash -xe /tmp/hudson4625947790545199077.sh
+ id
uid=1000(jenkins) gid=1000(jenkins) groups=1000(jenkins)
+ pwd
/var/jenkins_home/workspace/GENIVI/build_gdp
...
arsing of 1732 .bb files complete (0 cached, 1732 parsed). 2291 targets, 328 skipped, 0 masked, 0 errors.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "x86_64-poky-linux"
MACHINE           = "qemux86-64"
DISTRO            = "poky-ivi-systemd"
DISTRO_VERSION    = "10.0.0"
TUNE_FEATURES     = "m64 core2"
TARGET_FPU        = ""
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:fc45deac89ef63ca1c44e763c38ced7dfd72cbe1"
meta-ivi          
meta-ivi-bsp      = "HEAD:9d72380c1d50d0c3469b85c3a43fe612b5ee1dd9"
meta-oe           
meta-filesystems  
meta-ruby         = "HEAD:ad6133a2e95f4b83b6b3ea413598e2cd5fb3fd90"
meta-qt5          = "HEAD:ea37a0bc987aa9484937ad68f762b4657c198617"
meta-genivi-dev   = "HEAD:94dce439766f04d80abbf0fec88cfa07800a3573"
meta-rust         = "HEAD:f13ac9d48ae928b761d7be204fa8f877d41e7099"
meta-oic          = "HEAD:69146eaf8bc05c74c377e731b7e16d82854a4659"
meta-erlang       = "HEAD:4d7eacc8e6593934ed5b0c8abc3d3e9dc339d849"
meta-rvi          = "HEAD:de9d548fe35e2cee8688faaae910b4f6f7fea17e"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
...
[42A[JCurrently 1 running tasks (4784 of 4785):
0: genivi-dev-platform-1.3+snapshot-20160706-r0 do_rootfs (pid 5193)
[111A[JNo currently running tasks (4784 of 4785)
[42A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 4785 tasks of which 33 didn't need to be rerun and all succeeded.[0m

Summary: There were 6 WARNING messages shown.
Stopping Docker container after build completion
Archiving artifacts
Notifying upstream projects of job completion
Finished: SUCCESS
```

**NOTE**: A full build starting from an empty workspace takes about 6 hours to complete (Docker Engine running on a quad-core Intel(R) Xeon(TM) CPU X6550 @2.00GHz, 4 GB RAM + 16 GB swap).

![Artifacts of project build_gdp](images/capture-20160707-0933.png)

Browse `${JENKINS_URL}/job/GENIVI/job/build_gdp/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

![Workspace of project build_gdp_ivi9](images/capture-20160707-0934.png)

<!-- EOF -->
