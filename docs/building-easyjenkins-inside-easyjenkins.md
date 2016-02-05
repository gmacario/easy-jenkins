# Building easy-jenkins inside easy-jenkins

This document contains instructions for building easy-jenkins inside easy-jenkins.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to section **Preparation** of [howto-build-gdp.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/howto-build-gdp.md) for details.

## Test Docker inside the Jenkins master node

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
Building on master in workspace /var/jenkins_home/jobs/test_docker/workspace
[workspace] $ /bin/bash -xe /tmp/hudson1129037539263121207.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/jobs/test_docker/workspace
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
 Pool Name: docker-0:44-262-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 107.4 GB
 Backing Filesystem: 
 Data file: /dev/loop2
 Metadata file: /dev/loop3
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
CPUs: 8
Total Memory: 15.67 GiB
Name: e1d192d7db58
ID: 6QSX:2PIP:D44J:77ZL:WEIY:D5AM:HUWT:CPDY:YTA3:UZT6:CUIK:X2X2
WARNING: No swap limit support
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
Notifying upstream projects of job completion
Finished: SUCCESS
```

Notice that there are no containers inside this Docker engine - which means it is a different engine from the one where easy-jenkins services are running.

**TODO**: Should not use Docker-in-Docker for service myjenkins.
See [this blog post](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/) to understand why.

## Configure project `build_easyjenkins`

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

# TODO

# EOF
```
  
  then click **Save**

TODO

## Build project `build_easyjenkins`

* Browse `${JENKINS_URL}/job/build_easyjenkins`, then click **Build Now**

TODO
<!-- EOF -->
