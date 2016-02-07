# Building easy-jenkins inside easy-jenkins

This document contains instructions for building easy-jenkins inside easy-jenkins.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to section **Preparation** of [howto-build-gdp.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/howto-build-gdp.md) for details.

## Step-by-step instructions

### Configure project `build_easyjenkins_freestyle`

* Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_easyjenkins_freestyle`
  - Type: **Freestyle project**

  then click **OK**.
  
**TODO**: Configure a Pipeline instead than a Freestyle project
  
* Inside the project configuration page, fill-in the following information:
  - Restrict where this project can be run: `master` (TODO: Define and use label `docker`)
  - Source Code Management: Git
    - Repositories
      - Repository URL: `https://github.com/gmacario/easy-jenkins` (TODO?)
      - Credentials: - none -
      - Branches to build
        - Branch Specifier (blank for `any`): `*/master` (TODO?)
      - Repository browser: (Auto)
  - Build
    - Add build step > Execute shell
      - Command

```
#!/bin/bash -xe

docker-compose build --pull

# Uncomment after merging https://github.com/gmacario/easy-jenkins/pull/11
# ./runme.sh

# EOF
```
  
  then click **Save**

### Build project `build_easyjenkins_freestyle`

<!-- (2016-02-07 13:55 CET) -->

* Browse `${JENKINS_URL}/job/build_easyjenkins_freestyle`, then click **Build Now**

Result: FAILURE

```
Started by user anonymous
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_easyjenkins_freestyle/workspace
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gmacario/easy-jenkins # timeout=10
Fetching upstream changes from https://github.com/gmacario/easy-jenkins
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/easy-jenkins +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision d6821467638b3bba64bb45854b7320ec26dedb12 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f d6821467638b3bba64bb45854b7320ec26dedb12
 > git rev-list d6821467638b3bba64bb45854b7320ec26dedb12 # timeout=10
[workspace] $ /bin/bash -xe /tmp/hudson349287039455015219.sh
+ docker-compose build --pull
Building myjenkins
Step 1 : FROM jenkins:latest
latest: Pulling from library/jenkins
Digest: sha256:b5a25debea686791429aceb2a4cd655ced091b76921de3fda6d0c2bfc38bb2b6
Status: Image is up to date for jenkins:latest
 ---> 997d1b2b89a5
Step 2 : MAINTAINER Gianpaolo Macario <gmacario@gmail.com>
 ---> Using cache
 ---> 032131187f15
Step 3 : USER root
 ---> Using cache
 ---> 685253925988
Step 4 : RUN apt-get update -qq && apt-get install -qqy     apt-transport-https     ca-certificates     curl     wget     lxc     iptables     dos2unix     groovy2
 ---> Using cache
 ---> c09a804a4ff7
Step 5 : RUN wget -qO- https://get.docker.com/ | sh
 ---> Using cache
 ---> c262e82f6406
Step 6 : RUN usermod -aG docker jenkins
 ---> Using cache
 ---> dd4dba63b886
Step 7 : RUN curl -L https://github.com/docker/compose/releases/download/1.6.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
 ---> Using cache
 ---> a57eb3635acb
Step 8 : RUN chmod +x /usr/local/bin/docker-compose
 ---> Using cache
 ---> 87f81d92759f
Step 9 : COPY plugins.txt /usr/share/jenkins/plugins.txt
 ---> Using cache
 ---> 433b2bfa393e
Step 10 : RUN dos2unix /usr/share/jenkins/plugins.txt
 ---> Using cache
 ---> 69431ea067d5
Step 11 : RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt
 ---> Using cache
 ---> 5356da3db52e
Step 12 : COPY start.sh /tmp/start.sh
 ---> Using cache
 ---> ee12e83f352d
Step 13 : RUN chmod +x /tmp/start.sh && dos2unix /tmp/start.sh
 ---> Using cache
 ---> 1f419eda5828
Step 14 : COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy
 ---> Using cache
 ---> e36ea21aa09f
Step 15 : RUN touch /var/run/docker.sock
 ---> Using cache
 ---> 344b37247b7a
Step 16 : ENTRYPOINT /bin/bash -c /tmp/start.sh
 ---> Using cache
 ---> 144d1b05ba44
Successfully built 144d1b05ba44
build-yocto-slave uses an image, skipping
Notifying upstream projects of job completion
Finished: SUCCESS
```

~~**TODO**: Install docker 1.10.0 or later. See related https://github.com/gmacario/easy-jenkins/issues/35~~

~~**TODO**: Install docker-compose 1.6.0 or later. See related https://github.com/gmacario/easy-jenkins/issues/36~~

**TODO**: Handle case when no docker-machine installed. See related https://github.com/gmacario/easy-jenkins/pull/11

<!-- EOF -->
