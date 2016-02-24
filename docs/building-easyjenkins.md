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
Step 11 : RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt
 ---> Running in f8121831d46a
Downloading ace-editor:1.0.1
Downloading ant:1.2
Downloading antisamy-markup-formatter:1.3
Downloading authentication-tokens:1.2
Downloading branch-api:1.3
Downloading build-flow-plugin:0.18
Downloading build-flow-test-aggregator:1.2
Downloading build-pipeline-plugin:1.5.1
Downloading cloudbees-folder:5.2.1
Downloading conditional-buildstep:1.3.3
Downloading credentials:1.25
Downloading cvs:2.12
Downloading dashboard-view:2.9.7
Downloading delivery-pipeline-plugin:0.9.8
Downloading docker-build-publish:1.1
Downloading docker-commons:1.3.1
Downloading docker-custom-build-environment:1.6.5
Downloading docker-plugin:0.16.0
Downloading docker-workflow:1.3
Downloading durable-task:1.7
Downloading envinject:1.92.1
Downloading external-monitor-job:1.4
Downloading ghprb:1.30.4
Downloading git:2.4.2
Downloading git-client:1.19.5
Downloading git-server:1.6
Downloading github:1.17.1
Downloading github-api:1.72
Downloading github-oauth:0.22.2
Downloading github-pullrequest:0.0.1-rc3
Downloading greenballs:1.15
Downloading groovy:1.29
Downloading htmlpublisher:1.11
Downloading icon-shim:2.0.3
Downloading javadoc:1.3
Downloading jenkins-multijob-plugin:1.20
Downloading job-dsl:1.43
Downloading jobConfigHistory:2.12
Downloading join:1.19
Downloading jquery:1.11.2-0
Downloading jquery-detached:1.2
Downloading junit:1.10
Downloading ldap:1.11
Downloading mailer:1.16
Downloading mapdb-api:1.0.6.0
Downloading matrix-auth:1.3
Downloading matrix-project:1.6
Downloading maven-plugin:2.12.1
Downloading mercurial:1.54
Downloading mock-slave:1.8
Downloading multiple-scms:0.5
Downloading pam-auth:1.2
Downloading parameterized-trigger:2.30
Downloading plain-credentials:1.1
Downloading promoted-builds:2.25
Downloading run-condition:1.0
Downloading scm-api:1.0
Downloading script-security:1.17
Downloading scriptler:2.9
Downloading sectioned-view:1.20
Downloading ssh-agent:1.9
Downloading ssh-credentials:1.11
Downloading ssh-slaves:1.10
Downloading subversion:2.5.7
Downloading token-macro:1.12.1
Downloading translation:1.12
Downloading view-job-filters:1.27
Downloading windows-slaves:1.1
Downloading workflow-aggregator:1.13
Downloading workflow-api:1.13
Downloading workflow-basic-steps:1.13
Downloading workflow-cps:1.13
Downloading workflow-cps-global-lib:1.13
Downloading workflow-durable-task-step:1.13
Downloading workflow-job:1.13
Downloading workflow-multibranch:1.13
Downloading workflow-scm-step:1.13
Downloading workflow-step-api:1.13
Downloading workflow-support:1.13
 ---> 92c933702341
Removing intermediate container f8121831d46a
Step 12 : COPY start.sh /tmp/start.sh
 ---> 7ec474f8be5f
Removing intermediate container 3278e7a1dcb4
Step 13 : RUN chmod +x /tmp/start.sh && dos2unix /tmp/start.sh
 ---> Running in 666f31c47faf
[91mdos2unix: converting file /tmp/start.sh to Unix format ...
[0m ---> 094a055a3c4b
Removing intermediate container 666f31c47faf
Step 14 : COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy
 ---> 92151d6f447b
Removing intermediate container 95be18e0b67f
Step 15 : RUN touch /var/run/docker.sock
 ---> Running in 4d17b4ab2adb
 ---> fa36902b47d1
Removing intermediate container 4d17b4ab2adb
Step 16 : ENTRYPOINT /bin/bash -c /tmp/start.sh
 ---> Running in b415a4449403
 ---> a306135e21f1
Removing intermediate container b415a4449403
Successfully built a306135e21f1
+ ./runme.sh
WARNING: Cannot find docker-machine - assuming environment variables are already defined
Creating network "workspace_default" with the default driver
Creating workspace_myjenkins_1
Notifying upstream projects of job completion
Finished: SUCCESS
```

<!-- EOF -->
