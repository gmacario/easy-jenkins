# Building easy-jenkins inside easy-jenkins

TODO

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to section **Preparation** of [howto-build-gdp.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/howto-build-gdp.md) fpr details.

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

id
pwd

docker --version
docker version
docker info

# EOF
```
  
  then click **Save**

## Build project `build_easyjenkins`

* Browse `${JENKINS_URL}/job/build_easyjenkins`, then click **Build Now**

Result: SUCCESS

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building on master in workspace /var/jenkins_home/workspace/build_easyjenkins_freestyle
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/gmacario/easy-jenkins # timeout=10
Fetching upstream changes from https://github.com/gmacario/easy-jenkins
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/easy-jenkins +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision 06c6c5b230769f16c0b0f2fe2d7d061f96ee2182 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 06c6c5b230769f16c0b0f2fe2d7d061f96ee2182
 > git rev-list 06c6c5b230769f16c0b0f2fe2d7d061f96ee2182 # timeout=10
[build_easyjenkins_freestyle] $ /bin/bash -xe /tmp/hudson5564369139302359507.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/workspace/build_easyjenkins_freestyle
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
 Pool Name: docker-0:32-251-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 10.74 GB
 Backing Filesystem: ext4
 Data file: /dev/loop0
 Metadata file: /dev/loop1
 Data Space Used: 305.7 MB
 Data Space Total: 107.4 GB
 Data Space Available: 47.72 GB
 Metadata Space Used: 725 kB
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
Total Memory: 2.914 GiB
Name: 62aed343b8f5
ID: MH7H:JLY7:HS2B:4HSU:6F5I:XBRB:GSYS:ZHLM:RVI7:DIN5:ACJQ:L6SW
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
Notifying upstream projects of job completion
Finished: SUCCESS
```

Notice that there are no containers in the Docker engine - which means this is not the same engine where easy-build is running.

**TODO**: Should not use Docker-in-Docker for service myjenkins.
See [this blog post](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/) to understand why.

<!-- EOF -->
