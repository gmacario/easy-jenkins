**WORK-IN-PROGRESS**

## Introduction

<!-- (2016-02-23 09:22 CET) -->

This document explains how to build from sources the [GENIVI Demo Platform](http://projects.genivi.org/genivi-demo-platform/home) using Jenkins. 

Rather then performing the build inside a Jenkins slave, the build is done inside a Docker container.

This procedure may be used as a regression test suite for the [gmacario/easy-jenkins](https://github.com/gmacario/easy-jenkins) project.

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
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto-genivi`
* Build
  - Execute shell
    - Command
```
# DEBUG
id
pwd
ls -la
printenv
```
  - Execute shell
    - Command
```
#!/bin/bash -xe

# Actual build steps

source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

bitbake genivi-demo-platform
```

then click **Save**.

### Build project `build_gdp_ivi9_beta`

<!-- (2016-02-23 09:27 CET) -->

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_gdp_ivi9_beta/lastBuild/console`

<!-- (2016-02-23 13:00 CET) -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url git://git.projects.genivi.org/genivi-demo-platform.git # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/genivi-demo-platform.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/genivi-demo-platform.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/qemux86-64-ivi9-beta^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/qemux86-64-ivi9-beta^{commit} # timeout=10
Checking out Revision 6e50965700f98572eaa731e426d561b1b5031c87 (refs/remotes/origin/qemux86-64-ivi9-beta)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 6e50965700f98572eaa731e426d561b1b5031c87
 > git rev-list 6e50965700f98572eaa731e426d561b1b5031c87 # timeout=10
Docker container c43a9c84ce53ef55cedb9943f2e2870c378865ad7ad2b56cb8ce5354396f4028 started to host the build
$ docker exec --tty c43a9c84ce53ef55cedb9943f2e2870c378865ad7ad2b56cb8ce5354396f4028 env
[build_gdp_ivi9_beta] $ docker exec --tty --user 0:0 c43a9c84ce53ef55cedb9943f2e2870c378865ad7ad2b56cb8ce5354396f4028 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
 b="${f%.override}";
 echo "$f" >> "$COPY_REFERENCE_FILE_LOG";
 rel="${b:23}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "$COPY_REFERENCE_FILE_LOG";
 if [[ ! -e /var/jenkins_home/${rel} || $f = *.override ]]; then
 echo "copy $rel to JENKINS_HOME" >> "$COPY_REFERENCE_FILE_LOG";
 mkdir -p "/var/jenkins_home/${dir:23}";
 cp -r "${f}" "/var/jenkins_home/${rel}";
 [[ ${rel} == plugins/*.jpi ]] && touch "/var/jenkins_home/${rel}.pinned";
 fi
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#2 BUILD_ID=2 BUILD_NUMBER=2 BUILD_TAG=jenkins-GENIVI-build_gdp_ivi9_beta-2 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=0 GIT_BRANCH=origin/qemux86-64-ivi9-beta GIT_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87 GIT_PREVIOUS_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87 GIT_URL=git://git.projects.genivi.org/genivi-demo-platform.git HOME=/root HOSTNAME=5f8e489fe902 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=7c72346f6ffc0532 JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=7c72346f6ffc0532 JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.1 JOB_NAME=GENIVI/build_gdp_ivi9_beta LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta /bin/sh -xe /tmp/hudson932381446039103116.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta
+ ls -la
...
[391A[JCurrently 8 running tasks (1303 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: weston-1.6.0-r2 do_patch (pid 483)
5: binutils-native-2.24-r0 do_install (pid 476)
6: ruby-native-2.2.1-r0 do_configure (pid 482)
7: librsvg-native-2.40.6-r0 do_compile (pid 481)
[440A[J[1;31mERROR[0m: [31mCommand Error: exit status: 1  Output:
checking file configure.ac
Reversed (or previously applied) patch detected!  Assume -R? [n] 
Apply anyway? [n] 
Skipping patch.
1 out of 1 hunk ignored[0m
Currently 8 running tasks (1303 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: weston-1.6.0-r2 do_patch (pid 483)
5: binutils-native-2.24-r0 do_install (pid 476)
6: ruby-native-2.2.1-r0 do_configure (pid 482)
7: librsvg-native-2.40.6-r0 do_compile (pid 481)
[440A[J[1;31mERROR[0m: [31mFunction failed: patch_do_patch[0m
Currently 8 running tasks (1303 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: weston-1.6.0-r2 do_patch (pid 483)
5: binutils-native-2.24-r0 do_install (pid 476)
6: ruby-native-2.2.1-r0 do_configure (pid 482)
7: librsvg-native-2.40.6-r0 do_compile (pid 481)
[440A[J[1;31mERROR[0m: [31mLogfile of failure stored in: /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/weston/1.6.0-r2/temp/log.do_patch.483[0m
Currently 7 running tasks (1303 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: binutils-native-2.24-r0 do_install (pid 476)
5: ruby-native-2.2.1-r0 do_configure (pid 482)
6: librsvg-native-2.40.6-r0 do_compile (pid 481)
[402A[J[1;31mERROR[0m: [31mTask 1465 (/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../poky/meta/recipes-graphics/wayland/weston_1.6.0.bb, do_patch) failed with exit code '1'[0m
Currently 7 running tasks (1303 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: binutils-native-2.24-r0 do_install (pid 476)
5: ruby-native-2.2.1-r0 do_configure (pid 482)
6: librsvg-native-2.40.6-r0 do_compile (pid 481)
[402A[JCurrently 7 running tasks (1304 of 4460):
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: openjade-native-1.3.2-r5 do_install (pid 475)
2: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
3: audiomanager-7.0-r1 do_unpack (pid 484)
4: binutils-native-2.24-r0 do_install (pid 476)
5: ruby-native-2.2.1-r0 do_configure (pid 482)
6: librsvg-native-2.40.6-r0 do_compile (pid 481)
[402A[JWaiting for 6 running tasks to finish:
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
2: audiomanager-7.0-r1 do_unpack (pid 484)
3: binutils-native-2.24-r0 do_install (pid 476)
4: ruby-native-2.2.1-r0 do_configure (pid 482)
5: librsvg-native-2.40.6-r0 do_compile (pid 481)
[350A[JWaiting for 7 running tasks to finish:
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
2: audiomanager-7.0-r1 do_unpack (pid 484)
3: binutils-native-2.24-r0 do_install (pid 476)
4: ruby-native-2.2.1-r0 do_configure (pid 482)
5: librsvg-native-2.40.6-r0 do_compile (pid 481)
6: libsoup-2.4-2.46.0-r0 do_fetch (pid 942)
[394A[JWaiting for 6 running tasks to finish:
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
2: audiomanager-7.0-r1 do_unpack (pid 484)
3: binutils-native-2.24-r0 do_install (pid 476)
4: ruby-native-2.2.1-r0 do_configure (pid 482)
5: librsvg-native-2.40.6-r0 do_compile (pid 481)
[350A[JWaiting for 5 running tasks to finish:
0: gcc-cross-x86_64-4.9.2-r0 do_install (pid 466)
1: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
2: binutils-native-2.24-r0 do_install (pid 476)
3: ruby-native-2.2.1-r0 do_configure (pid 482)
4: librsvg-native-2.40.6-r0 do_compile (pid 481)
[307A[JWaiting for 4 running tasks to finish:
0: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
1: binutils-native-2.24-r0 do_install (pid 476)
2: ruby-native-2.2.1-r0 do_configure (pid 482)
3: librsvg-native-2.40.6-r0 do_compile (pid 481)
[257A[JWaiting for 3 running tasks to finish:
0: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
1: ruby-native-2.2.1-r0 do_configure (pid 482)
2: librsvg-native-2.40.6-r0 do_compile (pid 481)
[209A[JWaiting for 2 running tasks to finish:
0: dbus-c++-native-1_0.9.0+gitrAUTOINC+1f6f3e6e96-r0 do_compile (pid 485)
1: ruby-native-2.2.1-r0 do_configure (pid 482)
[160A[JWaiting for 1 running tasks to finish:
0: ruby-native-2.2.1-r0 do_configure (pid 482)
[86A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 1304 tasks of which 1295 didn't need to be rerun and 1 failed.[0m
Waiting for 0 running tasks to finish:

Summary: 1 task failed:
  /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../poky/meta/recipes-graphics/wayland/weston_1.6.0.bb, do_patch
Summary: There were 2 ERROR messages shown, returning a non-zero exit code.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

**NOTE**: A full build takes about TODO hours to complete (Docker Engine running on a dual-core AMD Opteron(TM) Processor 6276 CPU X5450 @2300 MHz, 4 GB RAM).

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

TODO: Attach screenshot

<!-- EOF -->
