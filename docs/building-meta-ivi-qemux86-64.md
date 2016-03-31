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

<!-- (2016-03-31 09:45 CEST) http://alm-gm-ubu15.solarma.it:9080/job/GENIVI/job/yocto-baseline-next-NEW2/2/console -->

```
TODO
```

**NOTE**: A full build starting from an empty workspace takes about TODO hours to complete (Docker Engine running on a dual-core AMD Opteron(TM) Processor 6276 CPU X5450 @2300 MHz, 4 GB RAM).

<!-- EOF -->
