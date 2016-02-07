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
[test_docker] $ /bin/bash -xe /tmp/hudson729394599786323653.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/workspace/test_docker
+ docker --version
Docker version 1.10.0, build 590d5108
+ docker version
Client:
 Version:      1.10.0
 API version:  1.22
 Go version:   go1.5.3
 Git commit:   590d5108
 Built:        Thu Feb  4 18:16:19 2016
 OS/Arch:      linux/amd64

Server:
 Version:      1.10.0
 API version:  1.22
 Go version:   go1.5.3
 Git commit:   590d5108
 Built:        Thu Feb  4 18:16:19 2016
 OS/Arch:      linux/amd64
+ docker info
Containers: 0
 Running: 0
 Paused: 0
 Stopped: 0
Images: 0
Server Version: 1.10.0
Storage Driver: devicemapper
 Pool Name: docker-0:32-181-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 10.74 GB
 Backing Filesystem: ext4
 Data file: /dev/loop0
 Metadata file: /dev/loop1
 Data Space Used: 305.7 MB
 Data Space Total: 107.4 GB
 Data Space Available: 47.74 GB
 Metadata Space Used: 729.1 kB
 Metadata Space Total: 2.147 GB
 Metadata Space Available: 2.147 GB
 Udev Sync Supported: false
 Deferred Removal Enabled: false
 Deferred Deletion Enabled: false
 Deferred Deleted Device Count: 0
 Data loop file: /var/lib/docker/devicemapper/devicemapper/data
 WARNING: Usage of loopback devices is strongly discouraged for production use. Either use `--storage-opt dm.thinpooldev` or use `--storage-opt dm.no_warn_on_loop_devices=true` to suppress this warning.
 Metadata loop file: /var/lib/docker/devicemapper/devicemapper/metadata
 Library Version: 1.02.90 (2014-09-01)
Execution Driver: native-0.2
Logging Driver: json-file
Plugins: 
 Volume: local
 Network: null host bridge
Kernel Version: 4.1.17-boot2docker
Operating System: Debian GNU/Linux 8 (jessie) (containerized)
OSType: linux
Architecture: x86_64
CPUs: 2
Total Memory: 1.956 GiB
Name: c2eb63618350
ID: UIQS:A2YO:DHQU:EUQZ:S2N7:ZVIK:4IU3:MHYM:FE6G:J652:72LH:Z6SI
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
+ docker-compose --version
docker-compose version 1.5.2, build 7240ff3
Notifying upstream projects of job completion
Finished: SUCCESS
```

Notice that there are no containers inside this Docker engine - which means it is a different engine from the one where easy-jenkins services are running.

**TODO**: Do not use Docker-in-Docker for service myjenkins. See related https://github.com/gmacario/easy-jenkins/issues/31

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

~~**TODO**: Install docker 1.10.0 or later. See related https://github.com/gmacario/easy-jenkins/issues/35~~

**TODO**: Install docker-compose 1.6.0 or later. See related https://github.com/gmacario/easy-jenkins/issues/36

**TODO**: Handle case when no docker-machine installed. See related https://github.com/gmacario/easy-jenkins/pull/11

<!-- EOF -->
