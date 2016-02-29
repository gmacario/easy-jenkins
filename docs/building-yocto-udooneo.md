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
        - Branch Specifier (blank for 'any'): `*/dev-udooneo-jethro`
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
printenv | sort
cat /etc/passwd

# Workaround in case USER is undefined
[ "$USER" = "" ] && export USER=jenkins

# Configure git
git config --global user.name "easy-jenkins"
git config --global user.email "$USER@$(hostname)"

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Workaround for "DISTRO 'poky-ivi-systemd' not found."
export DISTRO="poky"

# Workaround for https://github.com/gmacario/easy-jenkins/issues/57
bitbake m4-firmware

bitbake core-image-minimal
bitbake udoo-image-full-cmdline
# bitbake genivi-demo-platform

# EOF
```
- Post-build Actions
    - Archive the artifacts
      - Files to archive: `*/tmp*/deploy/images/**/*.sdcard.gz`

then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-28 12:17 CET): Tested on dc7600-gm -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_yocto_udooneo/lastBuild/console`

<!-- (2016-02-29 15:25 CET) http://mv-linux-powerhorse.solarma.it:9080/job/build_yocto_udooneo/lastBuild/consoleText -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/build_yocto_udooneo
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/dev-udooneo-jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/dev-udooneo-jethro^{commit} # timeout=10
Checking out Revision 5d7941d796519ec4d88da900aa4aa4807efb70dd (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 5d7941d796519ec4d88da900aa4aa4807efb70dd
 > git rev-list 5d7941d796519ec4d88da900aa4aa4807efb70dd # timeout=10
Docker container a8f65c4e68297b4c267791dca3df4d2ad3d5315aa2e3463f1519d6280bff3e51 started to host the build
$ docker exec --tty a8f65c4e68297b4c267791dca3df4d2ad3d5315aa2e3463f1519d6280bff3e51 env
[build_yocto_udooneo] $ docker exec --tty --user 1000:1000 a8f65c4e68297b4c267791dca3df4d2ad3d5315aa2e3463f1519d6280bff3e51 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#9 BUILD_ID=9 BUILD_NUMBER=9 BUILD_TAG=jenkins-build_yocto_udooneo-9 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/dev-udooneo-jethro GIT_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd GIT_PREVIOUS_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd GIT_PREVIOUS_SUCCESSFUL_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd GIT_URL=https://github.com/gmacario/genivi-demo-platform HOME=/var/jenkins_home HOSTNAME=1dddbffd6d7e HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=2c5d611b8254ec31 JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=2c5d611b8254ec31 JENKINS_SHA=e72e06e64d23eefb13090459f517b0697aad7be0 JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.2 JOB_NAME=build_yocto_udooneo LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/workspace/build_yocto_udooneo /bin/bash -xe /tmp/hudson5501682872211725896.sh
+ id
uid=1000 gid=1000
+ pwd
/var/jenkins_home/workspace/build_yocto_udooneo
+ ls -la
total 64
drwxr-xr-x 12 1000 1000 4096 Feb 29 08:59 .
drwxr-xr-x  7 1000 1000 4096 Feb 29 10:10 ..
drwxr-xr-x  9 1000 1000 4096 Feb 29 14:00 .git
-rw-r--r--  1 1000 1000  204 Feb 29 08:59 .gitignore
-rw-r--r--  1 1000 1000  938 Feb 29 08:59 .gitmodules
-rw-r--r--  1 1000 1000  805 Feb 29 08:59 README.md
drwxr-xr-x  7 1000 1000 4096 Feb 29 09:02 gdp-src-build
-rw-r--r--  1 1000 1000  908 Feb 29 08:59 init.sh
drwxr-xr-x 19 1000 1000 4096 Feb 29 10:55 meta-fsl-arm
drwxr-xr-x  7 1000 1000 4096 Feb 29 08:59 meta-fsl-arm-extra
drwxr-xr-x  5 1000 1000 4096 Feb 29 08:59 meta-fsl-demos
drwxr-xr-x 15 1000 1000 4096 Feb 29 10:55 meta-genivi-demo
drwxr-xr-x 17 1000 1000 4096 Feb 29 09:00 meta-openembedded
drwxr-xr-x  9 1000 1000 4096 Feb 29 10:55 meta-qt5
drwxr-xr-x 10 1000 1000 4096 Feb 29 09:00 meta-udoo
drwxr-xr-x 10 1000 1000 4096 Feb 29 09:01 poky
+ printenv
+ sort
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
BUILD_DISPLAY_NAME=#9
BUILD_ID=9
BUILD_NUMBER=9
BUILD_TAG=jenkins-build_yocto_udooneo-9
CA_CERTIFICATES_JAVA_VERSION=20140324
CLASSPATH=
COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log
EXECUTOR_NUMBER=1
GIT_BRANCH=origin/dev-udooneo-jethro
GIT_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd
GIT_PREVIOUS_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd
GIT_PREVIOUS_SUCCESSFUL_COMMIT=5d7941d796519ec4d88da900aa4aa4807efb70dd
GIT_URL=https://github.com/gmacario/genivi-demo-platform
HOME=/var/jenkins_home
HOSTNAME=1dddbffd6d7e
HUDSON_HOME=/var/jenkins_home
HUDSON_SERVER_COOKIE=2c5d611b8254ec31
JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_VERSION=8u72
JENKINS_HOME=/var/jenkins_home
JENKINS_SERVER_COOKIE=2c5d611b8254ec31
JENKINS_SHA=e72e06e64d23eefb13090459f517b0697aad7be0
JENKINS_SLAVE_AGENT_PORT=50000
JENKINS_UC=https://updates.jenkins-ci.org
JENKINS_VERSION=1.642.2
JOB_NAME=build_yocto_udooneo
LANG=C.UTF-8
NODE_LABELS=master
NODE_NAME=master
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
PWD=/var/jenkins_home/workspace/build_yocto_udooneo
ROOT_BUILD_CAUSE=MANUALTRIGGER
ROOT_BUILD_CAUSE_MANUALTRIGGER=true
SHLVL=3
TERM=xterm
TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888
WORKSPACE=/var/jenkins_home/workspace/build_yocto_udooneo
_=/usr/bin/printenv
}
+ cat /etc/passwd
root:x:0:0:root:/root:/bin/bash
daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin
bin:x:2:2:bin:/bin:/usr/sbin/nologin
sys:x:3:3:sys:/dev:/usr/sbin/nologin
sync:x:4:65534:sync:/bin:/bin/sync
games:x:5:60:games:/usr/games:/usr/sbin/nologin
man:x:6:12:man:/var/cache/man:/usr/sbin/nologin
lp:x:7:7:lp:/var/spool/lpd:/usr/sbin/nologin
mail:x:8:8:mail:/var/mail:/usr/sbin/nologin
news:x:9:9:news:/var/spool/news:/usr/sbin/nologin
uucp:x:10:10:uucp:/var/spool/uucp:/usr/sbin/nologin
proxy:x:13:13:proxy:/bin:/usr/sbin/nologin
www-data:x:33:33:www-data:/var/www:/usr/sbin/nologin
backup:x:34:34:backup:/var/backups:/usr/sbin/nologin
list:x:38:38:Mailing List Manager:/var/list:/usr/sbin/nologin
irc:x:39:39:ircd:/var/run/ircd:/usr/sbin/nologin
gnats:x:41:41:Gnats Bug-Reporting System (admin):/var/lib/gnats:/usr/sbin/nologin
nobody:x:65534:65534:nobody:/nonexistent:/usr/sbin/nologin
libuuid:x:100:101::/var/lib/libuuid:
syslog:x:101:104::/home/syslog:/bin/false
build:x:30000:30000::/home/build:
+ '[' '' = '' ']'
+ export USER=jenkins
+ USER=jenkins
+ git config --global user.name easy-jenkins
++ hostname
+ git config --global user.email jenkins@a8f65c4e6829
+ source init.sh
+++ basename /tmp/hudson5501682872211725896.sh
++ cmd=hudson5501682872211725896.sh
++ '[' hudson5501682872211725896.sh = init.sh ']'
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
++ source poky/oe-init-build-env gdp-src-build
+++ '[' -n poky/oe-init-build-env ']'
++++ dirname poky/oe-init-build-env
+++ OEROOT=poky
+++ '[' -n '' ']'
+++ THIS_SCRIPT=poky/oe-init-build-env
+++ '[' -z '' ']'
+++ '[' /tmp/hudson5501682872211725896.sh = poky/oe-init-build-env ']'
++++ readlink -f poky
+++ OEROOT=/var/jenkins_home/workspace/build_yocto_udooneo/poky
+++ export OEROOT
+++ . /var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts/oe-buildenv-internal
++++ '[' -z /var/jenkins_home/workspace/build_yocto_udooneo/poky ']'
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
++++ BDIR=/var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build
++++ '[' -z /var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build ']'
++++ '[' x '!=' x ']'
++++ expr /var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build : '/.*'
++++ BUILDDIR=/var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build
++++ unset BDIR
++++ '[' x = x ']'
++++ BITBAKEDIR=/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/
+++++ readlink -f /var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/
++++ BITBAKEDIR=/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake
+++++ readlink -f /var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build
++++ BUILDDIR=/var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build
++++ test -d /var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake
++++ NEWPATHS=/var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts:/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/bin:
+++++ echo /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+++++ sed -e 's|:/var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts:/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/bin:|:|g' -e 's|^/var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts:/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/bin:||'
++++ PATH=/var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts:/var/jenkins_home/workspace/build_yocto_udooneo/poky/bitbake/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
++++ unset BITBAKEDIR NEWPATHS
++++ export BUILDDIR
++++ export PATH
++++ export 'BB_ENV_EXTRAWHITE=MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy FTPS_PROXY ftps_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR'
++++ BB_ENV_EXTRAWHITE='MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy FTPS_PROXY ftps_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR'
+++ /var/jenkins_home/workspace/build_yocto_udooneo/poky/scripts/oe-setup-builddir

