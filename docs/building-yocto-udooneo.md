# Building a Yocto distribution for the UDOO Neo

**WORK-IN-PROGRESS**

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

# Configure git
git config --global user.name "easy-jenkins"
git config --global user.email "$(whoami)@$(hostname)"

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Worksaround for https://github.com/gmacario/easy-jenkins/issues/57
bitbake m4-firmware

bitbake core-image-minimal
# bitbake udoo-image-full-cmdline
# bitbake genivi-demo-platform

# EOF
```
  
  then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-24 14:55 CET): Tested on mv-linux-powerhorse -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_yocto_udooneo/lastBuild/console`

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/genivi-demo-platform
 > git init /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace # timeout=10
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
Checking out Revision c83152c2cc1e87d0f6adfcc45358331635f34618 (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f c83152c2cc1e87d0f6adfcc45358331635f34618
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto from repository ...
$ docker pull gmacario/build-yocto
...
Currently 2 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
1: core-image-minimal-1.0-r0 do_rootfs (pid 12809)
[137A[J[1;33mWARNING[0m: [33mQA Issue: python: /python-distutils-staticdev/usr/lib/python2.7/config/libpython2.7.a is owned by uid 0, which is the same as the user running bitbake. This may be due to host contamination [host-user-contaminated][0m
Currently 2 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
1: core-image-minimal-1.0-r0 do_rootfs (pid 12809)
[137A[J[1;31mERROR[0m: [31mError: The image creation script '/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/tmp-glibc/work/udooneo-oe-linux-gnueabi/core-image-minimal/1.0-r0/temp/create_image.sdcard' returned 1:
0+0 records in
0+0 records out
0 bytes (0 B) copied, 7.0948e-05 s, 0.0 kB/s
Model:  (file)
Disk /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/tmp-glibc/deploy/images/udooneo/core-image-minimal-udooneo-20160224140133.rootfs.sdcard: 25.2MB
Sector size (logical/physical): 512B/512B
Partition Table: msdos
Disk Flags: 

Number  Start   End     Size    Type     File system  Flags
 1      4194kB  12.6MB  8389kB  primary               lba
 2      12.6MB  21.0MB  8389kB  primary

62+0 records in
62+0 records out
31744 bytes (32 kB) copied, 0.000191588 s, 166 MB/s
225+1 records in
225+1 records out
231196 bytes (231 kB) copied, 0.000712553 s, 324 MB/s
mkfs.fat: warning - lowercase labels might not work properly with DOS or Windows
mkfs.fat 3.0.28 (2015-05-16)
/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/tmp-glibc/deploy/images/udooneo/m4clean.fw: No such file or directory
WARNING: exit code 1 from a shell command.
[0m
Currently 2 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
1: core-image-minimal-1.0-r0 do_rootfs (pid 12809)
[137A[J[1;31mERROR[0m: [31mFunction failed: do_rootfs[0m
Currently 2 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
1: core-image-minimal-1.0-r0 do_rootfs (pid 12809)
[137A[J[1;31mERROR[0m: [31mLogfile of failure stored in: /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/tmp-glibc/work/udooneo-oe-linux-gnueabi/core-image-minimal/1.0-r0/temp/log.do_rootfs.12809[0m
Currently 1 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
[86A[J[1;31mERROR[0m: [31mTask 7 (/var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/../poky/meta/recipes-core/images/core-image-minimal.bb, do_rootfs) failed with exit code '1'[0m
Currently 1 running tasks (1301 of 1303):
0: python-2.7.9-r1 do_package_qa (pid 7285)
[86A[J[1;33mWARNING[0m: [33mQA Issue: python: /python-dbg/usr/lib/python2.7/lib-dynload/.debug/crypt.so is owned by uid 0, which is the same as the user running bitbake. This may be due to host contamination [host-user-contaminated][0m
Waiting for 1 running tasks to finish:
0: python-2.7.9-r1 do_package_qa (pid 7285)
[83A[J[1;33mWARNING[0m: [33mQA Issue: python: /python-audio/usr/lib/python2.7/lib-dynload/audioop.so is owned by uid 0, which is the same as the user running bitbake. This may be due to host contamination [host-user-contaminated][0m
Waiting for 1 running tasks to finish:
0: python-2.7.9-r1 do_package_qa (pid 7285)
[83A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 1301 tasks of which 9 didn't need to be rerun and 1 failed.[0m
Waiting for 0 running tasks to finish:

Summary: 1 task failed:
  /var/jenkins_home/jobs/GENIVI/jobs/build_yocto_udooneo/workspace/gdp-src-build/../poky/meta/recipes-core/images/core-image-minimal.bb, do_rootfs
Summary: There were 1926 WARNING messages shown.
Summary: There were 2 ERROR messages shown, returning a non-zero exit code.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

Result: FAILURE in core-image-minimal.bb, do_rootfs

<!-- EOF -->
