# Building a sample Android Application for the UDOO NEO

**WORK-IN-PROGRESS**

This document explains how to build from sources a [sample Android App](https://github.com/gmacario/colorcode) Android Application for the [UDOO Neo](http://www.udoo.org/udoo-neo/) using [easy-jenkins](https://github.com/gmacario/easy-jenkins).

The following instructions were tested on

* Docker client: mac-tizy (HW: MacBook Pro; SW: OS X 10.11.3, Docker Toolbox 10.0)
* Docker engine: mv-linux-powerhorse (HW: HP xw8600 Workstation, SW: Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.0)

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Configure project `build_androidapp_udooneo`

Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_androidapp_udooneo`
  - Type: **Freestyle project**

  then click **OK**.

Inside the project configuration page, fill-in the following information:
  - Discard Old Builds: Yes
    - Strategy: Log Rotation
      - Days to keep builds: (none)
      - Max # of builds to keep: 2
  - Source Code Management: Git
    - Repositories
      - Repository URL: `https://github.com/gmacario/colorcode`
      - Credentials: - none -
      - Branches to build
        - Branch Specifier (blank for 'any'): `*/master`
      - Repository browser: (Auto)
  - Build Environment
    - Build inside a Docker container: Yes
      - Docker image to use: Pull docker image from repository
        - Image id/tag: `gmacario/wtfapp-devenv`
  - Build
    - Execute shell
      - Command

```
#!/bin/bash -xe

export JAVA_HOME=
chmod a+x gradlew
./gradlew

# EOF
```
- Post-build Actions
    - Archive the artifacts
      - Files to archive: `TODO`

then click **Save**

### Build project `build_androidapp_udooneo`

Browse `${JENKINS_URL}/job/build_androidapp_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_androidapp_udooneo/lastBuild/console`

<!-- (2016-04-26 18:00 CEST) http://alm-gm-ubu15.solarma.it:9080/job/build_androidapp_udooneo/3/consoleText -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_androidapp_udooneo/workspace
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gmacario/colorcode # timeout=10
Fetching upstream changes from https://github.com/gmacario/colorcode
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/colorcode +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision b834580fd9e0d08c7be8fb34bb34eda7b3132cf0 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f b834580fd9e0d08c7be8fb34bb34eda7b3132cf0
 > git rev-list b834580fd9e0d08c7be8fb34bb34eda7b3132cf0 # timeout=10
Docker container 363b1ec6456a1d252f2e187d7acfb40b59f0af7cb4544b7002e4993d2f6e952c started to host the build
$ docker exec --tty 363b1ec6456a1d252f2e187d7acfb40b59f0af7cb4544b7002e4993d2f6e952c env
[workspace] $ docker exec --tty --user 1000:1000 363b1ec6456a1d252f2e187d7acfb40b59f0af7cb4544b7002e4993d2f6e952c env ANDROID_HOME=/opt/android-sdk-linux 'BASH_FUNC_copy_reference_file%%=() {  f=${1%/};
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#3 BUILD_ID=3 BUILD_NUMBER=3 BUILD_TAG=jenkins-build_androidapp_udooneo-3 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/master GIT_COMMIT=b834580fd9e0d08c7be8fb34bb34eda7b3132cf0 GIT_PREVIOUS_COMMIT=b834580fd9e0d08c7be8fb34bb34eda7b3132cf0 GIT_URL=https://github.com/gmacario/colorcode HOME=/var/jenkins_home HOSTNAME=0ac267008914 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=88213e4aa86bf5e1 JAVA_DEBIAN_VERSION=8u45-b14-2~bpo8+2 JAVA_VERSION=8u45 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=88213e4aa86bf5e1 JENKINS_SHA=da06f963edb627f0ced2fce612f9985d1928f79b JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=2.0 JOB_NAME=build_androidapp_udooneo LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/jobs/build_androidapp_udooneo/workspace /bin/bash -xe /tmp/hudson8616581436146819036.sh
+ export JAVA_HOME=
+ JAVA_HOME=
+ chmod a+x gradlew
+ ./gradlew
[0m[1m> Loading[22m[1m > settings[22m[11D[0K[7D[1mConfiguring[22m[1m > 0/2 projects[22m[1m > root project[22m[1m > Resolving dependencies ':classpath'[22m[38D[0K[15D[0K[12D[1m1/2 projects[22m[1m > :app[22m[1m > Resolving dependencies ':app:classpath'[22m[42D[0K[7D[0K[12D[1m2/2 projects[22m[28D[0K
[1m> Configuring > 2/2 projects[22m[28D[0K[31mFAILURE: [39m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[9C[31mBuild failed with an exception.[39m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[40C
[1m> Configuring > 2/2 projects[22m[28D[0K
[1m> Configuring > 2/2 projects[22m[28D[0K* What went wrong:
[1m> Configuring > 2/2 projects[22m[28D[0KA problem occurred configuring project ':app'.
[1m> Configuring > 2/2 projects[22m[28D[0K[33m> [39m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[2Cfailed to find Build Tools revision 23.0.2
[1m> Configuring > 2/2 projects[22m[28D[0K
[1m> Configuring > 2/2 projects[22m[28D[0K* Try:
[1m> Configuring > 2/2 projects[22m[28D[0KRun with 
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[9C[1m--stacktrace[22m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[21C option to get the stack trace. Run with 
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[62C[1m--info[22m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[68C or 
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[72C[1m--debug[22m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[79C option to get more log output.
[1m> Configuring > 2/2 projects[22m[28D[0K
[1m> Configuring > 2/2 projects[22m[28D[0K[31mBUILD FAILED[39m
[1m> Configuring > 2/2 projects[22m[28D[0K[1A[12C
[1m> Configuring > 2/2 projects[22m[28D[0K
[1m> Configuring > 2/2 projects[22m[28D[0KTotal time: 10.267 secs
[1m> Configuring > 2/2 projects[22m[15D[0K[13D[0K[0mBuild step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

<!-- EOF -->
