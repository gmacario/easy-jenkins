# Building GENIVI Common API C in Jenkins

## Prerequisites

* A recent version of [Jenkins](https://jenkins-ci.org/) CI/CD installed together with the necessary plugins
  - Tested with https://github.com/gmacario/easy-jenkins
* An Internet browser able to access the Jenkins dashboard at `${JENKINS_URL}`
  - Example: http://192.168.99.100:9080/

## Step-by-step instructions

### Create folder `GENIVI`

Browse `${DOCKER_URL}`, then click **New Item**

* Name: `GENIVI`
* Type: **Folder**

then click **OK**. Inside the project configuration page, review configuration, then click **OK**.

### Create project `common-api-c`

Browse `${DOCKER_URL}/job/GENIVI`, then click **New Item**

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
    - Docker image to use: Pull docker image from repository
      - Image id/tag: `gmacario/build-capi-native`
      - Advanced...
        - force Pull: Yes
* Build
  - Execute shell
    - Command
```
#!/bin/bash -xe

# DEBUG
id
ls -la

# Actual build steps
autoreconf -i
./configure
make
sudo make install

# EOF
```

then click **Save**.

### Build project `common-api-c`

Browse `${DOCKER_URL}/job/GENIVI/job/common-api-c`, then click **Build Now**

Result: SUCCESS

Excerpt from build console:

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace
Cloning the remote Git repository
Cloning repository git://git.projects.genivi.org/common-api/c-poc.git
 > git init /var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/common-api/c-poc.git
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/common-api/c-poc.git +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url git://git.projects.genivi.org/common-api/c-poc.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url git://git.projects.genivi.org/common-api/c-poc.git # timeout=10
Fetching upstream changes from git://git.projects.genivi.org/common-api/c-poc.git
 > git -c core.askpass=true fetch --tags --progress git://git.projects.genivi.org/common-api/c-poc.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision d6ec42ce45c33f40560d1f24b9143e9b1e6816e9 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f d6ec42ce45c33f40560d1f24b9143e9b1e6816e9
First time build. Skipping changelog.
Pull Docker image gmacario/build-capi-native from repository ...
$ docker pull gmacario/build-capi-native
Docker container 9868e2db72d66222b443d7ce4389e3511fbb479ccc54bd72d5acf91c37c70e60 started to host the build
$ docker exec --tty 9868e2db72d66222b443d7ce4389e3511fbb479ccc54bd72d5acf91c37c70e60 env
[workspace] $ docker exec --tty --user 1000:1000 9868e2db72d66222b443d7ce4389e3511fbb479ccc54bd72d5acf91c37c70e60 env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#1 BUILD_ID=1 BUILD_NUMBER=1 BUILD_TAG=jenkins-GENIVI-common-api-c-1 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=1 GIT_BRANCH=origin/master GIT_COMMIT=d6ec42ce45c33f40560d1f24b9143e9b1e6816e9 GIT_URL=git://git.projects.genivi.org/common-api/c-poc.git HOME=/var/jenkins_home HOSTNAME=9a260eff78c8 HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=d73dd7a3cff408ad JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=d73dd7a3cff408ad JENKINS_SHA=e72e06e64d23eefb13090459f517b0697aad7be0 JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.2 JOB_NAME=GENIVI/common-api-c LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace /bin/bash -xe /tmp/hudson7515196655354952219.sh
+ id
uid=1000(build) gid=1000(build) groups=1000(build)
+ ls -la
total 92
drwxr-xr-x  8 build build  4096 Mar 11 14:40 .
drwxr-xr-x  4 build build  4096 Mar 11 14:40 ..
drwxr-xr-x  8 build build  4096 Mar 11 14:40 .git
-rw-r--r--  1 build build   199 Mar 11 14:40 .gitignore
-rw-r--r--  1 build build 11250 Mar 11 14:40 LICENSE.EPL-1.0
-rw-r--r--  1 build build 16726 Mar 11 14:40 LICENSE.MPL-2.0
-rw-r--r--  1 build build   870 Mar 11 14:40 Makefile.am
-rw-r--r--  1 build build  3019 Mar 11 14:40 NEWS
-rw-r--r--  1 build build  5758 Mar 11 14:40 README.adoc
-rw-r--r--  1 build build   235 Mar 11 14:40 capic.pc.in
-rw-r--r--  1 build build  1228 Mar 11 14:40 configure.ac
drwxr-xr-x  2 build build  4096 Mar 11 14:40 doc
drwxr-xr-x  5 build build  4096 Mar 11 14:40 ref
drwxr-xr-x  3 build build  4096 Mar 11 14:40 src
drwxr-xr-x  2 build build  4096 Mar 11 14:40 test
drwxr-xr-x 13 build build  4096 Mar 11 14:40 tools
+ autoreconf -i
libtoolize: putting auxiliary files in '.'.
libtoolize: copying file './ltmain.sh'
libtoolize: Consider adding 'AC_CONFIG_MACRO_DIRS([m4])' to configure.ac,
libtoolize: and rerunning libtoolize and aclocal.
libtoolize: Consider adding '-I m4' to ACLOCAL_AMFLAGS in Makefile.am.
configure.ac:18: installing './ar-lib'
configure.ac:17: installing './compile'
configure.ac:21: installing './config.guess'
configure.ac:21: installing './config.sub'
configure.ac:16: installing './install-sh'
configure.ac:16: installing './missing'
Makefile.am: installing './depcomp'
+ ./configure
checking for a BSD-compatible install... /usr/bin/install -c
checking whether build environment is sane... yes
checking for a thread-safe mkdir -p... /bin/mkdir -p
checking for gawk... no
checking for mawk... mawk
checking whether make sets $(MAKE)... yes
checking whether make supports nested variables... yes
checking for gcc... gcc
checking whether the C compiler works... yes
checking for C compiler default output file name... a.out
checking for suffix of executables... 
checking whether we are cross compiling... no
checking for suffix of object files... o
checking whether we are using the GNU C compiler... yes
checking whether gcc accepts -g... yes
checking for gcc option to accept ISO C89... none needed
checking whether gcc understands -c and -o together... yes
checking for style of include used by make... GNU
checking dependency style of gcc... gcc3
checking for ar... ar
checking the archiver (ar) interface... ar
checking build system type... x86_64-pc-linux-gnu
checking host system type... x86_64-pc-linux-gnu
checking how to print strings... printf
checking for a sed that does not truncate output... /bin/sed
checking for grep that handles long lines and -e... /bin/grep
checking for egrep... /bin/grep -E
checking for fgrep... /bin/grep -F
checking for ld used by gcc... /usr/bin/ld
checking if the linker (/usr/bin/ld) is GNU ld... yes
checking for BSD- or MS-compatible name lister (nm)... /usr/bin/nm -B
checking the name lister (/usr/bin/nm -B) interface... BSD nm
checking whether ln -s works... yes
checking the maximum length of command line arguments... 1572864
checking how to convert x86_64-pc-linux-gnu file names to x86_64-pc-linux-gnu format... func_convert_file_noop
checking how to convert x86_64-pc-linux-gnu file names to toolchain format... func_convert_file_noop
checking for /usr/bin/ld option to reload object files... -r
checking for objdump... objdump
checking how to recognize dependent libraries... pass_all
checking for dlltool... no
checking how to associate runtime and link libraries... printf %s\n
checking for archiver @FILE support... @
checking for strip... strip
checking for ranlib... ranlib
checking command to parse /usr/bin/nm -B output from gcc object... ok
checking for sysroot... no
checking for a working dd... /bin/dd
checking how to truncate binary pipes... /bin/dd bs=4096 count=1
checking for mt... no
checking if : is a manifest tool... no
checking how to run the C preprocessor... gcc -E
checking for ANSI C header files... yes
checking for sys/types.h... yes
checking for sys/stat.h... yes
checking for stdlib.h... yes
checking for string.h... yes
checking for memory.h... yes
checking for strings.h... yes
checking for inttypes.h... yes
checking for stdint.h... yes
checking for unistd.h... yes
checking for dlfcn.h... yes
checking for objdir... .libs
checking if gcc supports -fno-rtti -fno-exceptions... no
checking for gcc option to produce PIC... -fPIC -DPIC
checking if gcc PIC flag -fPIC -DPIC works... yes
checking if gcc static flag -static works... yes
checking if gcc supports -c -o file.o... yes
checking if gcc supports -c -o file.o... (cached) yes
checking whether the gcc linker (/usr/bin/ld -m elf_x86_64) supports shared libraries... yes
checking whether -lc should be explicitly linked in... no
checking dynamic linker characteristics... GNU/Linux ld.so
checking how to hardcode library paths into programs... immediate
checking whether stripping libraries is possible... yes
checking if libtool supports shared libraries... yes
checking whether to build shared libraries... yes
checking whether to build static libraries... no
checking for pkg-config... /usr/bin/pkg-config
checking pkg-config is at least version 0.9.0... yes
checking for LIBSYSTEMD... yes
checking for sd_bus_open in -lsystemd... yes
checking for sd_bus_get_scope in -lsystemd... yes
checking whether SD_EVENT_INITIAL is declared... yes
checking that generated files are newer than configure... done
configure: creating ./config.status
config.status: creating Makefile
config.status: creating capic.pc
config.status: creating config.h
config.status: executing depfiles commands
config.status: executing libtool commands
+ make
make  all-am
make[1]: Entering directory '/var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace'
/bin/bash ./libtool  --tag=CC   --mode=compile gcc -DHAVE_CONFIG_H -I.    -I./src -I.  -Wall -Werror -fvisibility=hidden -g -O2 -MT src/libcapic_la-backend.lo -MD -MP -MF src/.deps/libcapic_la-backend.Tpo -c -o src/libcapic_la-backend.lo `test -f 'src/backend.c' || echo './'`src/backend.c
libtool: compile:  gcc -DHAVE_CONFIG_H -I. -I./src -I. -Wall -Werror -fvisibility=hidden -g -O2 -MT src/libcapic_la-backend.lo -MD -MP -MF src/.deps/libcapic_la-backend.Tpo -c src/backend.c  -fPIC -DPIC -o src/.libs/libcapic_la-backend.o
mv -f src/.deps/libcapic_la-backend.Tpo src/.deps/libcapic_la-backend.Plo
/bin/bash ./libtool  --tag=CC   --mode=link gcc -I./src -I.  -Wall -Werror -fvisibility=hidden -g -O2 -no-undefined -version-info 0:0:0  -o libcapic.la -rpath /usr/local/lib  src/libcapic_la-backend.lo  
libtool: link: gcc -shared  -fPIC -DPIC  src/.libs/libcapic_la-backend.o    -g -O2   -Wl,-soname -Wl,libcapic.so.0 -o .libs/libcapic.so.0.0.0
libtool: link: (cd ".libs" && rm -f "libcapic.so.0" && ln -s "libcapic.so.0.0.0" "libcapic.so.0")
libtool: link: (cd ".libs" && rm -f "libcapic.so" && ln -s "libcapic.so.0.0.0" "libcapic.so")
libtool: link: ( cd ".libs" && rm -f "libcapic.la" && ln -s "../libcapic.la" "libcapic.la" )
make[1]: Leaving directory '/var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace'
+ sudo make install
make[1]: Entering directory '/var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace'
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
make[1]: Leaving directory '/var/jenkins_home/jobs/GENIVI/jobs/common-api-c/workspace'
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: SUCCESS
```

<!-- EOF -->
