## Introduction

<!-- (2016-02-02 07:58 CET) -->

This document explains how to build from sources my fork of the [GENIVI Demo Platform](http://projects.genivi.org/genivi-demo-platform/home) using Jenkins.

This procedure may be used as a regression test suite for the [gmacario/easy-jenkins](https://github.com/gmacario/easy-jenkins) project.

The following instructions were tested on

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.10.0)
* Docker engine: mv-linux-powerhorse (Ubuntu 14.04.3 LTS 64-bit, Docker Engine 1.10.0)

## Preparation

Refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.

## Build project `build_gdp`

<!-- (2016-02-04 12:20 CET) -->

Browse `${JENKINS_URL}/job/build_gdp/`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_gdp/lastBuild/console`

<!-- (2016-02-04 17:06 CET) -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building remotely on build-yocto-slave (yocto) in workspace /home/jenkins/workspace/build_gdp
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/genivi-demo-platform
...
NOTE: Running task 4393 of 4394 (ID: 7, /home/jenkins/workspace/build_gdp/gdp-src-build/../meta-genivi-demo/recipes-demo-platform/images/genivi-demo-platform.bb, do_rootfs)
NOTE: recipe genivi-demo-platform-1.3+snapshot-20160205-r0: task do_rootfs: Started
NOTE: recipe genivi-demo-platform-1.3+snapshot-20160205-r0: task do_rootfs: Succeeded
NOTE: Running noexec task 4394 of 4394 (ID: 11, /home/jenkins/workspace/build_gdp/gdp-src-build/../meta-genivi-demo/recipes-demo-platform/images/genivi-demo-platform.bb, do_build)
NOTE: Tasks Summary: Attempted 4394 tasks of which 22 didn't need to be rerun and all succeeded.

Summary: There were 16 WARNING messages shown.
Notifying upstream projects of job completion
Finished: SUCCESS
```

**NOTE**: A full build takes about 5 hours to complete on a dual-Xeon(R) CPU X5450 @3.00 GHz and 16 GB RAM.

Browse `${JENKINS_URL}/job/build_gdp/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

![Build results of project build_gdp](images/capture-20160205-1709.png)

<!-- EOF -->
