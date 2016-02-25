# Building a Yocto distribution for the UDOO Neo

This document explains how to build from sources a [Yocto](https://www.yoctoproject.org/)-based distribution for the [UDOO Neo](http://www.udoo.org/udoo-neo/) using [easy-jenkins](https://github.com/gmacario/easy-jenkins).

The following instructions were tested on

* Docker client: mac-tizy (HW: MacBook Pro; SW: OS X 10.11.3, Docker Toolbox 10.0)
* Docker engine: mv-linux-powerhorse (HW: HP xw8600 Workstation, SW: Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.0)

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

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
        - Branch Specifier (blank for `any`): `*/dev-udooneo-jethro`
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

# Configure git
git config --global user.name "easy-jenkins"
git config --global user.email "$(whoami)@$(hostname)"

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Workaround for https://github.com/gmacario/easy-jenkins/issues/57
bitbake m4-firmware

bitbake core-image-minimal
# bitbake udoo-image-full-cmdline
# bitbake genivi-demo-platform

# EOF
```
- Post-build Actions
    - Archive the artifacts
      - Files to archive: `*/tmp-glibc/deploy/images/**/*.sdcard.gz`

then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-25 09:49 CET): Tested on ies-genbld01-vm -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_yocto_udooneo/lastBuild/console`

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/dev-udooneo-jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/dev-udooneo-jethro^{commit} # timeout=10
Checking out Revision c83152c2cc1e87d0f6adfcc45358331635f34618 (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f c83152c2cc1e87d0f6adfcc45358331635f34618
 > git rev-list c83152c2cc1e87d0f6adfcc45358331635f34618 # timeout=10
Docker container 8408f54cfa8cab525b5790e3080e9e6aa7980bde5d6b6f3698dfff11b5746139 started to host the build
$ docker exec --tty 8408f54cfa8cab525b5790e3080e9e6aa7980bde5d6b6f3698dfff11b5746139 env
[workspace] $ docker exec --tty --user 0:0 8408f54cfa8cab525b5790e3080e9e6aa7980bde5d6b6f3698dfff11b5746139 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#6 BUILD_ID=6 BUILD_NUMBER=6 BUILD_TAG=jenkins-GENIVI-build_yocto_udooneo-6 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/dev-udooneo-jethro GIT_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618 GIT_PREVIOUS_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618 GIT_PREVIOUS_SUCCESSFUL_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618 GIT_URL=https://github.com/gmacario/genivi-demo-platform HOME=/root HOSTNAME=8fe1ae0d5dce HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=7ee8349211b6fd57 JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=7ee8349211b6fd57 JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.1 JOB_NAME=GENIVI/build_yocto_udooneo LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace /bin/bash -xe /tmp/hudson847811817353816394.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace
+ ls -la
total 64
drwxr-xr-x 12 root root 4096 Feb 24 13:53 .
drwxr-xr-x  4 root root 4096 Feb 25 08:50 ..
drwxr-xr-x  9 root root 4096 Feb 25 08:50 .git
-rw-r--r--  1 root root  204 Feb 24 13:53 .gitignore
-rw-r--r--  1 root root  938 Feb 24 13:53 .gitmodules
-rw-r--r--  1 root root  805 Feb 24 13:53 README.md
drwxr-xr-x  7 root root 4096 Feb 24 14:01 gdp-src-build
-rw-r--r--  1 root root  908 Feb 24 13:53 init.sh
drwxr-xr-x 19 root root 4096 Feb 24 13:58 meta-fsl-arm
drwxr-xr-x  7 root root 4096 Feb 24 13:58 meta-fsl-arm-extra
drwxr-xr-x  5 root root 4096 Feb 24 13:58 meta-fsl-demos
drwxr-xr-x 16 root root 4096 Feb 24 13:59 meta-genivi-demo
drwxr-xr-x 17 root root 4096 Feb 24 13:59 meta-openembedded
drwxr-xr-x  7 root root 4096 Feb 24 13:59 meta-qt5
drwxr-xr-x 10 root root 4096 Feb 24 13:59 meta-udoo
drwxr-xr-x 10 root root 4096 Feb 24 14:00 poky
+ printenv
HOSTNAME=8fe1ae0d5dce
HUDSON_SERVER_COOKIE=7ee8349211b6fd57
TERM=xterm
BUILD_TAG=jenkins-GENIVI-build_yocto_udooneo-6
GIT_PREVIOUS_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618
COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log
ROOT_BUILD_CAUSE=MANUALTRIGGER
WORKSPACE=/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace
CA_CERTIFICATES_JAVA_VERSION=20140324
JENKINS_HOME=/var/jenkins_home
GIT_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618
JENKINS_UC=https://updates.jenkins-ci.org
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
BUILD_CAUSE_MANUALTRIGGER=true
PWD=/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace
JENKINS_SLAVE_AGENT_PORT=50000
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
LANG=C.UTF-8
JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e
JAVA_VERSION=8u72
JOB_NAME=GENIVI/build_yocto_udooneo
BUILD_DISPLAY_NAME=#6
BUILD_ID=6
BUILD_CAUSE=MANUALTRIGGER
TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888
GIT_PREVIOUS_SUCCESSFUL_COMMIT=c83152c2cc1e87d0f6adfcc45358331635f34618
SHLVL=3
JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1
HOME=/root
GIT_BRANCH=origin/dev-udooneo-jethro
JENKINS_SERVER_COOKIE=7ee8349211b6fd57
EXECUTOR_NUMBER=1
NODE_LABELS=master
GIT_URL=https://github.com/gmacario/genivi-demo-platform
HUDSON_HOME=/var/jenkins_home
CLASSPATH=
NODE_NAME=master
BUILD_NUMBER=6
ROOT_BUILD_CAUSE_MANUALTRIGGER=true
JENKINS_VERSION=1.642.1
BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
 b="${f%.override}";
 echo "$f" >> "/var/jenkins_home/copy_reference_file.log";
 rel="${b:23}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "/var/jenkins_home/copy_reference_file.log";
 if [[ ! -e /var/jenkins_home/${rel} || $f = *.override ]]; then
 echo "copy $rel to JENKINS_HOME" >> "/var/jenkins_home/copy_reference_file.log";
 mkdir -p "/var/jenkins_home/${dir:23}";
 cp -r "${f}" "/var/jenkins_home/${rel}";
 [[ ${rel} == plugins/*.jpi ]] && touch "/var/jenkins_home/${rel}.pinned";
 fi
}
_=/usr/bin/printenv
+ git config --global user.name easy-jenkins
++ whoami
++ hostname
+ git config --global user.email root@8408f54cfa8c
+ source init.sh
+++ basename /tmp/hudson847811817353816394.sh
++ cmd=hudson847811817353816394.sh
++ '[' hudson847811817353816394.sh = init.sh ']'
++ git submodule init
++ git submodule sync
Synchronizing submodule url for 'meta-fsl-arm'
Synchronizing submodule url for 'meta-fsl-arm-extra'
Synchronizing submodule url for 'meta-fsl-demos'
Synchronizing submodule url for 'meta-genivi-demo'
Synchronizing submodule url for 'meta-openembedded'
Synchronizing submodule url for 'meta-qt5'
Synchronizing submodule url for 'meta-udoo'
Synchronizing submodule url for 'poky'
++ git submodule update
...

### Shell environment set up for builds. ###

You can now run 'bitbake <target>'

Common targets are:
    core-image-minimal
    core-image-sato
    meta-toolchain
    adt-installer
    meta-ide-support

You can also run generated qemu images with a command like 'runqemu qemux86'
...

++ echo 'Now run:  bitbake genivi-demo-platform'
Now run:  bitbake genivi-demo-platform
++ whoami
+ '[' root = root ']'
+ touch conf/sanity.conf
+ bitbake m4-firmware
Loading cache:   0% || ETA:  --:--:--
...
Loading cache: 100% || ETA:  00:00:00

Loaded 2123 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "arm-oe-linux-gnueabi"
MACHINE           = "udooneo"
DISTRO            = "poky-ivi-systemd"
DISTRO_VERSION    = "nodistro.0"
TUNE_FEATURES     = "arm armv7a vfp thumb neon callconvention-hard cortexa9"
TARGET_FPU        = "vfp-neon"
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:7fe17a2942ff03e2ec47d566fd5393f52b2eb736"
meta-oe           = "HEAD:dc5634968b270dde250690609f0015f881db81f2"
meta-udoo         = "HEAD:0dca517aca288909dddeb74e33c444d9b5e3afd9"
meta-fsl-arm      = "HEAD:35b8b9bd9863de208ab60e33b55f10ee43e2619b"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
[1;29mNOTE[0m: [29mTasks Summary: Attempted 386 tasks of which 386 didn't need to be rerun and all succeeded.[0m
+ bitbake core-image-minimal
Loading cache:   0% || ETA:  --:--:--
...
Loading cache: 100% || ETA:  00:00:00

Loaded 2123 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "arm-oe-linux-gnueabi"
MACHINE           = "udooneo"
DISTRO            = "poky-ivi-systemd"
DISTRO_VERSION    = "nodistro.0"
TUNE_FEATURES     = "arm armv7a vfp thumb neon callconvention-hard cortexa9"
TARGET_FPU        = "vfp-neon"
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:7fe17a2942ff03e2ec47d566fd5393f52b2eb736"
meta-oe           = "HEAD:dc5634968b270dde250690609f0015f881db81f2"
meta-udoo         = "HEAD:0dca517aca288909dddeb74e33c444d9b5e3afd9"
meta-fsl-arm      = "HEAD:35b8b9bd9863de208ab60e33b55f10ee43e2619b"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
[1;29mNOTE[0m: [29mTasks Summary: Attempted 1303 tasks of which 1303 didn't need to be rerun and all succeeded.[0m
Stopping Docker container after build completion
Archiving artifacts
Notifying upstream projects of job completion
Finished: SUCCESS
```

Result: SUCCESS

## Testing the image on UDOO Neo

* Browse `${JENKINS_URL}/jobs/build_yocto_udooneo`, in section "Last Successful Artifacts" click on `core-image-minimal-udooneo.sdcard.gz` to download the file to a machine with a SD Card writer.
* Write the image to a blank Micro SD card
  - On MS Windows first uncompress the image, then use [Win 32 Disk Imager](https://sourceforge.net/projects/win32diskimager/) to write the image to the Micro SD card
  - On Linux just use `$ zcat *.sdcard.gz | sudo dd of=/dev/xxx`
* Insert the Micro SD card on your UDOO Neo and power up the board.

Please see http://www.udoo.org/get-started-neo/ for details.

<!-- EOF -->
