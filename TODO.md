# TODO

* [ ] [Automatically update Jenkins plugins](https://github.com/gmacario/easy-jenkins/issues/1)
* [ ] Learn Jenkins Job DSL
* [ ] Automatically add Jenkins slave(s)
* [ ] Add Jenkins Job: build-gdp
* [ ] Add Jenkins Job: build-agl-demo

### Inspecting Jenkins master node configuration

<http://192.168.99.103:9080/computer/(master)/script>

```
println "uname -a".execute().text

println "id".execute().text

println System.getenv("PATH")
println ""

println "df -h".execute().text
```

Result:

```
Linux 4a957d54043a 4.1.13-boot2docker #1 SMP Fri Nov 20 19:05:50 UTC 2015 x86_64 GNU/Linux

uid=0(root) gid=0(root) groups=0(root)

/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

Filesystem      Size  Used Avail Use% Mounted on
none             48G  7.1G   38G  16% /
tmpfs           1.5G     0  1.5G   0% /dev
tmpfs           1.5G     0  1.5G   0% /sys/fs/cgroup
/dev/sda1        48G  7.1G   38G  16% /etc/hosts
shm              64M     0   64M   0% /dev/shm
```

### Testing Docker commands inside a Jenkins Job

<http://192.168.99.103:9080/view/All/newJob>

* Item name: `test-docker`
* Type: Freestyle project

<http://192.168.99.103:9080/view/All/job/test-docker/configure>

* Project name:
* Build > Add build step > Execute shell
** Command

```
whoami
df -h

docker --version
docker version
docker info
docker images
docker ps
docker run busybox echo Hello world

docker-compose --version

which ifconfig && ifconfig
which ip && ip addr
```

Build result:

```
Started by user anonymous
Building in workspace /var/jenkins_home/jobs/test-docker/workspace
[workspace] $ /bin/sh -xe /tmp/hudson7269085561987291954.sh
+ whoami
root
+ df -h
Filesystem      Size  Used Avail Use% Mounted on
none             48G  7.1G   38G  16% /
tmpfs           1.5G     0  1.5G   0% /dev
tmpfs           1.5G     0  1.5G   0% /sys/fs/cgroup
/dev/sda1        48G  7.1G   38G  16% /etc/hosts
shm              64M     0   64M   0% /dev/shm
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
Containers: 10
Images: 2
Server Version: 1.9.1
Storage Driver: devicemapper
  Pool Name: docker-0:33-201-pool
  Pool Blocksize: 65.54 kB
  Base Device Size: 107.4 GB
  Backing Filesystem: ext4
  Data file: /dev/loop2
  Metadata file: /dev/loop3
  Data Space Used: 1.832 GB
  Data Space Total: 107.4 GB
  Data Space Available: 42.92 GB
  Metadata Space Used: 1.946 MB
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
Kernel Version: 4.1.13-boot2docker
Operating System: Debian GNU/Linux 8 (jessie) (containerized)
CPUs: 2
Total Memory: 2.914 GiB
Name: 4a957d54043a
ID: QRJI:67X5:NE5A:2SMA:XGZ3:ELIK:TA63:VAUL:M5C5:WYC2:AEMA:JLPR
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
+ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
busybox             latest              ac6a7980c6c2        4 weeks ago         1.113 MB
+ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
+ docker run busybox echo Hello world
Hello world
+ docker-compose --version
docker-compose version 1.5.2, build 7240ff3
+ which ifconfig
+ which ip
/sbin/ip
+ ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host
       valid_lft forever preferred_lft forever
2: docker0: <BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state UP group default
    link/ether 02:42:3b:4d:f7:a3 brd ff:ff:ff:ff:ff:ff
    inet 172.18.0.1/16 scope global docker0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:3bff:fe4d:f7a3/64 scope link
       valid_lft forever preferred_lft forever
196: eth0@if197: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.2/16 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe11:2/64 scope link
       valid_lft forever preferred_lft forever
Notifying upstream projects of job completion
Finished: SUCCESS
```

### Learn Jenkins Job DSL

TODO: Follow tutorial: <https://github.com/jenkinsci/job-dsl-plugin/wiki/Tutorial---Using-the-Jenkins-Job-DSL>

TODO

--------------------------

TODO

### Automatically add Jenkins slave(s)

http://192.168.99.103:9080/computer/new

* Node name: `build-yocto-slave`
* Type: Dumb Slave

http://192.168.99.103:9080/computer/createItem

* Name: `build-yocto-slave`
* Description:
* Number of executors:
* Remote root directory: `/home/jenkins`
* Labels: `yocto`
* Usage: Utilize this node as much as possible
* Launch method: Launch slave agents via Java Web Start
* Availability: Keep this slave on-line as much as possible
* Node Properties:
  - Environment variables: None
  - Tool Locations: None

### Add Jenkins Job: build-gdp

http://192.168.99.103:9080/view/All/newJob

* Item name: `build-gdp-qemux86_64`
* Type: Freestyle project

<http://192.168.99.103:9080/job/build-gdp-qemux86_64/configure>

* Project name: `build-gdp-qemux86_64`


<!-- EOF -->
