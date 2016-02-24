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

<!-- (2016-02-23 16:00 CET) -->

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
 > git remote # timeout=10
 > git submodule init # timeout=10
 > git submodule sync # timeout=10
 > git config --get remote.origin.url # timeout=10
 > git submodule update --init --recursive
Docker container 
...
| [ 61%] [ 61%] Generating country_BO.png
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/rsvg-convert --width=32 --height=32 --output /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/country_BO.png /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_BO.svgz
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_IO.svgz: Start tag expected, '<' not found
| 
| 
| Generating country_UA.png
| make[2]: *** [navit/xpm/country_IO.png] Error 1
| make[2]: *** Waiting for unfinished jobs....
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/rsvg-convert --width=32 --height=32 --output /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/country_UA.png /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_UA.svgz
| Generating country_GH.png
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/rsvg-convert --width=32 --height=32 --output /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/country_GH.png /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_GH.svgz
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_BO.svgz: Start tag expected, '<' not found
| 
| 
| make[2]: *** [navit/xpm/country_BO.png] Error 1
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_UA.svgz: Start tag expected, '<' not found
| 
| 
| make[2]: *** [navit/xpm/country_UA.png] Error 1
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_GH.svgz: Start tag expected, '<' not found
| 
| 
| make[2]: *** [navit/xpm/country_GH.png] Error 1
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[1]: *** [navit/xpm/CMakeFiles/images.dir/all] Error 2
| make[1]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make: *** [all] Error 2
| ERROR: oe_runmake failed
| WARNING: exit code 1 from a shell command.
| ERROR: Function failed: do_compile (log file is located at /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/temp/log.do_compile.6119)
Currently 6 running tasks (2671 of 4460):
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: python-dbus-1.2.0-r0 do_populate_sysroot (pid 15604)
[413A[J[1;31mERROR[0m: [31mTask 1928 (/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../meta-genivi-demo/recipes-navigation/navit/navit_svn.bb, do_compile) failed with exit code '1'[0m
Currently 6 running tasks (2671 of 4460):
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: python-dbus-1.2.0-r0 do_populate_sysroot (pid 15604)
[413A[JCurrently 6 running tasks (2672 of 4460):
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: python-dbus-1.2.0-r0 do_populate_sysroot (pid 15604)
[413A[JWaiting for 7 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: python-dbus-1.2.0-r0 do_populate_sysroot (pid 15604)
6: alsa-state-0.2.0-r5 do_packagedata (pid 15924)
[460A[JWaiting for 8 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: python-dbus-1.2.0-r0 do_populate_sysroot (pid 15604)
6: alsa-state-0.2.0-r5 do_packagedata (pid 15924)
7: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[541A[JWaiting for 7 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: alsa-state-0.2.0-r5 do_packagedata (pid 15924)
6: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[485A[JWaiting for 6 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: rpm-5.4.14-r0 do_package (pid 6284)
5: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[435A[JWaiting for 5 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtmultimedia-5.4.2+gitAUTOINC+cc0569a038-r0 do_compile (pid 2026)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
3: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
4: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[396A[JWaiting for 4 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
2: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_ipk (pid 3298)
3: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[327A[JWaiting for 3 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
2: qtxmlpatterns-5.4.2+gitAUTOINC+c21924d67a-r0 do_package_write_rpm (pid 15707)
[254A[JWaiting for 2 running tasks to finish:
0: qt3d-5.4.2+gitAUTOINC+8a9723d742-r0 do_compile (pid 1987)
1: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
[173A[JWaiting for 1 running tasks to finish:
0: qtbase-5.4.2+gitAUTOINC+2cb17c1fb9-r0 do_package_write_rpm (pid 2387)
[112A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 2672 tasks of which 2623 didn't need to be rerun and 1 failed.[0m
Waiting for 0 running tasks to finish:

Summary: 1 task failed:
  /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../meta-genivi-demo/recipes-navigation/navit/navit_svn.bb, do_compile
Summary: There were 6 WARNING messages shown.
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
