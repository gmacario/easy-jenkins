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

<!-- (2016-02-28 12:17 CET): Tested on dc7600-gm -->

Browse `${JENKINS_URL}/job/build_androidapp_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_androidapp_udooneo/lastBuild/console`

<!-- (2016-02-29 15:25 CET) http://mv-linux-powerhorse.solarma.it:9080/job/build_androidapp_udooneo/lastBuild/consoleText -->

```
TODO
```

<!-- EOF -->
