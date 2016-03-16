# Building an Android 5.1.1 image for the UDOO Neo

**WORK-IN-PROGRESS**

This document explains how to build from sources an [Android](https://source.android.com/) image for the [UDOO Neo](http://www.udoo.org/udoo-neo/) using [easy-jenkins](https://github.com/gmacario/easy-jenkins).

The following instructions were tested on

* Docker client: mac-tizy (HW: MacBook Pro; SW: OS X 10.11.3, Docker Toolbox 10.0)
* Docker engine: mv-linux-powerhorse (HW: HP xw8600 Workstation, SW: Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.3)

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Configure project `build_android_udooneo`

Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_android_udooneo`
  - Type: **Freestyle project**

  then click **OK**.

Inside the project configuration page, fill-in the following information:
  - Discard Old Builds: Yes
    - Strategy: Log Rotation
      - Days to keep builds: (none)
      - Max # of builds to keep: 2
  - Source Code Management: None
  - Build Environment
    - Build inside a Docker container: Yes
      - Docker image to use: Pull docker image from repository
        - Image id/tag: `gmacario/build-aosp`
      - Advanced...
        - force Pull: Yes
  - Build
    - Execute shell
      - Command

```
#!/bin/bash -xe

# DEBUG
id
pwd
ls -la
printenv | sort
cat /etc/passwd

# Global configs (required by repo)
git config --global user.name "easy-jenkins"
git config --global user.email "jenkins@${HOSTNAME}"
git config --global color.ui auto

# From https://source.android.com/source/downloading.html
repo init -u https://github.com/UDOOboard/android_udoo_platform_manifest -b android-5.1.1
repo sync --force-sync

# Workaround for https://github.com/gmacario/easy-jenkins/issues/85
export WITHOUT_HOST_CLANG=true

# From README_compile_Android.txt
source build/envsetup.sh
lunch udooneo_6sx-eng
make -j 8
./prepare_distro.sh

# EOF
```
- Post-build Actions
    - Archive the artifacts
      - Files to archive: `out/TODO`

then click **Save**

### Build project `build_android_udooneo`

Browse `${JENKINS_URL}/job/build_android_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_android_udooneo/lastBuild/console`

<!-- (2016-03-16 16:15 CET) http://mv-linux-powerhorse.solarma.it:9080/job/build_android_udooneo/lastBuild/console -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_android_udooneo/workspace
Pull Docker image gmacario/build-aosp from repository ...
$ docker pull gmacario/build-aosp
Docker container f8da08dea12b270399e03a7efcbad47f06423670b5781d690400307d4e776c71 started to host the build
$ docker exec --tty f8da08dea12b270399e03a7efcbad47f06423670b5781d690400307d4e776c71 env
...
+ make -j 8
++ date +%s
+ local start_time=1458141000
++ get_make_command
++ echo command make
+ command make -j 8
+ make -j 8
============================================
PLATFORM_VERSION_CODENAME=REL
PLATFORM_VERSION=5.1.1
TARGET_PRODUCT=udooneo_6sx
TARGET_BUILD_VARIANT=eng
TARGET_BUILD_TYPE=release
TARGET_BUILD_APPS=
TARGET_ARCH=arm
TARGET_ARCH_VARIANT=armv7-a-neon
TARGET_CPU_VARIANT=cortex-a9
TARGET_2ND_ARCH=
TARGET_2ND_ARCH_VARIANT=
TARGET_2ND_CPU_VARIANT=
HOST_ARCH=x86_64
HOST_OS=linux
HOST_OS_EXTRA=Linux-3.13.0-79-generic-x86_64-with-Ubuntu-16.04-xenial
HOST_BUILD_TYPE=release
BUILD_ID=2.1.0-ga-rc3
OUT_DIR=out
============================================
including ./abi/cpp/Android.mk ...
...
Import includes file: out/host/linux-x86/obj32/EXECUTABLES/llvm-tblgen_intermediates/import_includes
  SORTEX  vmlinux
  SYSMAP  System.map
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/Error.cpp
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/Main.cpp
  OBJCOPY arch/arm/boot/Image
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/Record.cpp
  Kernel: arch/arm/boot/Image is ready
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/SetTheory.cpp
  AS      arch/arm/boot/compressed/head.o
  LZO     arch/arm/boot/compressed/piggy.lzo
  CC      arch/arm/boot/compressed/misc.o
  CC      arch/arm/boot/compressed/decompress.o
  CC      arch/arm/boot/compressed/string.o
  SHIPPED arch/arm/boot/compressed/hyp-stub.S
  SHIPPED arch/arm/boot/compressed/lib1funcs.S
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/StringMatcher.cpp
  SHIPPED arch/arm/boot/compressed/ashldi3.S
  SHIPPED arch/arm/boot/compressed/bswapsdi2.S
  AS      arch/arm/boot/compressed/hyp-stub.o
  AS      arch/arm/boot/compressed/lib1funcs.o
  AS      arch/arm/boot/compressed/ashldi3.o
  AS      arch/arm/boot/compressed/bswapsdi2.o
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/TableGenBackend.cpp
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/TGLexer.cpp
host C++: libLLVMTableGen_32 <= external/llvm/lib/TableGen/TGParser.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/Allocator.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/APFloat.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/APInt.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/APSInt.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/ARMBuildAttrs.cpp
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/ARMWinEH.cpp
  AS      arch/arm/boot/compressed/piggy.lzo.o
  LD      arch/arm/boot/compressed/vmlinux
  OBJCOPY arch/arm/boot/zImage
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/Atomic.cpp
  Kernel: arch/arm/boot/zImage is ready
  UIMAGE  arch/arm/boot/uImage
"mkimage" command not found - U-Boot images will not be built
/var/jenkins_home/jobs/build_android_udooneo/workspace/kernel_imx/arch/arm/boot/Makefile:80: recipe for target 'arch/arm/boot/uImage' failed
make[3]: *** [arch/arm/boot/uImage] Error 1
/var/jenkins_home/jobs/build_android_udooneo/workspace/kernel_imx/arch/arm/Makefile:307: recipe for target 'uImage' failed
make[2]: *** [uImage] Error 2
Makefile:133: recipe for target 'sub-make' failed
make[1]: *** [sub-make] Error 2
build/core/Makefile:980: recipe for target 'out/target/product/udooneo_6sx/kernel' failed
make: *** [out/target/product/udooneo_6sx/kernel] Error 2
make: *** Waiting for unfinished jobs....
host C++: libLLVMSupport_32 <= external/llvm/lib/Support/BlockFrequency.cpp
make: *** wait: No child processes.  Stop.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

**TODO**: Install tool `mkimage` in Docker image `gmacario/build-aosp` - See https://github.com/gmacario/easy-build/pull/229
