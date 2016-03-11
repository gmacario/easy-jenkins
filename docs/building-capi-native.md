# Building Common API C in Jenkins

## Prerequisites

* A recent version of [Jenkins](https://jenkins-ci.org/) CI/CD installed together with the necessary plugins
  - Tested with https://github.com/gmacario/easy-jenkins
* An Internet browser able to access the Jenkins dashboard at `${JENKINS_URL}`
  - Example: http://192.168.99.100:9080/

## Step-by-step instructions

### Create project `common-api-c`

Browse `${DOCKER_URL}`, then click **New Item**

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
    - Docker image to use: Build from Dockerfile
      - path to docker context: `doc`
      - Dockerfile: `Dockerfile`
* Build
  - Execute shell
    - Command
```
# DEBUG
id
ls -la
```
  - Execute shell
    - Command
```
# Actual build steps
autoreconf -i
./configure
make
sudo make install
```

then click **Save**.

### Build project `common-api-c`

Browse `${DOCKER_URL}/jobs/common-api-c`, then click **Build Now**

Result: SUCCESS

Excerpt from build console:

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/common-api-c
...
Build Docker image from doc/Dockerfile ...
$ docker build --file /var/jenkins_home/workspace/common-api-c/doc/Dockerfile /var/jenkins_home/workspace/common-api-c/doc
Sending build context to Docker daemon 18.43 kB

Step 1 : FROM ubuntu:16.04
 ---> 11f63f15630c
Step 2 : MAINTAINER Gianpaolo Macario <gianpaolo_macario@mentor.com>
 ---> Using cache
 ---> 91dcecfa92c7
Step 3 : RUN apt-get update && apt-get -y dist-upgrade
 ---> Using cache
 ---> bae022c700db
Step 4 : RUN apt-get -y install autoconf build-essential file git libtool make sudo
 ---> Using cache
 ---> caa90d416c8a
Step 5 : RUN apt-get -y install libsystemd-dev pkg-config
 ---> Using cache
 ---> b12bab8f0c8d
Step 6 : RUN id build 2>/dev/null || useradd -m build
 ---> Using cache
 ---> de67c0f76511
Step 7 : RUN echo "build ALL=(ALL) NOPASSWD: ALL" | tee -a /etc/sudoers
 ---> Using cache
 ---> 9c7b1bd05298
Successfully built 9c7b1bd05298
Docker container b50a1d08d1a85e667549d6ec2ef6205cf0af0d9c625ccf268918b7bab54b7566 started to host the build
$ docker exec --tty b50a1d08d1a85e667549d6ec2ef6205cf0af0d9c625ccf268918b7bab54b7566 env
[common-api-c] $ docker exec --tty --user 0:0 b50a1d08d1a85e667549d6ec2ef6205cf0af0d9c625ccf268918b7bab54b7566 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
 b="${f%.override}";
 echo "$f" >> "$COPY_REFERENCE_FILE_LOG";
 rel="${b:23}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "$COPY_REFERENCE_FILE_LOG";
 if [[ ! -e /var/jenkins_home/${rel} || $f = *.override ]]; then
 echo "copy $rel to JENKINS_HOME" >> "$COPY_REFERENCE_FILE_LOG";
 mkdir -p "/var/jenkins_home/${dir:23}";
 cp -r "${f}" "/var/jenkins_home/${rel}";
 [[ ${rel} == plugins/*.jpi ]] && touch "/var/jenkins_home/${rel}.pinned";
 fi
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#2 BUILD_ID=2 BUILD_NUMBER=2 BUILD_TAG=jenkins-common-api-c-2 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=0 GIT_BRANCH=origin/feat-build-in-docker GIT_COMMIT=c05ad6692ff31467da4dfb8c28ff5454f98b9d9f GIT_PREVIOUS_COMMIT=c05ad6692ff31467da4dfb8c28ff5454f98b9d9f GIT_PREVIOUS_SUCCESSFUL_COMMIT=c05ad6692ff31467da4dfb8c28ff5454f98b9d9f GIT_URL=https://github.com/gmacario/common-api-c-poc HOME=/root HOSTNAME=f809e1210c90 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=16c08e0031ecb35c JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=16c08e0031ecb35c JENKINS_SHA=6a0213256670a00610a3e09203850a0fcf1a688e JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.1 JOB_NAME=common-api-c LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/workspace/common-api-c /bin/sh -xe /tmp/hudson5538845131742388246.sh
+ autoreconf -i
libtoolize: Consider adding 'AC_CONFIG_MACRO_DIRS([m4])' to configure.ac,
libtoolize: and rerunning libtoolize and aclocal.
libtoolize: Consider adding '-I m4' to ACLOCAL_AMFLAGS in Makefile.am.
+ ./configure
checking for a BSD-compatible install... /usr/bin/install -c
...
config.status: executing libtool commands
+ make
make  all-am
make[1]: Entering directory '/var/jenkins_home/workspace/common-api-c'
make[1]: Leaving directory '/var/jenkins_home/workspace/common-api-c'
+ sudo make install
make[1]: Entering directory '/var/jenkins_home/workspace/common-api-c'
 /bin/mkdir -p '/usr/local/lib'
 /bin/bash ./libtool   --mode=install /usr/bin/install -c   libcapic.la '/usr/local/lib'
libtool: install: /usr/bin/install -c .libs/libcapic.so.0.0.0 /usr/local/lib/libcapic.so.0.0.0
libtool: install: (cd /usr/local/lib && { ln -s -f libcapic.so.0.0.0 libcapic.so.0 || { rm -f libcapic.so.0 && ln -s libcapic.so.0.0.0 libcapic.so.0; }; })
libtool: install: (cd /usr/local/lib && { ln -s -f libcapic.so.0.0.0 libcapic.so || { rm -f libcapic.so && ln -s libcapic.so.0.0.0 libcapic.so; }; })
libtool: install: /usr/bin/install -c .libs/libcapic.lai /usr/local/lib/libcapic.la
libtool: finish: PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/sbin" ldconfig -n /usr/local/lib
----------------------------------------------------------------------
Libraries have been installed in:
   /usr/local/lib

If you ever happen to want to link against installed libraries
in a given directory, LIBDIR, you must either use libtool, and
specify the full pathname of the library, or use the '-LLIBDIR'
flag during linking and do at least one of the following:
   - add LIBDIR to the 'LD_LIBRARY_PATH' environment variable
     during execution
   - add LIBDIR to the 'LD_RUN_PATH' environment variable
     during linking
   - use the '-Wl,-rpath -Wl,LIBDIR' linker flag
   - have your system administrator add LIBDIR to '/etc/ld.so.conf'

See any operating system documentation about shared libraries for
more information, such as the ld(1) and ld.so(8) manual pages.
----------------------------------------------------------------------
 /bin/mkdir -p '/usr/local/lib/pkgconfig'
 /usr/bin/install -c -m 644 capic.pc '/usr/local/lib/pkgconfig'
 /bin/mkdir -p '/usr/local/include/capic'
 /usr/bin/install -c -m 644 src/capic/backend.h src/capic/log.h src/capic/dbus-private.h '/usr/local/include/capic'
make[1]: Leaving directory '/var/jenkins_home/workspace/common-api-c'
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: SUCCESS
```

<!-- EOF -->
