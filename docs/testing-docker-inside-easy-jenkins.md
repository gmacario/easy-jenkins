# Testing Docker configuration inside easy-jenkins

This procedure is used to verify the correct installation of [Docker](https://www.docker.com/) tools inside service `myjenkins`, a.k.a. the Jenkins `master` node.

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to section **Preparation** of [howto-build-gdp.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/howto-build-gdp.md) for details.

## Step-by-step instructions

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
docker-compose --version
docker-machine --version || true

docker version
docker info
docker images
docker ps
docker volume ls

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
[workspace] $ /bin/bash -xe /tmp/hudson2360723683233882849.sh
+ id
uid=0(root) gid=0(root) groups=0(root)
+ pwd
/var/jenkins_home/jobs/test_docker/workspace
+ docker --version
Docker version 1.10.0, build 590d5108
+ docker-compose --version
docker-compose version 1.6.0, build d99cad6
+ docker-machine --version
/tmp/hudson2360723683233882849.sh: line 8: docker-machine: command not found
+ true
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
Containers: 1
 Running: 0
 Paused: 0
 Stopped: 1
Images: 16
Server Version: 1.10.0
Storage Driver: devicemapper
 Pool Name: docker-0:33-181-pool
 Pool Blocksize: 65.54 kB
 Base Device Size: 10.74 GB
 Backing Filesystem: ext4
 Data file: /dev/loop2
 Metadata file: /dev/loop3
 Data Space Used: 1.384 GB
 Data Space Total: 107.4 GB
 Data Space Available: 46.26 GB
 Metadata Space Used: 2.63 MB
 Metadata Space Total: 2.147 GB
 Metadata Space Available: 2.145 GB
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
 Network: host bridge null
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
+ docker images
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
workspace_myjenkins   latest              144d1b05ba44        5 minutes ago       927.6 MB
jenkins               latest              997d1b2b89a5        11 days ago         708.4 MB
+ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
+ docker volume ls
DRIVER              VOLUME NAME
local               04a5744717782810667775297d88e0684a86f5045748c417959cb3b8760e2071
Notifying upstream projects of job completion
Finished: SUCCESS
```

Notice that there are no containers inside this Docker engine - which means it is a different engine from the one where easy-jenkins services are running.

**TODO**: Do not use Docker-in-Docker for service myjenkins. See related https://github.com/gmacario/easy-jenkins/issues/31

<!-- EOF -->
