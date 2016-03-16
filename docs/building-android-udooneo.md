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

<!-- (2016-03-16 17:22 CET) http://mv-linux-powerhorse.solarma.it:9080/job/build_android_udooneo/lastBuild/console -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_android_udooneo/workspace
Pull Docker image gmacario/build-aosp:feat-aosp-uboottools from repository ...
$ docker pull gmacario/build-aosp:feat-aosp-uboottools
Docker container 979360a119f5cb28ef5d82654e642a7fef8076b1d0e65e4ac0f4f7a22d66614e started to host the build
$ docker exec --tty 979360a119f5cb28ef5d82654e642a7fef8076b1d0e65e4ac0f4f7a22d66614e env
...
+ make -j 8
++ date +%s
+ local start_time=1458145403
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
including ./tools/external/fat32lib/Android.mk ...
No private recovery resources for TARGET_DEVICE udooneo_6sx
build/core/main.mk:1056: warning: overriding recipe for target 'clean'
external/tools/iw/Makefile:126: warning: ignoring old recipe for target 'clean'
host Executable: validatekeymaps (out/host/linux-x86/obj32/EXECUTABLES/validatekeymaps_intermediates/validatekeymaps)
host Executable: acp (out/host/linux-x86/obj32/EXECUTABLES/acp_intermediates/acp)
mkdir -p out/target/product/udooneo_6sx/obj/KERNEL_OBJ
for ubootplat in imx6sx:udoo_neo_android_defconfig; do \
	UBOOT_PLATFORM=`echo $ubootplat | cut -d':' -f1`; \
	UBOOT_CONFIG=`echo $ubootplat | cut -d':' -f2`; \
	UBOOT_DIR="bootable/bootloader/uboot-imx"; \
        echo ; \
        echo ; \
	make -C $UBOOT_DIR distclean ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi-; \
	make -C $UBOOT_DIR $UBOOT_CONFIG ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi-; \
	make -C $UBOOT_DIR ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi-; \
	dd if=$UBOOT_DIR/SPL of=$UBOOT_DIR/u-boot.imx bs=1K seek=0 conv=notrunc; \
	dd if=$UBOOT_DIR/u-boot.img of=$UBOOT_DIR/u-boot.imx bs=1K seek=68; \
	install -D $UBOOT_DIR/u-boot.imx out/target/product/udooneo_6sx/u-boot-$UBOOT_PLATFORM.imx; \
done
rm -f out/target/product/udooneo_6sx/kernel


host StaticLib: libziparchive-host_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libziparchive-host_intermediates/libziparchive-host.a)
make -C kernel_imx udoo_neo_android_defconfig ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi- LOADADDR=0x80008000 O=`pwd`/out/target/product/udooneo_6sx/obj/KERNEL_OBJ 
  CLEAN   examples/standalone
host StaticLib: libzipfile_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libzipfile_intermediates/libzipfile.a)
  CLEAN   tools
  CLEAN   tools/lib tools/common
  CLEAN   scripts/basic
  CLEAN   scripts/kconfig
  CLEAN   spl/arch spl/board spl/common spl/drivers spl/fs spl/lib spl/u-boot-spl spl/u-boot-spl.bin spl/u-boot-spl.lds spl/u-boot-spl.map
host SharedLib: libunwind-ptrace_32 (out/host/linux-x86/obj32/lib/libunwind-ptrace.so)
  GEN     /var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx/obj/KERNEL_OBJ/Makefile
  CLEAN   u-boot.lds u-boot.map u-boot.imx u-boot.bin u-boot.srec u-boot u-boot.img SPL System.map
preparing StaticLib: libunwindbacktrace_32 [including  out/host/linux-x86/obj32/STATIC_LIBRARIES/libunwind_intermediates/libunwind.a]
  CLEAN   include/config include/generated spl
  CLEAN   .config include/autoconf.mk include/autoconf.mk.dep include/config.h
host StaticLib: libLLVMSupport_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libLLVMSupport_intermediates/libLLVMSupport.a)
host StaticLib: libbccRenderscript_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libbccRenderscript_intermediates/libbccRenderscript.a)
  HOSTCC  scripts/basic/fixdep
host StaticLib: libbccExecutionEngine_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libbccExecutionEngine_intermediates/libbccExecutionEngine.a)
host StaticLib: libbccCore_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libbccCore_intermediates/libbccCore.a)
host StaticLib: libunwindbacktrace_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libunwindbacktrace_intermediates/libunwindbacktrace.a)
#
# configuration written to .config
#
make -C kernel_imx -j8 prepare ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi- LOADADDR=0x80008000 O=`pwd`/out/target/product/udooneo_6sx/obj/KERNEL_OBJ 
make[1]: warning: -jN forced in submake: disabling jobserver mode.
host StaticLib: libbccSupport_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/libbccSupport_intermediates/libbccSupport.a)
  HOSTCC  scripts/kconfig/conf.o
  SHIPPED scripts/kconfig/zconf.hash.c
  SHIPPED scripts/kconfig/zconf.tab.c
  SHIPPED scripts/kconfig/zconf.lex.c
host StaticLib: librsloader_32 (out/host/linux-x86/obj32/STATIC_LIBRARIES/librsloader_intermediates/librsloader.a)
host C++: dalvikvm_32 <= art/dalvikvm/dalvikvm.cc
if [ ! -e include/config/auto.conf ]; then make -f /var/jenkins_home/jobs/build_android_udooneo/workspace/kernel_imx/Makefile silentoldconfig ; fi
  CHK     include/config/kernel.release
  GEN     /var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx/obj/KERNEL_OBJ/Makefile
  HOSTCC  scripts/kconfig/zconf.tab.o
  CHK     include/generated/uapi/linux/version.h
host SharedLib: libnativehelper_32 (out/host/linux-x86/obj32/lib/libnativehelper.so)
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/SHARED_LIBRARIES/libnativehelper_intermediates/JNIHelp.o: unsupported reloc 43 against global symbol std::string::_Rep::_S_empty_rep_storage
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/SHARED_LIBRARIES/libnativehelper_intermediates/JNIHelp.o: unsupported reloc 43 against global symbol std::string::_Rep::_S_empty_rep_storage
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/SHARED_LIBRARIES/libnativehelper_intermediates/JNIHelp.o: unsupported reloc 43 against global symbol std::string::_Rep::_S_empty_rep_storage
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/SHARED_LIBRARIES/libnativehelper_intermediates/JNIHelp.o: unsupported reloc 43 against global symbol std::string::_Rep::_S_empty_rep_storage
...
  OBJCOPY spl/u-boot-spl.bin
  CFGS    arch/arm/imx-common/spl_sd.cfg.cfgtmp
  MKIMAGE SPL
31+0 records in
31+0 records out
31744 bytes (32 kB, 31 KiB) copied, 0.000231922 s, 137 MB/s
256+1 records in
256+1 records out
262896 bytes (263 kB, 257 KiB) copied, 0.00121776 s, 216 MB/s
make: *** wait: No child processes.  Stop.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

**TODO**: Install tool `mkimage` in Docker image `gmacario/build-aosp` - See https://github.com/gmacario/easy-build/pull/229
