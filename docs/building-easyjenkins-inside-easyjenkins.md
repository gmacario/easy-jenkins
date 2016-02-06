# Building easy-jenkins inside easy-jenkins

This document contains instructions for building easy-jenkins inside easy-jenkins.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to section **Preparation** of [howto-build-gdp.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/howto-build-gdp.md) for details.

## Verify Docker configuration inside Jenkins `master` node

### Configure project `test_docker`

* Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `test_docker`
  - Type: **Freestyle project**

  then click **OK**.
  
* Inside the project configuration page, fill-in the following information:
  - Restrict where this project can be run: `master`
  - Build
    - Add build step > Execute shell
      - Command

```
#!/bin/bash -xe

id
pwd

docker --version
docker version
docker info

docker-compose --version

# EOF
```
  
  then click **Save**

### Build project `test_docker`

* Browse `${JENKINS_URL}/job/test_docker`, then click **Build Now**

Result: SUCCESS

Inspect `${JENKINS_URL}/job/test_docker/lastBuild/console`

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/test_docker
[test_docker] $ /bin/bash -xe /tmp/hudson4381156953900617112.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/workspace/test_docker
+ docker --version
Docker version 1.9.1, build a34a1d5
+ docker version
Client:
 Version:      1.9.1
 API version:  1.21
 Go version:   go1.4.2
 Git commit:   a34a1d5
 Built:        Fri Nov 20 12:59:02 UTC 2015
 OS/Arch:      linux/amd64

Server:
 Version:      1.9.1
 API version:  1.21
 Go version:   go1.4.2
 Git commit:   a34a1d5
 Built:        Fri Nov 20 12:59:02 UTC 2015
 OS/Arch:      linux/amd64
+ docker info
Containers: 0
Images: 0
Server Version: 1.9.1
Storage Driver: devicemapper
 Pool Name: docker-0:34-240-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 107.4 GB
 Backing Filesystem: ext4
 Data file: /dev/loop0
 Metadata file: /dev/loop1
 Data Space Used: 1.821 GB
 Data Space Total: 107.4 GB
 Data Space Available: 105.6 GB
 Metadata Space Used: 1.479 MB
 Metadata Space Total: 2.147 GB
 Metadata Space Available: 2.146 GB
 Udev Sync Supported: false
 Deferred Removal Enabled: false
 Deferred Deletion Enabled: false
 Deferred Deleted Device Count: 0
 Data loop file: /var/lib/docker/devicemapper/devicemapper/data
 Metadata loop file: /var/lib/docker/devicemapper/devicemapper/metadata
 Library Version: 1.02.90 (2014-09-01)
Execution Driver: native-0.2
Logging Driver: json-file
Kernel Version: 3.13.0-77-generic
Operating System: Debian GNU/Linux 8 (jessie) (containerized)
CPUs: 2
Total Memory: 3.177 GiB
Name: 12edd489028e
ID: OZWS:TLNR:V3FW:FYIH:2VBE:IN4Q:FYWZ:HSZM:LDPF:U7SY:D5Q4:HMY7
WARNING: No swap limit support
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
+ docker-compose --version
docker-compose version 1.5.2, build 7240ff3
Notifying upstream projects of job completion
Finished: SUCCESS
```

Notice that there are no containers inside this Docker engine - which means it is a different engine from the one where easy-jenkins services are running.

**TODO**: Should not use Docker-in-Docker for service myjenkins.
See [this blog post](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/) to understand why.

## Configure project `build_easyjenkins_freestyle`

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

./runme.sh

# EOF
```
  
  then click **Save**

TODO

## Build project `build_easyjenkins_freestyle`

* Browse `${JENKINS_URL}/job/build_easyjenkins_freestyle`, then click **Build Now**

Result: FAILURE

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/build_easyjenkins_freestyle
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/easy-jenkins
 > git init /var/jenkins_home/workspace/build_easyjenkins_freestyle # timeout=10
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
Checking out Revision fc1bd4ce76ffec5ceaeb3b16a91757e265eb0b75 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f fc1bd4ce76ffec5ceaeb3b16a91757e265eb0b75
 > git rev-list fc1bd4ce76ffec5ceaeb3b16a91757e265eb0b75 # timeout=10
[build_easyjenkins_freestyle] $ /bin/bash -xe /tmp/hudson5411443336708558632.sh
+ ./runme.sh
./runme.sh: line 71: docker-machine: command not found
ERROR: Should install docker >= 1.10.0 (have 1.9.1)
Build step 'Execute shell' marked build as failure
Notifying upstream projects of job completion
Finished: FAILURE
```

See related:

* https://github.com/gmacario/easy-jenkins/issues/35
* https://github.com/gmacario/easy-jenkins/issues/36
* https://github.com/gmacario/easy-jenkins/pull/11

<!-- EOF -->
