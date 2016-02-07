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
Building in workspace /var/jenkins_home/jobs/test_docker/workspace
[workspace] $ /bin/bash -xe /tmp/hudson5596916223361323318.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/jobs/test_docker/workspace
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
 Pool Name: docker-0:33-181-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 10.74 GB
 Backing Filesystem: ext4
 Data file: /dev/loop2
 Metadata file: /dev/loop3
 Data Space Used: 305.7 MB
 Data Space Total: 107.4 GB
 Data Space Available: 47.34 GB
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
 Network: bridge null host
Kernel Version: 4.1.17-boot2docker
Operating System: Debian GNU/Linux 8 (jessie) (containerized)
OSType: linux
Architecture: x86_64
CPUs: 2
Total Memory: 1.956 GiB
Name: 1bac2c6e2590
ID: CJPD:RMI5:UHWR:ASWD:UY5K:ZZ6L:BHPX:KVYR:C2FT:PXVS:I5SO:OHO7
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
+ docker-compose --version
docker-compose version 1.6.0, build d99cad6
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

docker-compose build --pull

# Uncomment after merging https://github.com/gmacario/easy-jenkins/pull/11
# ./runme.sh

# EOF
```
  
  then click **Save**

## Build project `build_easyjenkins_freestyle`

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