### Shell environment set up for builds. ###

You can now run 'bitbake <target>'

Common targets are:
    core-image-minimal
    core-image-sato
    meta-toolchain
    adt-installer
    meta-ide-support

You can also run generated qemu images with a command like 'runqemu qemux86'
+++ '[' -n /var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build ']'
+++ cd /var/jenkins_home/workspace/build_yocto_udooneo/gdp-src-build
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
whoami: cannot find name for user ID 1000
+ '[' = root ']'
/tmp/hudson5501682872211725896.sh: line 21: [: =: unary operator expected
+ export DISTRO=poky
+ DISTRO=poky
+ bitbake m4-firmware
Loading cache:   0% || ETA:  --:--:--
Loading cache:   1% || ETA:  00:00:00
Loading cache:   2% || ETA:  00:00:00
Loading cache:   3% || ETA:  00:00:00
Loading cache:   4% || ETA:  00:00:00
Loading cache:   5% || ETA:  00:00:00
Loading cache:   6% || ETA:  00:00:00
Loading cache:   7% || ETA:  00:00:00
Loading cache:   8% || ETA:  00:00:00
Loading cache:   9% || ETA:  00:00:00
Loading cache:  10% || ETA:  00:00:00
Loading cache:  11% || ETA:  00:00:00
Loading cache:  12% || ETA:  00:00:00
Loading cache:  13% || ETA:  00:00:00
Loading cache:  14% || ETA:  00:00:00
Loading cache:  15% || ETA:  00:00:00
Loading cache:  16% || ETA:  00:00:00
Loading cache:  17% || ETA:  00:00:00
Loading cache:  18% || ETA:  00:00:00
Loading cache:  19% || ETA:  00:00:00
Loading cache:  20% || ETA:  00:00:00
Loading cache:  21% || ETA:  00:00:00
Loading cache:  22% || ETA:  00:00:00
Loading cache:  23% || ETA:  00:00:00
Loading cache:  24% || ETA:  00:00:00
Loading cache:  25% || ETA:  00:00:00
Loading cache:  26% || ETA:  00:00:00
Loading cache:  27% || ETA:  00:00:00
Loading cache:  28% || ETA:  00:00:00
Loading cache:  29% || ETA:  00:00:00
Loading cache:  30% || ETA:  00:00:00
Loading cache:  31% || ETA:  00:00:00
Loading cache:  32% || ETA:  00:00:00
Loading cache:  33% || ETA:  00:00:00
Loading cache:  34% || ETA:  00:00:00
Loading cache:  35% || ETA:  00:00:00
Loading cache:  36% || ETA:  00:00:00
Loading cache:  37% || ETA:  00:00:00
Loading cache:  38% || ETA:  00:00:00
Loading cache:  39% || ETA:  00:00:00
Loading cache:  40% || ETA:  00:00:00
Loading cache:  41% || ETA:  00:00:00
Loading cache:  42% || ETA:  00:00:00
Loading cache:  43% || ETA:  00:00:00
Loading cache:  44% || ETA:  00:00:00
Loading cache:  45% || ETA:  00:00:00
Loading cache:  46% || ETA:  00:00:00
Loading cache:  47% || ETA:  00:00:00
Loading cache:  48% || ETA:  00:00:00
Loading cache:  49% || ETA:  00:00:00
Loading cache:  50% || ETA:  00:00:00
Loading cache:  51% || ETA:  00:00:00
Loading cache:  52% || ETA:  00:00:00
Loading cache:  53% || ETA:  00:00:00
Loading cache:  54% || ETA:  00:00:00
Loading cache:  55% || ETA:  00:00:00
Loading cache:  56% || ETA:  00:00:00
Loading cache:  57% || ETA:  00:00:00
Loading cache:  58% || ETA:  00:00:00
Loading cache:  59% || ETA:  00:00:00
Loading cache:  60% || ETA:  00:00:00
Loading cache:  61% || ETA:  00:00:00
Loading cache:  62% || ETA:  00:00:00
Loading cache:  63% || ETA:  00:00:00
Loading cache:  64% || ETA:  00:00:00
Loading cache:  65% || ETA:  00:00:00
Loading cache:  66% || ETA:  00:00:00
Loading cache:  67% || ETA:  00:00:00
Loading cache:  68% || ETA:  00:00:00
Loading cache:  69% || ETA:  00:00:00
Loading cache:  70% || ETA:  00:00:00
Loading cache:  71% || ETA:  00:00:00
Loading cache:  72% || ETA:  00:00:00
Loading cache:  73% || ETA:  00:00:00
Loading cache:  74% || ETA:  00:00:00
Loading cache:  75% || ETA:  00:00:00
Loading cache:  76% || ETA:  00:00:00
Loading cache:  77% || ETA:  00:00:00
Loading cache:  78% || ETA:  00:00:00
Loading cache:  79% || ETA:  00:00:00
Loading cache:  80% || ETA:  00:00:00
Loading cache:  81% || ETA:  00:00:00
Loading cache:  82% || ETA:  00:00:00
Loading cache:  83% || ETA:  00:00:00
Loading cache:  84% || ETA:  00:00:00
Loading cache:  85% || ETA:  00:00:00
Loading cache:  86% || ETA:  00:00:00
Loading cache:  87% || ETA:  00:00:00
Loading cache:  88% || ETA:  00:00:00
Loading cache:  89% || ETA:  00:00:00
Loading cache:  90% || ETA:  00:00:00
Loading cache:  91% || ETA:  00:00:00
Loading cache:  92% || ETA:  00:00:00
Loading cache:  93% || ETA:  00:00:00
Loading cache:  94% || ETA:  00:00:00
Loading cache:  95% || ETA:  00:00:00
Loading cache:  96% || ETA:  00:00:00
Loading cache:  97% || ETA:  00:00:00
Loading cache:  98% || ETA:  00:00:00
Loading cache:  99% || ETA:  00:00:00
Loading cache: 100% || ETA:  00:00:00

Loaded 2370 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "arm-poky-linux-gnueabi"
MACHINE           = "udooneo"
DISTRO            = "poky"
DISTRO_VERSION    = "2.0.1"
TUNE_FEATURES     = "arm armv7a vfp thumb neon callconvention-hard cortexa9"
TARGET_FPU        = "vfp-neon"
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:b1f23d1254682866236bfaeb843c0d8aa332efc2"
meta-networking   
meta-oe           
meta-python       = "HEAD:dc5634968b270dde250690609f0015f881db81f2"
meta-udoo         = "HEAD:93ce58963bb8155c0369c3797d695c08f854d30c"
meta-fsl-arm      = "HEAD:8d22b44716fa624ff87383c36d222d9e28d0b267"
meta-fsl-demos    = "HEAD:2231e946e7a94d096394f2b2477e8184c9bbde7b"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
[1;29mNOTE[0m: [29mTasks Summary: Attempted 386 tasks of which 386 didn't need to be rerun and all succeeded.[0m
+ bitbake core-image-minimal
Loading cache:   0% || ETA:  --:--:--
Loading cache:   1% || ETA:  00:00:01
Loading cache:   2% || ETA:  00:00:00
Loading cache:   3% || ETA:  00:00:00
Loading cache:   4% || ETA:  00:00:00
Loading cache:   5% || ETA:  00:00:00
Loading cache:   6% || ETA:  00:00:00
Loading cache:   7% || ETA:  00:00:00
Loading cache:   8% || ETA:  00:00:00
Loading cache:   9% || ETA:  00:00:00
Loading cache:  10% || ETA:  00:00:00
Loading cache:  11% || ETA:  00:00:00
Loading cache:  12% || ETA:  00:00:00
Loading cache:  13% || ETA:  00:00:00
Loading cache:  14% || ETA:  00:00:00
Loading cache:  15% || ETA:  00:00:00
Loading cache:  16% || ETA:  00:00:00
Loading cache:  17% || ETA:  00:00:00
Loading cache:  18% || ETA:  00:00:00
Loading cache:  19% || ETA:  00:00:00
Loading cache:  20% || ETA:  00:00:00
Loading cache:  21% || ETA:  00:00:00
Loading cache:  22% || ETA:  00:00:00
Loading cache:  23% || ETA:  00:00:00
Loading cache:  24% || ETA:  00:00:00
Loading cache:  25% || ETA:  00:00:00
Loading cache:  26% || ETA:  00:00:00
Loading cache:  27% || ETA:  00:00:00
Loading cache:  28% || ETA:  00:00:00
Loading cache:  29% || ETA:  00:00:00
Loading cache:  30% || ETA:  00:00:00
Loading cache:  31% || ETA:  00:00:00
Loading cache:  32% || ETA:  00:00:00
Loading cache:  33% || ETA:  00:00:00
Loading cache:  34% || ETA:  00:00:00
Loading cache:  35% || ETA:  00:00:00
Loading cache:  36% || ETA:  00:00:00
Loading cache:  37% || ETA:  00:00:00
Loading cache:  38% || ETA:  00:00:00
Loading cache:  39% || ETA:  00:00:00
Loading cache:  40% || ETA:  00:00:00
Loading cache:  41% || ETA:  00:00:00
Loading cache:  42% || ETA:  00:00:00
Loading cache:  43% || ETA:  00:00:00
Loading cache:  44% || ETA:  00:00:00
Loading cache:  45% || ETA:  00:00:00
Loading cache:  46% || ETA:  00:00:00
Loading cache:  47% || ETA:  00:00:00
Loading cache:  48% || ETA:  00:00:00
Loading cache:  49% || ETA:  00:00:00
Loading cache:  50% || ETA:  00:00:00
Loading cache:  51% || ETA:  00:00:00
Loading cache:  52% || ETA:  00:00:00
Loading cache:  53% || ETA:  00:00:00
Loading cache:  54% || ETA:  00:00:00
Loading cache:  55% || ETA:  00:00:00
Loading cache:  56% || ETA:  00:00:00
Loading cache:  57% || ETA:  00:00:00
Loading cache:  58% || ETA:  00:00:00
Loading cache:  59% || ETA:  00:00:00
Loading cache:  60% || ETA:  00:00:00
Loading cache:  61% || ETA:  00:00:00
Loading cache:  62% || ETA:  00:00:00
Loading cache:  63% || ETA:  00:00:00
Loading cache:  64% || ETA:  00:00:00
Loading cache:  65% || ETA:  00:00:00
Loading cache:  66% || ETA:  00:00:00
Loading cache:  67% || ETA:  00:00:00
Loading cache:  68% || ETA:  00:00:00
Loading cache:  69% || ETA:  00:00:00
Loading cache:  70% || ETA:  00:00:00
Loading cache:  71% || ETA:  00:00:00
Loading cache:  72% || ETA:  00:00:00
Loading cache:  73% || ETA:  00:00:00
Loading cache:  74% || ETA:  00:00:00
Loading cache:  75% || ETA:  00:00:00
Loading cache:  76% || ETA:  00:00:00
Loading cache:  77% || ETA:  00:00:00
Loading cache:  78% || ETA:  00:00:00
Loading cache:  79% || ETA:  00:00:00
Loading cache:  80% || ETA:  00:00:00
Loading cache:  81% || ETA:  00:00:00
Loading cache:  82% || ETA:  00:00:00
Loading cache:  83% || ETA:  00:00:00
Loading cache:  84% || ETA:  00:00:00
Loading cache:  85% || ETA:  00:00:00
Loading cache:  86% || ETA:  00:00:00
Loading cache:  87% || ETA:  00:00:00
Loading cache:  88% || ETA:  00:00:00
Loading cache:  89% || ETA:  00:00:00
Loading cache:  90% || ETA:  00:00:00
Loading cache:  91% || ETA:  00:00:00
Loading cache:  92% || ETA:  00:00:00
Loading cache:  93% || ETA:  00:00:00
Loading cache:  94% || ETA:  00:00:00
Loading cache:  95% || ETA:  00:00:00
Loading cache:  96% || ETA:  00:00:00
Loading cache:  97% || ETA:  00:00:00
Loading cache:  98% || ETA:  00:00:00
Loading cache:  99% || ETA:  00:00:00
Loading cache: 100% || ETA:  00:00:00

Loaded 2370 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "arm-poky-linux-gnueabi"
MACHINE           = "udooneo"
DISTRO            = "poky"
DISTRO_VERSION    = "2.0.1"
TUNE_FEATURES     = "arm armv7a vfp thumb neon callconvention-hard cortexa9"
TARGET_FPU        = "vfp-neon"
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:b1f23d1254682866236bfaeb843c0d8aa332efc2"
meta-networking   
meta-oe           
meta-python       = "HEAD:dc5634968b270dde250690609f0015f881db81f2"
meta-udoo         = "HEAD:93ce58963bb8155c0369c3797d695c08f854d30c"
meta-fsl-arm      = "HEAD:8d22b44716fa624ff87383c36d222d9e28d0b267"
meta-fsl-demos    = "HEAD:2231e946e7a94d096394f2b2477e8184c9bbde7b"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
[1;29mNOTE[0m: [29mTasks Summary: Attempted 1964 tasks of which 1964 didn't need to be rerun and all succeeded.[0m
+ bitbake udoo-image-full-cmdline
Loading cache:   0% || ETA:  --:--:--
Loading cache:   1% || ETA:  00:00:01
Loading cache:   2% || ETA:  00:00:00
Loading cache:   3% || ETA:  00:00:00
Loading cache:   4% || ETA:  00:00:00
Loading cache:   5% || ETA:  00:00:00
Loading cache:   6% || ETA:  00:00:00
Loading cache:   7% || ETA:  00:00:00
Loading cache:   8% || ETA:  00:00:00
Loading cache:   9% || ETA:  00:00:00
Loading cache:  10% || ETA:  00:00:00
Loading cache:  11% || ETA:  00:00:00
Loading cache:  12% || ETA:  00:00:00
Loading cache:  13% || ETA:  00:00:00
Loading cache:  14% || ETA:  00:00:00
Loading cache:  15% || ETA:  00:00:00
Loading cache:  16% || ETA:  00:00:00
Loading cache:  17% || ETA:  00:00:00
Loading cache:  18% || ETA:  00:00:00
Loading cache:  19% || ETA:  00:00:00
Loading cache:  20% || ETA:  00:00:00
Loading cache:  21% || ETA:  00:00:00
Loading cache:  22% || ETA:  00:00:00
Loading cache:  23% || ETA:  00:00:00
Loading cache:  24% || ETA:  00:00:00
Loading cache:  25% || ETA:  00:00:00
Loading cache:  26% || ETA:  00:00:00
Loading cache:  27% || ETA:  00:00:00
Loading cache:  28% || ETA:  00:00:00
Loading cache:  29% || ETA:  00:00:00
Loading cache:  30% || ETA:  00:00:00
Loading cache:  31% || ETA:  00:00:00
Loading cache:  32% || ETA:  00:00:00
Loading cache:  33% || ETA:  00:00:00
Loading cache:  34% || ETA:  00:00:00
Loading cache:  35% || ETA:  00:00:00
Loading cache:  36% || ETA:  00:00:00
Loading cache:  37% || ETA:  00:00:00
Loading cache:  38% || ETA:  00:00:00
Loading cache:  39% || ETA:  00:00:00
Loading cache:  40% || ETA:  00:00:00
Loading cache:  41% || ETA:  00:00:00
Loading cache:  42% || ETA:  00:00:00
Loading cache:  43% || ETA:  00:00:00
Loading cache:  44% || ETA:  00:00:00
Loading cache:  45% || ETA:  00:00:00
Loading cache:  46% || ETA:  00:00:00
Loading cache:  47% || ETA:  00:00:00
Loading cache:  48% || ETA:  00:00:00
Loading cache:  49% || ETA:  00:00:00
Loading cache:  50% || ETA:  00:00:00
Loading cache:  51% || ETA:  00:00:00
Loading cache:  52% || ETA:  00:00:00
Loading cache:  53% || ETA:  00:00:00
Loading cache:  54% || ETA:  00:00:00
Loading cache:  55% || ETA:  00:00:00
Loading cache:  56% || ETA:  00:00:00
Loading cache:  57% || ETA:  00:00:00
Loading cache:  58% || ETA:  00:00:00
Loading cache:  59% || ETA:  00:00:00
Loading cache:  60% || ETA:  00:00:00
Loading cache:  61% || ETA:  00:00:00
Loading cache:  62% || ETA:  00:00:00
Loading cache:  63% || ETA:  00:00:00
Loading cache:  64% || ETA:  00:00:00
Loading cache:  65% || ETA:  00:00:00
Loading cache:  66% || ETA:  00:00:00
Loading cache:  67% || ETA:  00:00:00
Loading cache:  68% || ETA:  00:00:00
Loading cache:  69% || ETA:  00:00:00
Loading cache:  70% || ETA:  00:00:00
Loading cache:  71% || ETA:  00:00:00
Loading cache:  72% || ETA:  00:00:00
Loading cache:  73% || ETA:  00:00:00
Loading cache:  74% || ETA:  00:00:00
Loading cache:  75% || ETA:  00:00:00
Loading cache:  76% || ETA:  00:00:00
Loading cache:  77% || ETA:  00:00:00
Loading cache:  78% || ETA:  00:00:00
Loading cache:  79% || ETA:  00:00:00
Loading cache:  80% || ETA:  00:00:00
Loading cache:  81% || ETA:  00:00:00
Loading cache:  82% || ETA:  00:00:00
Loading cache:  83% || ETA:  00:00:00
Loading cache:  84% || ETA:  00:00:00
Loading cache:  85% || ETA:  00:00:00
Loading cache:  86% || ETA:  00:00:00
Loading cache:  87% || ETA:  00:00:00
Loading cache:  88% || ETA:  00:00:00
Loading cache:  89% || ETA:  00:00:00
Loading cache:  90% || ETA:  00:00:00
Loading cache:  91% || ETA:  00:00:00
Loading cache:  92% || ETA:  00:00:00
Loading cache:  93% || ETA:  00:00:00
Loading cache:  94% || ETA:  00:00:00
Loading cache:  95% || ETA:  00:00:00
Loading cache:  96% || ETA:  00:00:00
Loading cache:  97% || ETA:  00:00:00
Loading cache:  98% || ETA:  00:00:00
Loading cache:  99% || ETA:  00:00:00
Loading cache: 100% || ETA:  00:00:00

Loaded 2370 entries from dependency cache.
[1;29mNOTE[0m: [29mResolving any missing task queue dependencies[0m
[1;29mNOTE[0m: [29mmultiple providers are available for jpeg-native (jpeg-native, libjpeg-turbo-native)[0m
[1;29mNOTE[0m: [29mconsider defining a PREFERRED_PROVIDER entry to match jpeg-native[0m

Build Configuration:
BB_VERSION        = "1.28.0"
BUILD_SYS         = "x86_64-linux"
NATIVELSBSTRING   = "Ubuntu-14.04"
TARGET_SYS        = "arm-poky-linux-gnueabi"
MACHINE           = "udooneo"
DISTRO            = "poky"
DISTRO_VERSION    = "2.0.1"
TUNE_FEATURES     = "arm armv7a vfp thumb neon callconvention-hard cortexa9"
TARGET_FPU        = "vfp-neon"
meta              
meta-yocto        
meta-yocto-bsp    = "HEAD:b1f23d1254682866236bfaeb843c0d8aa332efc2"
meta-networking   
meta-oe           
meta-python       = "HEAD:dc5634968b270dde250690609f0015f881db81f2"
meta-udoo         = "HEAD:93ce58963bb8155c0369c3797d695c08f854d30c"
meta-fsl-arm      = "HEAD:8d22b44716fa624ff87383c36d222d9e28d0b267"
meta-fsl-demos    = "HEAD:2231e946e7a94d096394f2b2477e8184c9bbde7b"

[1;29mNOTE[0m: [29mPreparing RunQueue[0m
[1;29mNOTE[0m: [29mExecuting SetScene Tasks[0m
[1;29mNOTE[0m: [29mExecuting RunQueue Tasks[0m
[1;29mNOTE[0m: [29mTasks Summary: Attempted 4325 tasks of which 4325 didn't need to be rerun and all succeeded.[0m
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

Please see [this blog post](http://gmacario.github.io/howto/udoo/neo/embedded/software/development/2015/11/08/connecting-to-udoo-neo-serial-console.html) to learn how to connect to the UDOO Neo serial console.

After powering up your UDOO Neo the serial console should display the boot messages up to a login prompt:

```
U-Boot SPL 2015.04 (Feb 24 2016 - 14:37:26)
Setting 1024MB RAM calibration data
port 1


U-Boot 2015.04 (Feb 24 2016 - 14:37:26)

CPU:   Freescale i.MX6SX rev1.2 at 792 MHz
CPU:   Temperature 50 C
Reset cause: POR
Board: UDOO Neo Full
I2C:   ready
DRAM:  1 GiB
PMIC:  PFUZE3000 DEV_ID=0x30 REV_ID=0x11
MMC:   FSL_SDHC: 0, FSL_SDHC: 1
*** Warning - bad CRC, using default environment

In:    serial
Out:   serial
Err:   serial
Net:   CPU Net Initialization Failed
No ethernet found.
Normal Boot
Hit any key to stop autoboot:
...

Starting syslogd/klogd: done

OpenEmbedded nodistro.0 udooneo /dev/ttymxc0

udooneo login:
```

Login to the target with user `root` (default password: none)

```
root@udooneo:~# cat /proc/version
Linux version 3.14.56_1.0.x-udoo+gfeef1c3 (root@1cfc10892ef9) (gcc version 5.2.0 (GCC) ) #1 SMP PREEMPT Wed Feb 24 14:30:31 UTC 2016
root@udooneo:~#  fdisk -l

Disk /dev/mmcblk0: 15.7 GB, 15720251392 bytes
4 heads, 32 sectors/track, 239872 cylinders
Units = cylinders of 128 * 512 = 65536 bytes

        Device Boot      Start         End      Blocks  Id System
/dev/mmcblk0p1              65         192        8192   c Win95 FAT32 (LBA)
/dev/mmcblk0p2             193         320        8192  83 Linux
root@udooneo:~# df -h
Filesystem                Size      Used Available Use% Mounted on
/dev/root                 6.7M      4.8M      1.4M  77% /
devtmpfs                337.1M         0    337.1M   0% /dev
tmpfs                   497.2M     84.0K    497.1M   0% /run
tmpfs                   497.2M     56.0K    497.2M   0% /var/volatile
root@udooneo:~#
```

<!-- EOF -->
