# Building UDOObuntu image for UDOO Neo using easy-jenkins

**WORK-IN-PROGRESS**

<!-- (2016-06-16 10:00 CEST) 
Tested on http://ies-genbld01-vm.ies.mentorg.com:9080/job/UDOO/job/mkudoobuntu-udooneo-minimal/configure
-->

This document explains how to build from sources the [UDOObuntu](TODO) distribution using [Jenkins](https://jenkins-ci.org/).

The actual build is executed inside a [Docker custom build environment](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Custom+Build+Environment+Plugin) which is automatically spun by the Jenkins server.

The following instructions have been tested on:

* Docker client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.11.1)
* Docker engine: ies-genbld01-vm (Ubuntu 14.04.4 LTS 64-bit, Docker 1.11.1, Docker Compose 1.7.1)

## Preparation

* Install and configure [easy-jenkins](https://github.com/gmacario/easy-jenkins) - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Create folder `UDOO`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `UDOO`
* Type: **Folder**

then click **OK**. Inside the project configuration page, review configuration, then click **OK**.

### Create project `mkudoobuntu-udooneo-minimal`

<!-- (2016-06-16 11:54 CEST) -->

Browse `${DOCKER_URL}/job/UDOO`, then click **New Item**

* Name: `mkudoobuntu-udooneo-minimal`
* Type: **Freestyle project**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 2
* Source Code Management: Git
  - Repositories
    - Repository URL: `https://github.com/UDOOboard/mkudoobuntu.git`
    - Credentials: - none -
  - Branches to build
    - Branch Specifier (blank for 'any'): `*/master`
  - Repository browser: (Auto)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-yocto`
    - Advanced...
      - force Pull: Yes
      - Verbose: Yes
* Build
  - Execute shell
    - Command

```
#!/bin/bash -xe

id
cat /etc/passwd
cat /etc/group
ls -la

# ./mkudoobuntu.sh
# ./mkudoobuntu.sh help
# ./mkudoobuntu.sh udoo-neo help
sudo ./mkudoobuntu.sh udoo-neo help

sudo bash -xe ./mkudoobuntu.sh udoo-neo minimal
# sudo ./mkudoobuntu.sh udoo-neo desktop

# EOF
```

* Post-build Actions
  - Archive the artifacts
    - Files to archive: `TODO`

then click **Save**.

### Build project `mkudoobuntu-udooneo-minimal`

Browse `${JENKINS_URL}/job/UDOO/job/mkudoobuntu-udooneo-minimal/`, then click **Build Now**.

You may watch the build logs at `${JENKINS_URL}/job/UDOO/job/mkudoobuntu-udooneo-minimal/lastBuild/console`

<!-- (2016-06-16 11:56 CEST) 
http://ies-genbld01-vm.ies.mentorg.com:9080/job/UDOO/job/mkudoobuntu-udooneo-minimal/12/console
-->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/UDOO/jobs/mkudoobuntu-udooneo-minimal/workspace
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/UDOOboard/mkudoobuntu.git # timeout=10
Fetching upstream changes from https://github.com/UDOOboard/mkudoobuntu.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/UDOOboard/mkudoobuntu.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision 3de1f38e039cc040050dd1fab431087cecc9fa65 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 3de1f38e039cc040050dd1fab431087cecc9fa65
 > git rev-list 3de1f38e039cc040050dd1fab431087cecc9fa65 # timeout=10
Docker container fb6a1bd910101d678ad03bb30fdcc3645d9968068828272857d5cd8f2b1f5737 started to host the build
$ docker exec --tty fb6a1bd910101d678ad03bb30fdcc3645d9968068828272857d5cd8f2b1f5737 env
[workspace] $ docker exec --tty --user 1000:1000 fb6a1bd910101d678ad03bb30fdcc3645d9968068828272857d5cd8f2b1f5737 env 'BASH_FUNC_copy_reference_file%%=() {  f=${1%/};
 echo "$f" >> $COPY_REFERENCE_FILE_LOG;
 rel=${f:23};
 dir=$(dirname ${f});
 echo " $f -> $rel" >> $COPY_REFERENCE_FILE_LOG;
 if [[ ! -e /var/jenkins_home/${rel} ]]; then
 echo "copy $rel to JENKINS_HOME" >> $COPY_REFERENCE_FILE_LOG;
 mkdir -p /var/jenkins_home/${dir:23};
 cp -r /usr/share/jenkins/ref/${rel} /var/jenkins_home/${rel};
 [[ ${rel} == plugins/*.jpi ]] && touch /var/jenkins_home/${rel}.pinned;
 fi
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#12 BUILD_ID=12 BUILD_NUMBER=12 BUILD_TAG=jenkins-UDOO-mkudoobuntu-udooneo-minimal-12 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=0 GIT_BRANCH=origin/master GIT_COMMIT=3de1f38e039cc040050dd1fab431087cecc9fa65 GIT_PREVIOUS_COMMIT=3de1f38e039cc040050dd1fab431087cecc9fa65 GIT_URL=https://github.com/UDOOboard/mkudoobuntu.git HOME=/var/jenkins_home HOSTNAME=73b3632611cb HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=11929db4441d9f7c JAVA_DEBIAN_VERSION=8u45-b14-2~bpo8+2 JAVA_VERSION=8u45 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=11929db4441d9f7c JENKINS_SHA=da06f963edb627f0ced2fce612f9985d1928f79b JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=2.0 JOB_NAME=UDOO/mkudoobuntu-udooneo-minimal LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/jobs/UDOO/jobs/mkudoobuntu-udooneo-minimal/workspace /bin/bash -xe /tmp/hudson5126542139947711536.sh
+ id
uid=1000(jenkins) gid=1000(jenkins) groups=1000(jenkins)
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
jenkins:x:1000:1000::/home/jenkins:
build:x:30000:30000::/home/build:
+ cat /etc/group
root:x:0:
daemon:x:1:
bin:x:2:
sys:x:3:
adm:x:4:syslog
tty:x:5:
disk:x:6:
lp:x:7:
mail:x:8:
news:x:9:
uucp:x:10:
man:x:12:
proxy:x:13:
kmem:x:15:
dialout:x:20:
fax:x:21:
voice:x:22:
cdrom:x:24:
floppy:x:25:
tape:x:26:
sudo:x:27:
audio:x:29:
dip:x:30:
www-data:x:33:
backup:x:34:
operator:x:37:
list:x:38:
irc:x:39:
src:x:40:
gnats:x:41:
shadow:x:42:
utmp:x:43:
video:x:44:
sasl:x:45:
plugdev:x:46:
staff:x:50:
games:x:60:
users:x:100:
nogroup:x:65534:
libuuid:x:101:
netdev:x:102:
crontab:x:103:
syslog:x:104:
ssh:x:105:
utempter:x:106:
jenkins:x:1000:
build:x:30000:
+ ls -la
total 68
drwxr-xr-x 7 jenkins jenkins  4096 Jun  5 08:15 .
drwxr-xr-x 4 jenkins jenkins  4096 Jun  5 09:02 ..
drwxr-xr-x 8 jenkins jenkins  4096 Jun  5 09:02 .git
-rw-r--r-- 1 jenkins jenkins    61 Jun  5 08:15 .gitignore
-rw-r--r-- 1 jenkins jenkins 18047 Jun  5 08:15 LICENSE
-rw-r--r-- 1 jenkins jenkins  1203 Jun  5 08:15 README.md
drwxr-xr-x 5 jenkins jenkins  4096 Jun  5 08:15 boards
drwxr-xr-x 2 jenkins jenkins  4096 Jun  5 08:15 include
-rwxr-xr-x 1 jenkins jenkins  8672 Jun  5 08:15 mkudoobuntu.sh
drwxr-xr-x 4 jenkins jenkins  4096 Jun  5 08:15 patches
drwxr-xr-x 2 jenkins jenkins  4096 Jun  5 08:15 tools
+ sudo ./mkudoobuntu.sh udoo-neo help
[sudo] password for jenkins: 
```

**FIXME**: Must give sudo rights to user `jenkins` inside container

**NOTE**: A full build starting from an empty workspace takes about TODO hours to complete (Docker Engine running on a quad-core Intel(R) Xeon(TM) CPU X6550 @2.00GHz, 4 GB RAM + 16 GB swap).

![Artifacts of project mkudoobuntu-udooneo-minimal](images/capture-2016xxxx-yyyy.png)

Browse `${JENKINS_URL}/job/UDOO/job/bmkudoobuntu-udooneo-minimal/ws/TODO/` to inspect the build results.

![Workspace of project mkudoobuntu-udooneo-minimal](images/capture-2016xxxx-yyyy.png)

<!-- EOF -->
