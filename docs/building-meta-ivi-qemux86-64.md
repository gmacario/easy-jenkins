# Building the GENIVI Yocto Baseline using easy-jenkins

## Introduction

<!-- (2016-02-30 16:40 CEST) -->

This document explains how to build from sources the [GENIVI Yocto Baseline](https://at.projects.genivi.org/wiki/display/PROJ/meta-ivi) using [Jenkins](https://jenkins-ci.org/).

The build is performed inside a Docker container spun by the [Docker Pipeline Plugin](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Pipeline+Plugin).

The instructions inside this document have been tested on

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

### Create project `yocto-baseline-next`

<!-- (2016-03-31 09:40 CEST) -->

<!-- Adapted from http://go.genivi.org/go/admin/pipelines/yocto-baseline-next/general -->

Please refer to official build instructions on wiki page [meta-ivi/10.0.0](https://at.projects.genivi.org/wiki/x/UYKw) for details.

Browse `${DOCKER_URL}/job/GENIVI`, then click **New Item**

* Name: `yocto-baseline-next`
* Type: **Pipeline**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 2
* Execute concurrent builds if necessary: Yes

* Pipeline
  - Definition: Pipeline script
    - Script

```
docker.image('gmacario/build-yocto-genivi').inside {
   stage 'Checkout meta-ivi'
   dir ('meta-ivi') {
      git branch: '10.0', url: 'http://git.yoctoproject.org/git/meta-ivi'
   }

   stage 'Checkout poky'
   dir ('poky') {
      git branch: 'jethro', url: 'http://git.yoctoproject.org/git/poky.git'
   }

   stage 'Checkout meta-openembedded'
   dir ('meta-openembedded') {
      git branch: 'jethro', url: 'git://git.openembedded.org/meta-openembedded'
   }

   stage 'Checkout baseline_ci_helper'
   dir ('baseline_ci_helper') {
      git branch: 'master', url: 'https://github.com/gunnarx/baseline_ci_helper.git'
   }

   stage 'Checkout correct SHAs'
   sh 'cd meta-ivi && git config user.name "Jenkins Agent"'
   sh 'cd meta-ivi && git config user.email "no_email@example.com"'
   sh 'cd baseline_ci_helper && sh -c "./checkout_layer_hash.sh poky"'
   sh 'cd baseline_ci_helper && sh -c "./checkout_layer_hash.sh meta-openembedded"'
   
   stage 'Inspect sources'
   echo 'Inspecting sources'
   sh 'pwd'
   sh 'ls -la'

   stage 'Build the image'
   sh 'bash -c "export MACHINE=qemux86-64 && export TEMPLATECONF=$PWD/meta-ivi/meta-ivi/conf && source poky/oe-init-build-env && bitbake leviathan-image"'
   
   stage 'Inspect results'
   echo 'Inspecting results'
   sh 'pwd'
   // sh 'ls -la .'
   sh 'ls -laR build/tmp/deploy/images'
}
```

then click **Save**.

### Build project `yocto-baseline-next`

Browse `${JENKINS_URL}/job/GENIVI/job/yocto-baseline-next/`, then click **Build Now**.

You may watch the build logs at `${JENKINS_URL}/job/GENIVI/job/yocto-baseline-next/lastBuild/console`

<!-- (2016-03-31 19:16 CEST) http://alm-gm-ubu15.solarma.it:9080/job/GENIVI/job/yocto-baseline-next/2/console -->

```
Started by user anonymous
[Pipeline] Allocate node : Start
Running on master in /var/jenkins_home/workspace/GENIVI/yocto-baseline-next
[Pipeline] node {
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ docker inspect -f . gmacario/build-yocto-genivi
.
[Pipeline] Run build steps inside a Docker container : Start
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/GENIVI/yocto-baseline-next -v /var/jenkins_home/workspace/GENIVI/yocto-baseline-next:/var/jenkins_home/workspace/GENIVI/yocto-baseline-next:rw -v /var/jenkins_home/workspace/GENIVI/yocto-baseline-next@tmp:/var/jenkins_home/workspace/GENIVI/yocto-baseline-next@tmp:rw -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** gmacario/build-yocto-genivi cat
[Pipeline] withDockerContainer {
[Pipeline] stage (Checkout meta-ivi)
Entering stage Checkout meta-ivi
Proceeding
[Pipeline] Change current directory : Start
Running in /var/jenkins_home/workspace/GENIVI/yocto-baseline-next/meta-ivi
[Pipeline] dir {
[Pipeline] git
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url http://git.yoctoproject.org/git/meta-ivi # timeout=10
Fetching upstream changes from http://git.yoctoproject.org/git/meta-ivi
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress http://git.yoctoproject.org/git/meta-ivi +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/10.0^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/10.0^{commit} # timeout=10
Checking out Revision 1c3889b3f828fedd0260754163ca65e5644e8e69 (refs/remotes/origin/10.0)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 1c3889b3f828fedd0260754163ca65e5644e8e69 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D 10.0 # timeout=10
 > git checkout -b 10.0 1c3889b3f828fedd0260754163ca65e5644e8e69
 > git rev-list 1c3889b3f828fedd0260754163ca65e5644e8e69 # timeout=10
[Pipeline] } //dir
[Pipeline] Change current directory : End
[Pipeline] stage (Checkout poky)
Entering stage Checkout poky
Proceeding
[Pipeline] Change current directory : Start
Running in /var/jenkins_home/workspace/GENIVI/yocto-baseline-next/poky
[Pipeline] dir {
[Pipeline] git
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url http://git.yoctoproject.org/git/poky.git # timeout=10
Fetching upstream changes from http://git.yoctoproject.org/git/poky.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress http://git.yoctoproject.org/git/poky.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/jethro^{commit} # timeout=10
Checking out Revision 3b223f75eec1738fbc913858e8e11c8305e3edcb (refs/remotes/origin/jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 3b223f75eec1738fbc913858e8e11c8305e3edcb # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D jethro # timeout=10
 > git checkout -b jethro 3b223f75eec1738fbc913858e8e11c8305e3edcb
 > git rev-list 3b223f75eec1738fbc913858e8e11c8305e3edcb # timeout=10
[Pipeline] } //dir
[Pipeline] Change current directory : End
[Pipeline] stage (Checkout meta-openembedded)
Entering stage Checkout meta-openembedded
Proceeding
[Pipeline] Change current directory : Start
Running in /var/jenkins_home/workspace/GENIVI/yocto-baseline-next/meta-openembedded
[Pipeline] dir {
[Pipeline] git
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url git://git.openembedded.org/meta-openembedded # timeout=10
Fetching upstream changes from git://git.openembedded.org/meta-openembedded
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress git://git.openembedded.org/meta-openembedded +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/jethro^{commit} # timeout=10
Checking out Revision c305ac5d2f5285d5eec8952a4ca7f3b4f89aed96 (refs/remotes/origin/jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f c305ac5d2f5285d5eec8952a4ca7f3b4f89aed96 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D jethro # timeout=10
 > git checkout -b jethro c305ac5d2f5285d5eec8952a4ca7f3b4f89aed96
 > git rev-list c305ac5d2f5285d5eec8952a4ca7f3b4f89aed96 # timeout=10
[Pipeline] } //dir
[Pipeline] Change current directory : End
[Pipeline] stage (Checkout baseline_ci_helper)
Entering stage Checkout baseline_ci_helper
Proceeding
[Pipeline] Change current directory : Start
Running in /var/jenkins_home/workspace/GENIVI/yocto-baseline-next/baseline_ci_helper
[Pipeline] dir {
[Pipeline] git
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gunnarx/baseline_ci_helper.git # timeout=10
Fetching upstream changes from https://github.com/gunnarx/baseline_ci_helper.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gunnarx/baseline_ci_helper.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision ece83c165bdb6b97d3f91064aa9d94c584990b64 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f ece83c165bdb6b97d3f91064aa9d94c584990b64 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D master # timeout=10
 > git checkout -b master ece83c165bdb6b97d3f91064aa9d94c584990b64
 > git rev-list ece83c165bdb6b97d3f91064aa9d94c584990b64 # timeout=10
[Pipeline] } //dir
[Pipeline] Change current directory : End
[Pipeline] stage (Checkout correct SHAs)
Entering stage Checkout correct SHAs
Proceeding
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ cd meta-ivi
+ git config user.name Jenkins Agent
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ cd meta-ivi
+ git config user.email no_email@example.com
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ cd baseline_ci_helper
+ sh -c ./checkout_layer_hash.sh poky
Note: checking out 'fc45deac89ef63ca1c44e763c38ced7dfd72cbe1'.

You are in 'detached HEAD' state. You can look around, make experimental
changes and commit them, and you can discard any commits you make in this
state without impacting any branches by performing another checkout.

If you want to create a new branch to retain commits you create, you may
do so (now or later) by using -b with the checkout command again. Example:

  git checkout -b new_branch_name

HEAD is now at fc45dea... build-appliance-image: Update to jethro head revision
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ cd baseline_ci_helper
+ sh -c ./checkout_layer_hash.sh meta-openembedded
Note: checking out 'ad6133a2e95f4b83b6b3ea413598e2cd5fb3fd90'.

You are in 'detached HEAD' state. You can look around, make experimental
changes and commit them, and you can discard any commits you make in this
state without impacting any branches by performing another checkout.

If you want to create a new branch to retain commits you create, you may
do so (now or later) by using -b with the checkout command again. Example:

  git checkout -b new_branch_name

HEAD is now at ad6133a... mce-inject: add recipe
[Pipeline] stage (Inspect sources)
Entering stage Inspect sources
Proceeding
[Pipeline] echo
Inspecting sources
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ pwd
/var/jenkins_home/workspace/GENIVI/yocto-baseline-next
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ ls -la
total 28
drwxr-xr-x  7 build build 4096 Mar 31 11:20 .
drwxr-xr-x  4 root  root  4096 Mar 31 17:16 ..
drwxr-xr-x  3 build build 4096 Mar 31 11:20 baseline_ci_helper
drwxr-xr-x  8 build build 4096 Mar 31 17:12 build
drwxr-xr-x  7 build build 4096 Mar 31 11:19 meta-ivi
drwxr-xr-x 18 build build 4096 Mar 31 11:20 meta-openembedded
drwxr-xr-x 11 build build 4096 Mar 31 17:16 poky
[Pipeline] stage (Build the image)
Entering stage Build the image
Proceeding
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ bash -c export MACHINE=qemux86-64 && export TEMPLATECONF=/var/jenkins_home/workspace/GENIVI/yocto-baseline-next/meta-ivi/meta-ivi/conf && source poky/oe-init-build-env && bitbake leviathan-image

### Shell environment set up for builds. ###

You can now run 'bitbake <target>'

Common targets are:
    leviathan-image
    ivi-image

Loading cache...done.
Loaded 2116 entries from dependency cache.
Parsing recipes...NOTE: INCLUDING less as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
NOTE: INCLUDING libidn as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
NOTE: INCLUDING libidn as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
NOTE: INCLUDING libassuan as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
NOTE: INCLUDING libtasn1 as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
NOTE: INCLUDING gnutls as buildable despite INCOMPATIBLE_LICENSE because it has been whitelisted
done.
Parsing of 1593 .bb files complete (362 cached, 1231 parsed). 2115 targets, 431 skipped, 0 masked, 0 errors.
NOTE: Resolving any missing task queue dependencies

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "x86_64-poky-linux"
MACHINE           = "qemux86-64"
DISTRO            = "poky-ivi-systemd"
DISTRO_VERSION    = "10.0+snapshot-20160331"
TUNE_FEATURES     = "m64 core2"
TARGET_FPU        = ""
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:fc45deac89ef63ca1c44e763c38ced7dfd72cbe1"
meta-oe           
meta-filesystems  = "HEAD:ad6133a2e95f4b83b6b3ea413598e2cd5fb3fd90"
meta-ivi          
meta-ivi-bsp      = "10.0:1c3889b3f828fedd0260754163ca65e5644e8e69"

NOTE: Preparing RunQueue
NOTE: Executing SetScene Tasks
NOTE: Executing RunQueue Tasks
NOTE: Tasks Summary: Attempted 3270 tasks of which 3270 didn't need to be rerun and all succeeded.
[Pipeline] stage (Inspect results)
Entering stage Inspect results
Proceeding
[Pipeline] echo
Inspecting results
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ pwd
/var/jenkins_home/workspace/GENIVI/yocto-baseline-next
[Pipeline] sh
[yocto-baseline-next] Running shell script
+ ls -laR build/tmp/deploy/images
build/tmp/deploy/images:
total 12
drwxr-xr-x 3 build build 4096 Mar 31 16:14 .
drwxr-xr-x 5 build build 4096 Mar 31 16:59 ..
drwxr-xr-x 2 build build 4096 Mar 31 17:14 qemux86-64

build/tmp/deploy/images/qemux86-64:
total 280140
drwxr-xr-x 2 build build      4096 Mar 31 17:14 .
drwxr-xr-x 3 build build      4096 Mar 31 16:14 ..
-rw-r--r-- 2 build build       294 Mar 31 17:10 README_-_DO_NOT_DELETE_FILES_IN_THIS_DIRECTORY.txt
lrwxrwxrwx 1 build build        76 Mar 31 16:14 bzImage -> bzImage--3.14.36+git0+060fa80b79_578602a722-r0-qemux86-64-20160331112227.bin
-rw-r--r-- 2 build build   6630560 Mar 31 16:13 bzImage--3.14.36+git0+060fa80b79_578602a722-r0-qemux86-64-20160331112227.bin
lrwxrwxrwx 1 build build        76 Mar 31 16:14 bzImage-qemux86-64.bin -> bzImage--3.14.36+git0+060fa80b79_578602a722-r0-qemux86-64-20160331112227.bin
-rw-r--r-- 1 build build 264473600 Mar 31 17:14 leviathan-image-qemux86-64-20160331112227.rootfs.ext4
-rw-r--r-- 1 build build     35606 Mar 31 17:12 leviathan-image-qemux86-64-20160331112227.rootfs.manifest
-rw-r--r-- 1 build build  50292037 Mar 31 17:14 leviathan-image-qemux86-64-20160331112227.rootfs.tar.bz2
lrwxrwxrwx 1 build build        53 Mar 31 17:14 leviathan-image-qemux86-64.ext4 -> leviathan-image-qemux86-64-20160331112227.rootfs.ext4
lrwxrwxrwx 1 build build        57 Mar 31 17:14 leviathan-image-qemux86-64.manifest -> leviathan-image-qemux86-64-20160331112227.rootfs.manifest
lrwxrwxrwx 1 build build        56 Mar 31 17:14 leviathan-image-qemux86-64.tar.bz2 -> leviathan-image-qemux86-64-20160331112227.rootfs.tar.bz2
-rw-r--r-- 2 build build  70578482 Mar 31 16:14 modules--3.14.36+git0+060fa80b79_578602a722-r0-qemux86-64-20160331112227.tgz
lrwxrwxrwx 1 build build        76 Mar 31 16:14 modules-qemux86-64.tgz -> modules--3.14.36+git0+060fa80b79_578602a722-r0-qemux86-64-20160331112227.tgz
[Pipeline] } //withDockerContainer
$ docker stop 3d4650282d76021f94e201cbe12d4e3d6a5c71747184df0597ef286e2b29a062
$ docker rm -f 3d4650282d76021f94e201cbe12d4e3d6a5c71747184df0597ef286e2b29a062
[Pipeline] Run build steps inside a Docker container : End
[Pipeline] } //node
[Pipeline] Allocate node : End
[Pipeline] End of Pipeline
Finished: SUCCESS
```

**NOTE**: A full build starting from an empty workspace takes about TODO hours to complete (Docker Engine running on a dual-core AMD Opteron(TM) Processor 6276 CPU X5450 @2300 MHz, 4 GB RAM).

<!-- EOF -->
