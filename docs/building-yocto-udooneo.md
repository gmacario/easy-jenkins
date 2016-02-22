# Building a Yocto distribution for the UDOO Neo

**WORK-IN-PROGRESS**

This document explains how to buil from sources a [Yocto](https://www.yoctoproject.org/)-based distribution for the [UDOO Neo](http://www.udoo.org/udoo-neo/) using [easy-jenkins](https://github.com/gmacario/easy-jenkins).

The following instructions were tested on

* Docker client: mac-tizy (MacBook Pro, Docker Toolbox 10.0 on OS X 10.11.3)
* Docker engine: mv-linux-powerhorse (Docker 1.10.0 on Ubuntu 14.04.4 LTS 64-bit)

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.

## Step-by-step instructions

### Configure project `build_yocto_udooneo`

Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_yocto_udooneo`
  - Type: **Freestyle project**

  then click **OK**.
  
Inside the project configuration page, fill-in the following information:
  - Discard Old Builds: Yes
    - Strategy: Log Rotation
      - Days to keep builds: (none)
      - Max # of builds to keep: 2
  - Source Code Management: Git
    - Repositories
      - Repository URL: `https://github.com/gmacario/genivi-demo-platform`
      - Credentials: - none -
      - Branches to build
        - Branch Specifier (blank for `any`): `*/dev-udooneo-jethro` (TODO?)
      - Repository browser: (Auto)
  - Build Environment
    - Build inside a Docker container: Yes
      - Docker image to use: Pull docker image from repository
        - Image id/tag: `gmacario/build-yocto`
  - Build
    - Execute shell
      - Command

```
#!/bin/bash -xe

# DEBUG
id
pwd
ls -la
printenv

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
touch conf/sanity.conf

bitbake core-image-minimal
# bitbake -vvv core-image-minimal # ==> FAIL core-image-minimal.bb, task do_rootfs

# bitbake udoo-image-full-cmdline
# bitbake genivi-demo-platform

# EOF
```
  
  then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-22 18:57 CET) -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

Result: TODO

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
Checking out Revision 1ff20e1c8d9739333a1df26453de5a88102a8bd8 (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 1ff20e1c8d9739333a1df26453de5a88102a8bd8
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto from repository ...
$ docker pull gmacario/build-yocto
...
TODO
```

<!-- EOF -->
