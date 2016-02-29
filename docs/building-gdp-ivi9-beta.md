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

<!-- (2016-02-29 00:22 CET) http://192.168.99.100:9080/job/GENIVI/job/build_gdp_ivi9_beta/lastBuild/console -->

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
Docker container 6adfe0290a7a1f655f1cefd79068635e1eda9f51145a714909ceb036ab6b3254 started to host the build
$ docker exec --tty 6adfe0290a7a1f655f1cefd79068635e1eda9f51145a714909ceb036ab6b3254 env
[build_gdp_ivi9_beta] $ docker exec --tty --user 1000:1000 6adfe0290a7a1f655f1cefd79068635e1eda9f51145a714909ceb036ab6b3254 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#5 BUILD_ID=5 BUILD_NUMBER=5 BUILD_TAG=jenkins-GENIVI-build_gdp_ivi9_beta-5 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/qemux86-64-ivi9-beta GIT_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87 GIT_PREVIOUS_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87 GIT_URL=git://git.projects.genivi.org/genivi-demo-platform.git HOME=/var/jenkins_home HOSTNAME=b6987309d631 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=338d41cd30e1cddf JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=338d41cd30e1cddf JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.1 JOB_NAME=GENIVI/build_gdp_ivi9_beta LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta /bin/bash -xe /tmp/hudson2246849306886478518.sh
+ id
uid=1000(build) gid=1000(build) groups=1000(build)
+ pwd
/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta
+ ls -la
total 52
drwxr-xr-x  9 build build 4096 Feb 27 23:14 .
drwxr-xr-x  3 build build 4096 Feb 27 23:14 ..
drwxr-xr-x  9 build build 4096 Feb 28 23:18 .git
-rw-r--r--  1 build build  204 Feb 27 23:14 .gitignore
-rw-r--r--  1 build build  470 Feb 27 23:14 .gitmodules
-rw-r--r--  1 build build  805 Feb 27 23:14 README.md
drwxr-xr-x  7 build build 4096 Feb 27 23:25 gdp-src-build
-rw-r--r--  1 build build  908 Feb 27 23:14 init.sh
drwxr-xr-x 15 build build 4096 Feb 27 23:21 meta-genivi-demo
drwxr-xr-x  6 build build 4096 Feb 27 23:21 meta-ivi
drwxr-xr-x 18 build build 4096 Feb 27 23:21 meta-openembedded
drwxr-xr-x  7 build build 4096 Feb 27 23:21 meta-qt5
drwxr-xr-x 10 build build 4096 Feb 27 23:23 poky
+ sort
+ printenv
 [[ ${rel} == plugins/*.jpi ]] && touch "/var/jenkins_home/${rel}.pinned";
 b="${f%.override}";
 cp -r "${f}" "/var/jenkins_home/${rel}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "/var/jenkins_home/copy_reference_file.log";
 echo "$f" >> "/var/jenkins_home/copy_reference_file.log";
 echo "copy $rel to JENKINS_HOME" >> "/var/jenkins_home/copy_reference_file.log";
 fi
 if [[ ! -e /var/jenkins_home/${rel} || $f = *.override ]]; then
 mkdir -p "/var/jenkins_home/${dir:23}";
 rel="${b:23}";
BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
BUILD_CAUSE=MANUALTRIGGER
BUILD_CAUSE_MANUALTRIGGER=true
BUILD_DISPLAY_NAME=#5
BUILD_ID=5
BUILD_NUMBER=5
BUILD_TAG=jenkins-GENIVI-build_gdp_ivi9_beta-5
CA_CERTIFICATES_JAVA_VERSION=20140324
CLASSPATH=
COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log
EXECUTOR_NUMBER=1
GIT_BRANCH=origin/qemux86-64-ivi9-beta
GIT_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87
GIT_PREVIOUS_COMMIT=6e50965700f98572eaa731e426d561b1b5031c87
GIT_URL=git://git.projects.genivi.org/genivi-demo-platform.git
HOME=/var/jenkins_home
HOSTNAME=b6987309d631
HUDSON_HOME=/var/jenkins_home
HUDSON_SERVER_COOKIE=338d41cd30e1cddf
JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_VERSION=8u72
JENKINS_HOME=/var/jenkins_home
JENKINS_SERVER_COOKIE=338d41cd30e1cddf
JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e
JENKINS_SLAVE_AGENT_PORT=50000
JENKINS_UC=https://updates.jenkins-ci.org
JENKINS_VERSION=1.642.1
JOB_NAME=GENIVI/build_gdp_ivi9_beta
LANG=C.UTF-8
NODE_LABELS=master
NODE_NAME=master
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
PWD=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta
ROOT_BUILD_CAUSE=MANUALTRIGGER
ROOT_BUILD_CAUSE_MANUALTRIGGER=true
SHLVL=3
TERM=xterm
TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888
WORKSPACE=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta
_=/usr/bin/printenv
}
+ git config --global user.name easy-jenkins
++ whoami
++ hostname
+ git config --global user.email build@6adfe0290a7a
+ source init.sh
+++ basename /tmp/hudson2246849306886478518.sh
++ cmd=hudson2246849306886478518.sh
++ '[' hudson2246849306886478518.sh = init.sh ']'
++ git submodule init
++ git submodule sync
Synchronizing submodule url for 'meta-genivi-demo'
Synchronizing submodule url for 'meta-ivi'
Synchronizing submodule url for 'meta-openembedded'
Synchronizing submodule url for 'meta-qt5'
Synchronizing submodule url for 'poky'
++ git submodule update
++ source poky/oe-init-build-env gdp-src-build
+++ '[' -n poky/oe-init-build-env ']'
++++ dirname poky/oe-init-build-env
+++ OEROOT=poky
+++ '[' -n '' ']'
+++ THIS_SCRIPT=poky/oe-init-build-env
+++ '[' -z '' ']'
+++ '[' /tmp/hudson2246849306886478518.sh = poky/oe-init-build-env ']'
++++ readlink -f poky
+++ OEROOT=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky
+++ export OEROOT
+++ . /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts/oe-buildenv-internal
++++ '[' -z /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky ']'
++++ '[' -z '' -a '!' -z '' ']'
+++++ /usr/bin/env python --version
+++++ grep 'Python 3'
++++ py_v3_check=
++++ '[' '' '!=' '' ']'
+++++ python -c 'import sys; print sys.version_info >= (2,7,3)'
++++ py_v26_check=True
++++ '[' True '!=' True ']'
++++ '[' x = x ']'
++++ '[' xgdp-src-build = x ']'
++++ BDIR=gdp-src-build
++++ '[' gdp-src-build = / ']'
+++++ echo gdp-src-build
+++++ sed -re 's|/+$||'
++++ BDIR=gdp-src-build
+++++ readlink -f gdp-src-build
++++ BDIR=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build
++++ '[' -z /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build ']'
++++ '[' x '!=' x ']'
++++ expr /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build : '/.*'
++++ BUILDDIR=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build
++++ unset BDIR
++++ '[' x = x ']'
++++ BITBAKEDIR=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/
+++++ readlink -f /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/
++++ BITBAKEDIR=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake
+++++ readlink -f /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build
++++ BUILDDIR=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build
++++ test -d /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake
++++ NEWPATHS=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts:/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/bin:
+++++ echo /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+++++ sed -e 's|:/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts:/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/bin:|:|g' -e 's|^/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts:/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/bin:||'
++++ PATH=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts:/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/bitbake/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
++++ unset BITBAKEDIR NEWPATHS
++++ export BUILDDIR
++++ export PATH
++++ export 'BB_ENV_EXTRAWHITE=MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy FTPS_PROXY ftps_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR'
++++ BB_ENV_EXTRAWHITE='MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy FTPS_PROXY ftps_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR'
+++ /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/poky/scripts/oe-setup-builddir

### Shell environment set up for builds. ###

You can now run 'bitbake <target>'

Common targets are:
    core-image-minimal
    core-image-sato
    meta-toolchain
    adt-installer
    meta-ide-support

You can also run generated qemu images with a command like 'runqemu qemux86'
+++ '[' -n /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build ']'
+++ cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build
+++ unset OEROOT
+++ unset BBPATH
+++ unset THIS_SCRIPT
+++ '[' -z '' ']'
+++ '[' -f bitbake.lock ']'
+++ grep : bitbake.lock
+++ '[' 1 = 0 ']'
++ echo

++ echo 'Now run:  bitbake genivi-demo-platform'
Now run:  bitbake genivi-demo-platform
++ whoami
+ '[' build = root ']'
+ bitbake genivi-demo-platform
Loading cache:   0% || ETA:  --:--:--
...
Loading cache: 100% || ETA:  00:00:00

Loaded 2087 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.26.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "x86_64-poky-linux"
MACHINE           = "qemux86-64"
DISTRO            = "poky-ivi-systemd"
DISTRO_VERSION    = "9.0.1"
TUNE_FEATURES     = "m64 core2"
TARGET_FPU        = ""
meta              
meta-yocto        
meta-yocto-bsp    = "(detachedfromeb4a134):eb4a134a60e3ac26a48379675ad6346a44010339"
meta-ivi          
meta-ivi-bsp      = "(detachedfrombfd95c5):bfd95c5021885ed61b58a33087a4ee8e3d2f32ad"
meta-oe           
meta-filesystems  
meta-ruby         = "(detachedfrom5b0305d):5b0305d9efa4b5692cd942586fb7aa92dba42d59"
meta-qt5          = "(detachedfrom90919b9):90919b9d86988e7da01fa2c0a07246b5b5600a5d"
meta-genivi-demo  = "(detachedfrom6c13a96):6c13a96ba719c657bd69f7c0212cd224def036c8"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
No currently running tasks (2025 of 4460)
[42A[JNo currently running tasks (2029 of 4460)
[42A[J[1;33mWARNING[0m: [33mnavit: LICENSE value "GPLv2 LGPLv2" has an invalid format - license names must be separated by the following characters to indicate the license selection: &|() [0m
No currently running tasks (2029 of 4460)
[42A[JCurrently 1 running tasks (2029 of 4460):
0: navit-1_0.2.0+svnr5532-r9.3 do_compile (pid 445)
[94A[JCurrently 2 running tasks (2029 of 4460):
0: navit-1_0.2.0+svnr5532-r9.3 do_compile (pid 445)
1: ruby-native-2.2.1-r0 do_unpack (pid 446)
[138A[J[1;31mERROR[0m: [31mFunction failed: do_compile (log file is located at /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/temp/log.do_compile.445)[0m
Currently 2 running tasks (2029 of 4460):
0: navit-1_0.2.0+svnr5532-r9.3 do_compile (pid 445)
1: ruby-native-2.2.1-r0 do_unpack (pid 446)
[138A[J[1;31mERROR[0m: [31mLogfile of failure stored in: /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/temp/log.do_compile.445[0m
Log data follows:
| DEBUG: SITE files ['endian-little', 'common-linux', 'common-glibc', 'bit-64', 'x86_64-linux', 'common']
| DEBUG: Executing shell function do_compile
| NOTE: make -j 2
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -H/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit -B/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build --check-build-system CMakeFiles/Makefile.cmake 0
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_start /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles/progress.marks
| make -f CMakeFiles/Makefile2 all
| make[1]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/CMakeFiles/version.dir/build.make navit/CMakeFiles/version.dir/depend
| make -f navit/fib-1.1/CMakeFiles/fib.dir/build.make navit/fib-1.1/CMakeFiles/fib.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/CMakeFiles/version.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/fib-1.1 /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/fib-1.1 /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/fib-1.1/CMakeFiles/fib.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/fib-1.1/CMakeFiles/fib.dir/build.make navit/fib-1.1/CMakeFiles/fib.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/fib-1.1/CMakeFiles/fib.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] Built target fib
| make -f navit/CMakeFiles/navit_config_xml.dir/build.make navit/CMakeFiles/navit_config_xml.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/CMakeFiles/navit_config_xml.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/CMakeFiles/navit_config_xml.dir/build.make navit/CMakeFiles/navit_config_xml.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/CMakeFiles/navit_config_xml.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/CMakeFiles/version.dir/build.make navit/CMakeFiles/version.dir/build
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -D SRC=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/version.h.in -D DST=/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/version.h -D NAME=SVN_VERSION -P /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/cmake/version.cmake
| git: 'svn' is not a git command. See 'git --help'.
| 
| Did you mean one of these?
| 	fsck
| 	mv
| 	show
| Built target navit_config_xml
| make -f navit/support/shapefile/CMakeFiles/support_shapefile.dir/build.make navit/support/shapefile/CMakeFiles/support_shapefile.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/support/shapefile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/support/shapefile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/support/shapefile/CMakeFiles/support_shapefile.dir/DependInfo.cmake --color=
| -- Found Subversion: /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/svn (found version "1.8.11")
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/support/shapefile/CMakeFiles/support_shapefile.dir/build.make navit/support/shapefile/CMakeFiles/support_shapefile.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/support/shapefile/CMakeFiles/support_shapefile.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| -- SVN-version Unversioned directory
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] [  0%] Built target support_shapefile
| make -f navit/font/freetype/CMakeFiles/font_freetype.dir/build.make navit/font/freetype/CMakeFiles/font_freetype.dir/depend
| Built target version
| make -f navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/build.make navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/font/freetype /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/font/freetype /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/font/freetype/CMakeFiles/font_freetype.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/font/freetype/CMakeFiles/font_freetype.dir/build.make navit/font/freetype/CMakeFiles/font_freetype.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/graphics/opengl /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/graphics/opengl /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/font/freetype/CMakeFiles/font_freetype.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] Built target font_freetype
| make -f navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/build.make navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/gpsd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gpsd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/build.make navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/build.make navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/gpsd/CMakeFiles/vehicle_gpsd.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| [  0%] make[2]: Nothing to be done for `navit/graphics/opengl/CMakeFiles/graphics_opengl.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| Built target vehicle_gpsd
| make -f navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/build.make navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/depend
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/gypsy /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gypsy /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/DependInfo.cmake --color=
| Built target graphics_opengl
| make -f navit/binding/dbus/CMakeFiles/binding_dbus.dir/build.make navit/binding/dbus/CMakeFiles/binding_dbus.dir/depend
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/build.make navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/binding/dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/binding/dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/binding/dbus/CMakeFiles/binding_dbus.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/binding/dbus/CMakeFiles/binding_dbus.dir/build.make navit/binding/dbus/CMakeFiles/binding_dbus.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/gypsy/CMakeFiles/vehicle_gypsy.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] Built target vehicle_gypsy
| make -f navit/speech/dbus/CMakeFiles/speech_dbus.dir/build.make navit/speech/dbus/CMakeFiles/speech_dbus.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/speech/dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/speech/dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/speech/dbus/CMakeFiles/speech_dbus.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/speech/dbus/CMakeFiles/speech_dbus.dir/build.make navit/speech/dbus/CMakeFiles/speech_dbus.dir/build
| make[2]: Nothing to be done for `navit/binding/dbus/CMakeFiles/binding_dbus.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/speech/dbus/CMakeFiles/speech_dbus.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] [  0%] Built target binding_dbus
| make -f navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/build.make navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/depend
| Built target speech_dbus
| make -f navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/build.make navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/speech/cmdline /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/speech/cmdline /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/gpsd_dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gpsd_dbus /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/build.make navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/build.make navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/speech/cmdline/CMakeFiles/speech_cmdline.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Nothing to be done for `navit/vehicle/gpsd_dbus/CMakeFiles/vehicle_gpsd_dbus.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] [  0%] Built target speech_cmdline
| make -f navit/graphics/null/CMakeFiles/graphics_null.dir/build.make navit/graphics/null/CMakeFiles/graphics_null.dir/depend
| Built target vehicle_gpsd_dbus
| make -f navit/osd/core/CMakeFiles/osd_core.dir/build.make navit/osd/core/CMakeFiles/osd_core.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/graphics/null /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/graphics/null /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/graphics/null/CMakeFiles/graphics_null.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/graphics/null/CMakeFiles/graphics_null.dir/build.make navit/graphics/null/CMakeFiles/graphics_null.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/osd/core /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/osd/core /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/osd/core/CMakeFiles/osd_core.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/graphics/null/CMakeFiles/graphics_null.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/osd/core/CMakeFiles/osd_core.dir/build.make navit/osd/core/CMakeFiles/osd_core.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/osd/core/CMakeFiles/osd_core.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| Built target graphics_null
| make -f navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/build.make navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/depend
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/demo /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/demo /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/build.make navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/build
| Built target osd_core
| make -f navit/vehicle/file/CMakeFiles/vehicle_file.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_file.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/demo/CMakeFiles/vehicle_demo.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file/CMakeFiles/vehicle_file.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/file/CMakeFiles/vehicle_file.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_file.dir/build
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| Built target vehicle_demo
| make[2]: Nothing to be done for `navit/vehicle/file/CMakeFiles/vehicle_file.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make -f navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/depend
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/DependInfo.cmake --color=
| Built target vehicle_file
| make -f navit/vehicle/file/CMakeFiles/vehicle_serial.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_serial.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file/CMakeFiles/vehicle_serial.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/file/CMakeFiles/vehicle_pipe.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  0%] make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/file/CMakeFiles/vehicle_serial.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_serial.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/file/CMakeFiles/vehicle_serial.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| Built target vehicle_pipe
| make -f navit/vehicle/file/CMakeFiles/vehicle_socket.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_socket.dir/depend
| [  0%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/file/CMakeFiles/vehicle_socket.dir/DependInfo.cmake --color=
| Built target vehicle_serial
| make -f navit/vehicle/null/CMakeFiles/vehicle_null.dir/build.make navit/vehicle/null/CMakeFiles/vehicle_null.dir/depend
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/file/CMakeFiles/vehicle_socket.dir/build.make navit/vehicle/file/CMakeFiles/vehicle_socket.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/vehicle/null /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/null /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/vehicle/null/CMakeFiles/vehicle_null.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/vehicle/null/CMakeFiles/vehicle_null.dir/build.make navit/vehicle/null/CMakeFiles/vehicle_null.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/file/CMakeFiles/vehicle_socket.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles  100
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/vehicle/null/CMakeFiles/vehicle_null.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] [  1%] Built target vehicle_socket
| make -f navit/gui/internal/CMakeFiles/gui_internal.dir/build.make navit/gui/internal/CMakeFiles/gui_internal.dir/depend
| Built target vehicle_null
| make -f navit/map/binfile/CMakeFiles/map_binfile.dir/build.make navit/map/binfile/CMakeFiles/map_binfile.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/binfile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/binfile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/binfile/CMakeFiles/map_binfile.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/gui/internal /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/gui/internal /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/gui/internal/CMakeFiles/gui_internal.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/gui/internal/CMakeFiles/gui_internal.dir/build.make navit/gui/internal/CMakeFiles/gui_internal.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/binfile/CMakeFiles/map_binfile.dir/build.make navit/map/binfile/CMakeFiles/map_binfile.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/binfile/CMakeFiles/map_binfile.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] make[2]: Nothing to be done for `navit/gui/internal/CMakeFiles/gui_internal.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] Built target map_binfile
| make -f navit/map/filter/CMakeFiles/map_filter.dir/build.make navit/map/filter/CMakeFiles/map_filter.dir/depend
| Built target gui_internal
| make -f navit/map/mg/CMakeFiles/map_mg.dir/build.make navit/map/mg/CMakeFiles/map_mg.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/filter /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/filter /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/filter/CMakeFiles/map_filter.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/mg /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/mg /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/mg/CMakeFiles/map_mg.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/mg/CMakeFiles/map_mg.dir/build.make navit/map/mg/CMakeFiles/map_mg.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/filter/CMakeFiles/map_filter.dir/build.make navit/map/filter/CMakeFiles/map_filter.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/mg/CMakeFiles/map_mg.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/filter/CMakeFiles/map_filter.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] [  1%] Built target map_mg
| make -f navit/map/shapefile/CMakeFiles/map_shapefile.dir/build.make navit/map/shapefile/CMakeFiles/map_shapefile.dir/depend
| Built target map_filter
| make -f navit/map/textfile/CMakeFiles/map_textfile.dir/build.make navit/map/textfile/CMakeFiles/map_textfile.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/shapefile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/shapefile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/shapefile/CMakeFiles/map_shapefile.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/textfile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/textfile /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/textfile/CMakeFiles/map_textfile.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/shapefile/CMakeFiles/map_shapefile.dir/build.make navit/map/shapefile/CMakeFiles/map_shapefile.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/textfile/CMakeFiles/map_textfile.dir/build.make navit/map/textfile/CMakeFiles/map_textfile.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/shapefile/CMakeFiles/map_shapefile.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/textfile/CMakeFiles/map_textfile.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] [  1%] Built target map_shapefile
| make -f navit/map/csv/CMakeFiles/map_csv.dir/build.make navit/map/csv/CMakeFiles/map_csv.dir/depend
| Built target map_textfile
| make -f navit/maptool/CMakeFiles/maptool_core.dir/build.make navit/maptool/CMakeFiles/maptool_core.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/map/csv /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/csv /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/map/csv/CMakeFiles/map_csv.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/maptool /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/maptool /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/maptool/CMakeFiles/maptool_core.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/map/csv/CMakeFiles/map_csv.dir/build.make navit/map/csv/CMakeFiles/map_csv.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/maptool/CMakeFiles/maptool_core.dir/build.make navit/maptool/CMakeFiles/maptool_core.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/map/csv/CMakeFiles/map_csv.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  1%] Built target map_csv
| make -f navit/xpm/CMakeFiles/images.dir/build.make navit/xpm/CMakeFiles/images.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/maptool/CMakeFiles/maptool_core.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles  96
| [  2%] Built target maptool_core
| make -f po/CMakeFiles/locales.dir/build.make po/CMakeFiles/locales.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/po /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/po /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/po/CMakeFiles/locales.dir/DependInfo.cmake --color=
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/CMakeFiles/images.dir/DependInfo.cmake --color=
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f po/CMakeFiles/locales.dir/build.make po/CMakeFiles/locales.dir/build
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/xpm/CMakeFiles/images.dir/build.make navit/xpm/CMakeFiles/images.dir/build
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `po/CMakeFiles/locales.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles  89 90 91 92 93 94
| [  8%] Built target locales
| make -f navit/CMakeFiles/navit_core.dir/build.make navit/CMakeFiles/navit_core.dir/depend
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles
| [  8%] make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_depends "Unix Makefiles" /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/CMakeFiles/navit_core.dir/DependInfo.cmake --color=
| Generating country_MF.png
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/rsvg-convert --width=32 --height=32 --output /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/country_MF.png /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_MF.svgz
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make -f navit/CMakeFiles/navit_core.dir/build.make navit/CMakeFiles/navit_core.dir/build
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_MF.svgz: Start tag expected, '<' not found
| 
| 
| make[2]: *** [navit/xpm/country_MF.png] Error 1
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[1]: *** [navit/xpm/CMakeFiles/images.dir/all] Error 2
| make[1]: *** Waiting for unfinished jobs....
| make[2]: Entering directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[2]: Nothing to be done for `navit/CMakeFiles/navit_core.dir/build'.
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/cmake -E cmake_progress_report /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/CMakeFiles  97 98 99
| [ 11%] Built target navit_core
| make[1]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make: *** [all] Error 2
| ERROR: oe_runmake failed
| WARNING: exit code 1 from a shell command.
| ERROR: Function failed: do_compile (log file is located at /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/temp/log.do_compile.445)
Currently 1 running tasks (2029 of 4460):
0: ruby-native-2.2.1-r0 do_unpack (pid 446)
[86A[J[1;31mERROR[0m: [31mTask 1928 (/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../meta-genivi-demo/recipes-navigation/navit/navit_svn.bb, do_compile) failed with exit code '1'[0m
Currently 1 running tasks (2029 of 4460):
0: ruby-native-2.2.1-r0 do_unpack (pid 446)
[86A[JCurrently 1 running tasks (2030 of 4460):
0: ruby-native-2.2.1-r0 do_unpack (pid 446)
[86A[JWaiting for 0 running tasks to finish:
[39A[JWaiting for 1 running tasks to finish:
0: libyaml-native-0.1.5-r0 do_install (pid 654)
[87A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 2030 tasks of which 2027 didn't need to be rerun and 1 failed.[0m
Waiting for 0 running tasks to finish:

Summary: 1 task failed:
  /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../meta-genivi-demo/recipes-navigation/navit/navit_svn.bb, do_compile
Summary: There was 1 WARNING message shown.
Summary: There was 1 ERROR message shown, returning a non-zero exit code.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

**NOTE**: A full build takes about TODO hours to complete (Docker Engine running on a dual-core AMD Opteron(TM) Processor 6276 CPU X5450 @2300 MHz, 4 GB RAM).

Browse `${JENKINS_URL}/job/build_gdp_ivi9_beta/ws/gdp-src-build/tmp/deploy/images/qemux86-64/` to inspect the build results.

TODO: Attach screenshot

<!-- EOF -->
