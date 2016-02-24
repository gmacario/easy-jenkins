# Building easy-jenkins inside easy-jenkins

This document contains instructions for building easy-jenkins inside easy-jenkins.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Configure project `build_easyjenkins`

* Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_easyjenkins`
  - Type: **Freestyle project**

  then click **OK**.
  
**TODO**: Configure a Pipeline instead than a Freestyle project
  
* Inside the project configuration page, fill-in the following information:
  - Discard Old Builds: Yes
    - Strategy: Log Rotation
      - Days to keep builds: (none)
      - Max # of builds to keep: 5
  - Restrict where this project can be run: `master` (TODO: Define and use label `docker`)
  - Source Code Management: Git
    - Repositories
      - Repository URL: `https://github.com/gmacario/easy-jenkins`
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
./runme.sh

# EOF
```
  
  then click **Save**

### Build project `build_easyjenkins`

<!-- (2016-02-24 12:50 CET) -->

* Browse `${JENKINS_URL}/job/build_easyjenkins`, then click **Build Now**

Result: SUCCESS

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_easyjenkins/workspace
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/easy-jenkins
 > git init /var/jenkins_home/jobs/build_easyjenkins/workspace # timeout=10
Fetching upstream changes from https://github.com/gmacario/easy-jenkins
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/easy-jenkins +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url https://github.com/gmacario/easy-jenkins # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/gmacario/easy-jenkins # timeout=10
Fetching upstream changes from https://github.com/gmacario/easy-jenkins
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/easy-jenkins +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision 207f490c51814f47d1f686f818d5bfcd5ed126d3 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 207f490c51814f47d1f686f818d5bfcd5ed126d3
First time build. Skipping changelog.
[workspace] $ /bin/bash -xe /tmp/hudson4273555466691351036.sh
+ docker-compose build --pull
Building myjenkins
Step 1 : FROM jenkins:latest
latest: Pulling from library/jenkins
...
TODO
```

<!-- EOF -->
