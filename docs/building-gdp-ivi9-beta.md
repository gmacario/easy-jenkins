## Introduction

<!-- (2016-02-19 16:38 CET) -->

This document explains how to build from sources the [GENIVI Demo Platform](http://projects.genivi.org/genivi-demo-platform/home) using Jenkins.

This procedure may be used as a regression test suite for the [gmacario/easy-jenkins](https://github.com/gmacario/easy-jenkins) project.

The instructions inside this document were tested on

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.10.0)
* Docker engine: Oracle VirtualBox VM created by Docker Toolbox

## Preparation

Refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.

## Create project `build_gdp_ivi9_beta`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `build_gdp_ivi9_beta`
* Type: **Freestyle project**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 5
* Source Code Management: Git
  - Repositories
    - Repository URL: `git://git.projects.genivi.org/genivi-demo-platform.git`
    - Credentials: - none -
  - Branches to build
    - Branch Specifier (blank for 'any'): `*/qemux86-64-ivi9-beta`
  - Repository browser: (Auto)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto`
* Build
  - Execute shell
    - Command
```
# DEBUG
id
ls -la
docker info
docker images
docker ps
```
  - Execute shell
    - Command
```
# Actual build steps

echo TODO
# bash -xec "source init.sh && bitbake genivi-demo-platform"
```

then click **Save**.

## Build project `build_gdp_ivi9_beta`

<!-- (2016-02-04 12:20 CET) -->

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_gdp_ivi9_beta/lastBuild/console`

<!-- (2016-02-04 17:06 CET) -->

```
TODO
```

**NOTE**: A full build takes about 5 hours to complete on a dual-Xeon(R) CPU X5450 @3.00 GHz and 16 GB RAM.

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

TODO: Attach screenshot

<!-- EOF -->
