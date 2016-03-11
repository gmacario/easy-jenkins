# Building GENIVI Common API C in Jenkins

## Prerequisites

* A recent version of [Jenkins](https://jenkins-ci.org/) CI/CD installed together with the necessary plugins
  - Tested with https://github.com/gmacario/easy-jenkins
* An Internet browser able to access the Jenkins dashboard at `${JENKINS_URL}`
  - Example: http://192.168.99.100:9080/

## Step-by-step instructions

### Create folder `GENIVI`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `GENIVI`
* Type: **Folder**

then click **OK**. Inside the project configuration page, review configuration, then click **OK**.

### Create project `common-api-c`

Browse `${DOCKER_URL}/job/GENIVI`, then click **New Item**

* Name: `common-api-c`
* Type: **Freestyle project**

then click **OK**. Inside the project configuration page, add the following information:

* Discard Old Builds: Yes
  - Strategy: Log Rotation
    - Days to keep build: (none)
    - Max # of builds to keep: 5
* Source Code Management: Git
  - Repositories
    - Repository URL: `git://git.projects.genivi.org/common-api/c-poc.git`
    - Credentials: - none -
  - Branches to build
    - Branch Specifier (blank for 'any'): `*/master`
  - Repository browser: (Auto)
* Build Environment
  - Build inside a Docker container: Yes
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-capi-native`
      - Advanced...
        - force Pull: Yes
* Build
  - Execute shell
    - Command
```
#!/bin/bash -xe

# DEBUG
id
ls -la

# Actual build steps
autoreconf -i
./configure
make
sudo make install

# EOF
```

then click **Save**.

### Build project `common-api-c`

Browse `${DOCKER_URL}/job/GENIVI/job/common-api-c`, then click **Build Now**

Result: SUCCESS

Excerpt from build console:

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace
Cloning the remote Git repository
Cloning repository git://git.projects.genivi.org/common-api/c-poc.git
 > git init /var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/common-api/c-poc.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/common-api/c-poc.git +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url git://git.projects.genivi.org/common-api/c-poc.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url git://git.projects.genivi.org/common-api/c-poc.git # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/common-api/c-poc.git
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/common-api/c-poc.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision d6ec42ce45c33f40560d1f24b9143e9b1e6816e9 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f d6ec42ce45c33f40560d1f24b9143e9b1e6816e9
First time build. Skipping changelog.
Pull Docker image gmacario/build-capi-native from repository ...
$ docker pull gmacario/build-capi-native
...
TODO
```

<!-- EOF -->
