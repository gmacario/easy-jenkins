**WORK-IN-PROGRESS**

# Building Yocto distro for UDOO Neo inside easy-jenkins

This document explains how to buil from sources a Yocto-based distribution for the UDOO Neo.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

The following instructions were tested on

* Docker client: mac-tizy (MacBook Pro, OS X)
* Docker engine: Oracle VirtualBox VM created by Docker Toolbox

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
        - Image id/tag: `gmacario/build-yocto-new`
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

bitbake genivi-core-image-minimal
# bitbake genivi-demo-platform
```
  
  then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-21 14:06 CET) -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

Result: TODO

```
TODO
```

<!-- EOF -->
