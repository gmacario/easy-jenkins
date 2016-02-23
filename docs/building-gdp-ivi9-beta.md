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
 > git rev-parse refs/remotes/origin/qemux86-64^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/qemux86-64^{commit} # timeout=10
Checking out Revision 6e50965700f98572eaa731e426d561b1b5031c87 (refs/remotes/origin/qemux86-64)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 6e50965700f98572eaa731e426d561b1b5031c87
 > git rev-list 6e50965700f98572eaa731e426d561b1b5031c87 # timeout=10
 > git remote # timeout=10
 > git submodule init # timeout=10
 > git submodule sync # timeout=10
 > git config --get remote.origin.url # timeout=10
 > git submodule update --init --recursive
...
| [ 12%] Generating country_GE.png
| cd /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm && /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/sysroots/x86_64-linux/usr/bin/rsvg-convert --width=32 --height=32 --output /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build/navit/xpm/country_GE.png /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_GE.svgz
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_UG.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_LV.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_GE.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_GH.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_BM.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_CO.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_SR.svgz: Start tag expected, '<' not found
| 
| 
| Error reading SVG:Error domain 1 code 4 on line 1 column 1 of file:///var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/navit/navit/xpm/country_VN.svgz: Start tag expected, '<' not found
| 
| 
| make[2]: *** [navit/xpm/country_CO.png] Error 1
| make[2]: *** Waiting for unfinished jobs....
| make[2]: *** [navit/xpm/country_LV.png] Error 1
| make[2]: *** [navit/xpm/country_GE.png] Error 1
| make[2]: *** [navit/xpm/country_VN.png] Error 1
| make[2]: *** [navit/xpm/country_BM.png] Error 1
| make[2]: *** [navit/xpm/country_GH.png] Error 1
| make[2]: *** [navit/xpm/country_UG.png] Error 1
| make[2]: *** [navit/xpm/country_SR.png] Error 1
| make[2]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make[1]: *** [navit/xpm/CMakeFiles/images.dir/all] Error 2
| make[1]: Leaving directory `/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/build'
| make: *** [all] Error 2
| ERROR: oe_runmake failed
| WARNING: exit code 1 from a shell command.
| ERROR: Function failed: do_compile (log file is located at /var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/tmp/work/core2-64-poky-linux/navit/1_0.2.0+svnr5532-r9.3/temp/log.do_compile.467)
Currently 7 running tasks (2593 of 4460):
0: qtdeclarative-5.4.2+gitAUTOINC+2fdb6eba0a-r0 do_install (pid 464)
1: alsa-utils-1.0.28-r1 do_compile (pid 474)
2: glib-networking-2.38.0-r0 do_configure (pid 475)
3: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
4: xproto-1_7.0.26-r0 do_package (pid 482)
5: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
6: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
[418A[J[1;31mERROR[0m: [31mTask 1928 (/var/jenkins_home/workspace/GENIVI/build_gdp_ivi9_beta/gdp-src-build/../meta-genivi-demo/recipes-navigation/navit/navit_svn.bb, do_compile) failed with exit code '1'[0m
Currently 7 running tasks (2593 of 4460):
0: qtdeclarative-5.4.2+gitAUTOINC+2fdb6eba0a-r0 do_install (pid 464)
1: alsa-utils-1.0.28-r1 do_compile (pid 474)
2: glib-networking-2.38.0-r0 do_configure (pid 475)
3: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
4: xproto-1_7.0.26-r0 do_package (pid 482)
5: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
6: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
[418A[JCurrently 7 running tasks (2594 of 4460):
0: qtdeclarative-5.4.2+gitAUTOINC+2fdb6eba0a-r0 do_install (pid 464)
1: alsa-utils-1.0.28-r1 do_compile (pid 474)
2: glib-networking-2.38.0-r0 do_configure (pid 475)
3: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
4: xproto-1_7.0.26-r0 do_package (pid 482)
5: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
6: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
[418A[JWaiting for 8 running tasks to finish:
0: qtdeclarative-5.4.2+gitAUTOINC+2fdb6eba0a-r0 do_install (pid 464)
1: alsa-utils-1.0.28-r1 do_compile (pid 474)
2: glib-networking-2.38.0-r0 do_configure (pid 475)
3: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
4: xproto-1_7.0.26-r0 do_package (pid 482)
5: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
6: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
7: keyutils-1.5.9-r1 do_package (pid 1443)
[458A[JWaiting for 7 running tasks to finish:
0: alsa-utils-1.0.28-r1 do_compile (pid 474)
1: glib-networking-2.38.0-r0 do_configure (pid 475)
2: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
3: xproto-1_7.0.26-r0 do_package (pid 482)
4: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
5: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
6: keyutils-1.5.9-r1 do_package (pid 1443)
[389A[JWaiting for 6 running tasks to finish:
0: glib-networking-2.38.0-r0 do_configure (pid 475)
1: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
2: xproto-1_7.0.26-r0 do_package (pid 482)
3: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
4: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
5: keyutils-1.5.9-r1 do_package (pid 1443)
[344A[JWaiting for 5 running tasks to finish:
0: glib-networking-2.38.0-r0 do_configure (pid 475)
1: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
2: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
3: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
4: keyutils-1.5.9-r1 do_package (pid 1443)
[301A[JWaiting for 4 running tasks to finish:
0: glib-networking-2.38.0-r0 do_configure (pid 475)
1: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
2: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
3: qtscript-5.4.2+gitAUTOINC+eb08742e8f-r0 do_package (pid 724)
[258A[JWaiting for 3 running tasks to finish:
0: glib-networking-2.38.0-r0 do_configure (pid 475)
1: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
2: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
[194A[JWaiting for 2 running tasks to finish:
0: mesa-2_10.4.4-r1 do_package_write_ipk (pid 476)
1: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
[142A[JWaiting for 1 running tasks to finish:
0: pulseaudio-6.0-r1 do_package_write_ipk (pid 479)
[91A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 2594 tasks of which 2583 didn't need to be rerun and 1 failed.[0m
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
