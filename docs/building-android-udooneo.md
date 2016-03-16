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

<!-- (2016-03-16 10:28 CET) http://mv-linux-powerhorse.solarma.it:9080/job/build_android_udooneo/lastBuild/console -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/jobs/build_android_udooneo/workspace
Pull Docker image gmacario/build-aosp:feat-aosp-add-lzop from repository ...
$ docker pull gmacario/build-aosp:feat-aosp-add-lzop
Docker container 5daca1281b0512132fffd0dd28bd0977670a00fbfa45375904176cbca620a50e started to host the build
$ docker exec --tty 5daca1281b0512132fffd0dd28bd0977670a00fbfa45375904176cbca620a50e env
[workspace] $ docker exec --tty --user 1000:1000 5daca1281b0512132fffd0dd28bd0977670a00fbfa45375904176cbca620a50e env 'BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
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
}' BUILD_CAUSE=MANUALTRIGGER BUILD_CAUSE_MANUALTRIGGER=true BUILD_DISPLAY_NAME=#13 BUILD_ID=13 BUILD_NUMBER=13 BUILD_TAG=jenkins-build_android_udooneo-13 CA_CERTIFICATES_JAVA_VERSION=20140324 CLASSPATH= COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log EXECUTOR_NUMBER=0 HOME=/var/jenkins_home HOSTNAME=1dddbffd6d7e HUDSON_HOME=/var/jenkins_home HUDSON_SERVER_COOKIE=2c5d611b8254ec31 JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1 JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 JAVA_VERSION=8u72 JENKINS_HOME=/var/jenkins_home JENKINS_SERVER_COOKIE=2c5d611b8254ec31 JENKINS_SHA=e72e06e64d23eefb13090459f517b0697aad7be0 JENKINS_SLAVE_AGENT_PORT=50000 JENKINS_UC=https://updates.jenkins-ci.org JENKINS_VERSION=1.642.2 JOB_NAME=build_android_udooneo LANG=C.UTF-8 NODE_LABELS=master NODE_NAME=master PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin PWD=/ ROOT_BUILD_CAUSE=MANUALTRIGGER ROOT_BUILD_CAUSE_MANUALTRIGGER=true SHLVL=2 TERM=xterm TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888 WORKSPACE=/var/jenkins_home/jobs/build_android_udooneo/workspace /bin/bash -xe /tmp/hudson7622237467916912674.sh
+ id
uid=1000(jenkins) gid=1000(jenkins) groups=1000(jenkins)
+ pwd
/var/jenkins_home/jobs/build_android_udooneo/workspace
+ ls -la
total 132
drwxr-xr-x  27 jenkins jenkins 4096 Mar 14 14:08 .
drwxr-xr-x   4 jenkins jenkins 4096 Mar 16 09:42 ..
drwxr-xr-x   7 jenkins jenkins 4096 Mar 16 09:30 .repo
-r--r--r--   1 jenkins jenkins   87 Mar 14 12:59 Makefile
-r--r--r--   1 jenkins jenkins  741 Mar 14 12:59 README_compile_Android.txt
-r--r--r--   1 jenkins jenkins  241 Mar 14 12:59 README_make_uSD.txt
drwxr-xr-x   3 jenkins jenkins 4096 Mar 14 12:59 abi
drwxr-xr-x  14 jenkins jenkins 4096 Mar 14 12:59 art
drwxr-xr-x  10 jenkins jenkins 4096 Mar 14 12:59 bionic
drwxr-xr-x   4 jenkins jenkins 4096 Mar 14 14:03 bootable
drwxr-xr-x   8 jenkins jenkins 4096 Mar 14 12:59 build
drwxr-xr-x  12 jenkins jenkins 4096 Mar 14 12:59 cts
drwxr-xr-x  13 jenkins jenkins 4096 Mar 14 12:59 dalvik
drwxr-xr-x   6 jenkins jenkins 4096 Mar 14 12:59 developers
drwxr-xr-x  20 jenkins jenkins 4096 Mar 14 12:59 development
drwxr-xr-x   8 jenkins jenkins 4096 Mar 14 12:59 device
drwxr-xr-x 198 jenkins jenkins 4096 Mar 14 13:02 external
drwxr-xr-x  17 jenkins jenkins 4096 Mar 14 13:04 frameworks
drwxr-xr-x  15 jenkins jenkins 4096 Mar 14 13:04 hardware
drwxr-xr-x  27 jenkins jenkins 4096 Mar 14 13:04 kernel_imx
drwxr-xr-x  16 jenkins jenkins 4096 Mar 14 13:04 libcore
drwxr-xr-x   5 jenkins jenkins 4096 Mar 14 13:04 libnativehelper
-r-xr-xr-x   1 jenkins jenkins 7506 Mar 14 12:59 make_sd.sh
drwxr-xr-x   8 jenkins jenkins 4096 Mar 14 13:05 ndk
drwxr-xr-x   4 jenkins jenkins 4096 Mar 14 14:49 out
drwxr-xr-x   9 jenkins jenkins 4096 Mar 14 13:05 packages
drwxr-xr-x   6 jenkins jenkins 4096 Mar 14 13:05 pdk
drwxr-xr-x  16 jenkins jenkins 4096 Mar 14 13:10 prebuilts
-r-xr-xr-x   1 jenkins jenkins  935 Mar 14 12:59 prepare_distro.sh
drwxr-xr-x  27 jenkins jenkins 4096 Mar 14 13:10 sdk
drwxr-xr-x   9 jenkins jenkins 4096 Mar 14 13:10 system
drwxr-xr-x   3 jenkins jenkins 4096 Mar 14 13:10 tools
+ printenv
+ sort
 [[ ${rel} == plugins/*.jpi ]] && touch "/var/jenkins_home/${rel}.pinned";
 b="${f%.override}";
 cp -r "${f}" "/var/jenkins_home/${rel}";
 dir=$(dirname "${b}");
 echo " $f -> $rel" >> "/var/jenkins_home/copy_reference_file.log";
 echo "$f" >> "/var/jenkins_home/copy_reference_file.log";
 echo "copy $rel to JENKINS_HOME" >> "/var/jenkins_home/copy_reference_file.log";
 fi
 if [[ ! -e /var/jenkins_home/${rel} || $f = *.override ]]; then
 mkdir -p "/var/jenkins_home/${dir:23}";
 rel="${b:23}";
BASH_FUNC_copy_reference_file%%=() {  f="${1%/}";
BUILD_CAUSE=MANUALTRIGGER
BUILD_CAUSE_MANUALTRIGGER=true
BUILD_DISPLAY_NAME=#13
BUILD_ID=13
BUILD_NUMBER=13
BUILD_TAG=jenkins-build_android_udooneo-13
CA_CERTIFICATES_JAVA_VERSION=20140324
CLASSPATH=
COPY_REFERENCE_FILE_LOG=/var/jenkins_home/copy_reference_file.log
EXECUTOR_NUMBER=0
HOME=/var/jenkins_home
HOSTNAME=1dddbffd6d7e
HUDSON_HOME=/var/jenkins_home
HUDSON_SERVER_COOKIE=2c5d611b8254ec31
JAVA_DEBIAN_VERSION=8u72-b15-1~bpo8+1
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_VERSION=8u72
JENKINS_HOME=/var/jenkins_home
JENKINS_SERVER_COOKIE=2c5d611b8254ec31
JENKINS_SHA=e72e06e64d23eefb13090459f517b0697aad7be0
JENKINS_SLAVE_AGENT_PORT=50000
JENKINS_UC=https://updates.jenkins-ci.org
JENKINS_VERSION=1.642.2
JOB_NAME=build_android_udooneo
LANG=C.UTF-8
NODE_LABELS=master
NODE_NAME=master
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
PWD=/var/jenkins_home/jobs/build_android_udooneo/workspace
ROOT_BUILD_CAUSE=MANUALTRIGGER
ROOT_BUILD_CAUSE_MANUALTRIGGER=true
SHLVL=3
TERM=xterm
TINI_SHA=066ad710107dc7ee05d3aa6e4974f01dc98f3888
WORKSPACE=/var/jenkins_home/jobs/build_android_udooneo/workspace
_=/usr/bin/printenv
}
+ cat /etc/passwd
root:x:0:0:root:/root:/bin/bash
daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin
bin:x:2:2:bin:/bin:/usr/sbin/nologin
sys:x:3:3:sys:/dev:/usr/sbin/nologin
sync:x:4:65534:sync:/bin:/bin/sync
games:x:5:60:games:/usr/games:/usr/sbin/nologin
man:x:6:12:man:/var/cache/man:/usr/sbin/nologin
lp:x:7:7:lp:/var/spool/lpd:/usr/sbin/nologin
mail:x:8:8:mail:/var/mail:/usr/sbin/nologin
news:x:9:9:news:/var/spool/news:/usr/sbin/nologin
uucp:x:10:10:uucp:/var/spool/uucp:/usr/sbin/nologin
proxy:x:13:13:proxy:/bin:/usr/sbin/nologin
www-data:x:33:33:www-data:/var/www:/usr/sbin/nologin
backup:x:34:34:backup:/var/backups:/usr/sbin/nologin
list:x:38:38:Mailing List Manager:/var/list:/usr/sbin/nologin
irc:x:39:39:ircd:/var/run/ircd:/usr/sbin/nologin
gnats:x:41:41:Gnats Bug-Reporting System (admin):/var/lib/gnats:/usr/sbin/nologin
nobody:x:65534:65534:nobody:/nonexistent:/usr/sbin/nologin
systemd-timesync:x:100:102:systemd Time Synchronization,,,:/run/systemd:/bin/false
systemd-network:x:101:103:systemd Network Management,,,:/run/systemd/netif:/bin/false
systemd-resolve:x:102:104:systemd Resolver,,,:/run/systemd/resolve:/bin/false
systemd-bus-proxy:x:103:105:systemd Bus Proxy,,,:/run/systemd:/bin/false
_apt:x:104:65534::/nonexistent:/bin/false
messagebus:x:105:108::/var/run/dbus:/bin/false
jenkins:x:1000:1000::/home/jenkins:/bin/bash
build:x:30000:30000::/home/build:/bin/bash
+ git config --global user.name easy-jenkins
+ git config --global user.email jenkins@1dddbffd6d7e
+ git config --global color.ui auto
+ repo init -u https://github.com/UDOOboard/android_udoo_platform_manifest -b android-5.1.1

Your identity is: easy-jenkins <jenkins@1dddbffd6d7e>
If you want to change this, please re-run 'repo init' with --config-name

repo has been initialized in /var/jenkins_home/jobs/build_android_udooneo/workspace
+ repo sync --force-sync
Fetching project platform/prebuilts/maven_repo/androidFetching project platform/packages/wallpapers/MusicVisualization

Fetching project platform/external/chromium_org/third_party/libjingle/source/talk
Fetching project platform/prebuilts/gcc/darwin-x86/x86/x86_64-linux-android-4.9

Fetching projects:   0% (1/475)  Fetching project platform/external/chromium_org/third_party/libvpx
Fetching project platform/packages/wallpapers/HoloSpiral
Fetching project platform/packages/providers/MediaProvider
Fetching project platform/packages/apps/SpareParts

Fetching projects:   1% (5/475)  Fetching project android_system_vold
Fetching project platform/external/fdlibm
Fetching project platform/hardware/qcom/sensors
Fetching project android_hardware_libhardware
Fetching project platform/external/noto-fonts

Fetching projects:   2% (10/475)  Fetching project platform/external/jarjar
Fetching project platform/external/javassist
Fetching project platform/packages/apps/CellBroadcastReceiver
Fetching project platform/external/tcpdump
Fetching project platform/hardware/intel/img/hwcomposer

Fetching projects:   3% (15/475)  Fetching project platform/packages/apps/Phone
Fetching project platform/external/srec
Fetching project platform/external/okhttp
Fetching project platform/external/fonttools

Fetching projects:   4% (19/475)  Fetching project platform/packages/apps/Bluetooth
Fetching project platform/prebuilts/gcc/darwin-x86/x86/x86_64-linux-android-4.8
Fetching project platform/abi/cpp
Fetching project device/google/accessory/demokit
Fetching project platform/external/chromium_org/third_party/libaddressinput/src

Fetching projects:   5% (24/475)  Fetching project platform/external/freetype
Fetching project platform/packages/apps/Exchange
Fetching project platform/packages/apps/PackageInstaller
Fetching project platform/external/tinyxml
Fetching project platform/prebuilts/gcc/linux-x86/mips/mips64el-linux-android-4.8

Fetching projects:   6% (29/475)  Fetching project platform/prebuilts/gcc/darwin-x86/aarch64/aarch64-linux-android-4.8
Fetching project device/generic/common
Fetching project platform/external/google-fonts/cutive-mono
Fetching project platform/external/libutf
Fetching project platform/external/mockwebserver

Fetching projects:   7% (34/475)  Fetching project platform/external/mockito
Fetching project platform/hardware/qcom/keymaster
Fetching project platform/external/proguard
Fetching project platform/packages/apps/ContactsCommon

Fetching projects:   8% (38/475)  Fetching project android_hardware_broadcom_wlan
Fetching project platform/packages/screensavers/WebView
Fetching project platform/prebuilts/gcc/linux-x86/mips/mips64el-linux-android-4.9
Fetching project platform/external/chromium_org/third_party/leveldatabase/src
Fetching project platform/hardware/qcom/msm8960

Fetching projects:   9% (43/475)  Fetching project android_dalvik
Fetching project platform/hardware/akm
Fetching project platform/external/llvm
Fetching project android_frameworks_webview
Fetching project platform/external/harfbuzz_ng

Fetching projects:  10% (48/475)  Fetching project platform/frameworks/support
Fetching project android_frameworks_opt_telephony
Fetching project platform/external/chromium_org/third_party/libyuv
Fetching project platform/external/chromium-libpac
Fetching project platform/packages/apps/Provision

Fetching projects:  11% (53/475)  Fetching project platform/frameworks/compile/mclinker
Fetching project platform/external/mesa3d
Fetching project platform/external/chromium_org/third_party/libphonenumber/src/phonenumbers
Fetching project android_external_powerdebug

Fetching projects:  12% (57/475)  Fetching project platform/prebuilts/clang/linux-x86/mips/3.3
Fetching project platform/frameworks/opt/net/voip
Fetching project platform/external/yaffs2
Fetching project platform/frameworks/opt/chips
Fetching project platform/hardware/ti/omap4xxx

Fetching projects:  13% (62/475)  Fetching project platform/prebuilts/eclipse
Fetching project android_packages_apps_Launcher2
Fetching project platform/prebuilts/tools
Fetching project platform/external/libphonenumber
Fetching project platform/external/jemalloc

Fetching projects:  14% (67/475)  Fetching project platform/packages/providers/TelephonyProvider
Fetching project android_external_sonivox
Fetching project platform/external/hamcrest
Fetching project platform/hardware/qcom/msm8x27
Fetching project platform/external/oprofile

Fetching projects:  15% (72/475)  Fetching project platform/hardware/nvidia/audio
Fetching project platform/ndk
Fetching project platform/external/apache-harmony
Fetching project platform/prebuilts/gcc/linux-x86/x86/x86_64-linux-android-4.8

Fetching projects:  16% (76/475)  Fetching project platform/packages/apps/Terminal
Fetching project platform/prebuilts/gcc/darwin-x86/arm/arm-linux-androideabi-4.8
Fetching project platform/developers/build
Fetching project platform/external/checkpolicy
Fetching project platform/external/safe-iop

Fetching projects:  17% (81/475)  Fetching project platform/prebuilts/ndk
Fetching project platform/packages/apps/Tag
Fetching project platform/packages/apps/Contacts
Fetching project platform/prebuilts/qemu-kernel
Fetching project android_system_netd

Fetching projects:  18% (86/475)  Fetching project platform/packages/providers/CalendarProvider
Fetching project platform/external/eyes-free
Fetching project platform/system/media
Fetching project android_hardware_libhardware_legacy
Fetching project platform/external/sfntly

Fetching projects:  19% (91/475)  Fetching project platform/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8
Fetching project platform/packages/apps/CertInstaller
Fetching project platform/frameworks/opt/colorpicker
Fetching project android_hardware_ti_wlan

Fetching projects:  20% (95/475)  Fetching project platform/external/glide
Fetching project platform/packages/apps/QuickSearchBox
Fetching project platform/hardware/nvidia/tegra124
Fetching project platform/external/netcat
Fetching project platform/external/jhead

Fetching projects:  21% (100/475)  Fetching project platform/developers/docs
Fetching project android_frameworks_av
Fetching project android_packages_apps_Gallery2
Fetching project platform/prebuilts/gcc/darwin-x86/aarch64/aarch64-linux-android-4.9
Fetching project platform/external/bzip2

Fetching projects:  22% (105/475)  Fetching project platform/external/protobuf
Fetching project platform/developers/samples/android
Fetching project platform/external/emma
Fetching project platform/external/google-tv-pairing-protocol
Fetching project platform/external/nfacct

Fetching projects:  23% (110/475)  Fetching project platform/external/pcre
Fetching project device/common
Fetching project platform/external/objenesis
Fetching project android_external_mtd-utils

Fetching projects:  24% (114/475)  Fetching project android_external_linux-firmware-imx
Fetching project platform/packages/apps/Stk
Fetching project platform/packages/screensavers/PhotoTable
Fetching project platform/external/f2fs-tools
Fetching project platform/hardware/intel/common/bd_prov

Fetching projects:  25% (119/475)  Fetching project platform/system/keymaster
Fetching project platform/packages/inputmethods/OpenWnn
Fetching project platform/packages/apps/TvSettings
Fetching project platform/external/libogg
Fetching project platform/prebuilts/gcc/darwin-x86/mips/mipsel-linux-android-4.8

Fetching projects:  26% (124/475)  Fetching project platform/external/libopus
Fetching project platform/external/mp4parser
Fetching project platform/external/chromium_org/third_party/skia
Fetching project platform/packages/apps/PhoneCommon
Fetching project device/google/accessory/arduino

Fetching projects:  27% (129/475)  Fetching project android_external_libselinux
Fetching project platform/external/iproute2
Fetching project platform/packages/services/Mms
Fetching project platform/external/libseccomp-helper

Fetching projects:  28% (133/475)  Fetching project platform/external/zlib
Fetching project platform/prebuilts/clang/linux-x86/host/3.4
Fetching project platform/external/libvpx
Fetching project platform/external/javasqlite
Fetching project platform/external/google-fonts/carrois-gothic-sc

Fetching projects:  29% (138/475)  Fetching project platform/frameworks/minikin
Fetching project platform/packages/providers/TvProvider
Fetching project platform/external/xmlwriter
Fetching project android_packages_apps_Email
Fetching project platform/frameworks/opt/vcard

Fetching projects:  30% (143/475)  Fetching project platform/packages/apps/OneTimeInitializer
Fetching project android_external_dnsmasq
Fetching project platform/external/bsdiff
Fetching project platform/packages/providers/ApplicationsProvider
Fetching project platform/packages/apps/Mms

Fetching projects:  31% (148/475)  Fetching project platform/external/libhevc
Fetching project android_hardware_imx
Fetching project platform/packages/apps/SmartCardService
Fetching project platform/external/icu

Fetching projects:  32% (152/475)  Fetching project platform/external/chromium_org/tools/grit
Fetching project platform/external/tinycompress
Fetching project platform/external/chromium_org/third_party/yasm/source/patched-yasm
Fetching project android_external_libpng
Fetching project android_external_bluetooth_bluedroid

Fetching projects:  33% (157/475)  Fetching project platform/external/pdfium
Fetching project platform/frameworks/opt/timezonepicker
Fetching project platform/external/nist-sip
Fetching project platform/external/tagsoup
Fetching project android_external_fsl_vpu_omx

Fetching projects:  34% (162/475)  Fetching project platform/frameworks/opt/widget
Fetching project android_external_aac
Fetching project platform/frameworks/ex
Fetching project platform/frameworks/wilhelm
Fetching project platform/external/owasp/sanitizer

Fetching projects:  35% (167/475)  Fetching project android_udoo_platform_build
Fetching project platform/hardware/intel/common/utils
Fetching project platform/external/ant-glob
Fetching project android_frameworks_opt_net_ethernet

Fetching projects:  36% (171/475)  Fetching project platform/hardware/intel/common/omx-components
Fetching project platform/hardware/qcom/display
Fetching project platform/external/libgsm
Fetching project platform/frameworks/opt/net/ims
Fetching project platform/hardware/intel/common/libstagefrighthw

Fetching projects:  37% (176/475)  Fetching project platform/external/libvterm
Fetching project platform/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.8
Fetching project platform/external/android-clat
Fetching project platform/external/smali
Fetching project platform/prebuilts/clang/darwin-x86/x86/3.3

Fetching projects:  38% (181/475)  Fetching project platform/frameworks/opt/setupwizard
Fetching project platform/hardware/intel/common/libva
Fetching project platform/frameworks/ml
Fetching project platform/external/sepolicy
Fetching project platform/packages/apps/Launcher3

Fetching projects:  39% (186/475)  Fetching project platform/packages/wallpapers/MagicSmoke
Fetching project android_packages_apps_Gallery
Fetching project platform/external/jack
Fetching project platform/packages/apps/MusicFX

Fetching projects:  40% (190/475)  Fetching project platform/frameworks/multidex
Fetching project platform/hardware/intel/common/libmix
Fetching project platform/external/chromium_org/third_party/eyesfree/src/android/java/src/com/googlecode/eyesfree/braille
Fetching project platform/external/apache-xml
Fetching project platform/prebuilts/clang/darwin-x86/mips/3.3

Fetching projects:  41% (195/475)  Fetching project platform/external/sqlite
Fetching project platform/hardware/intel/img/psb_headers
Fetching project android_external_kernel-headers
Fetching project android_device_fsl-proprietary
Fetching project platform/developers/demos

Fetching projects:  42% (200/475)  Fetching project platform/external/lzma
Fetching project android_frameworks_native
Fetching project platform/prebuilts/sdk
Fetching project platform/packages/screensavers/Basic
Fetching project platform/external/nanopb-c

Fetching projects:  43% (205/475)  Fetching project platform/external/giflib
Fetching project platform/packages/apps/Calculator
Fetching project platform/external/lldb
Fetching project platform/external/jsr305

Fetching projects:  44% (209/475)  Fetching project platform/packages/wallpapers/Galaxy4
Fetching project platform/prebuilts/clang/linux-x86/3.1
Fetching project platform/external/libcap-ng
Fetching project android_external_linux-lib
Fetching project platform/external/gtest

Fetching projects:  45% (214/475)  Fetching project platform/external/chromium_org/third_party/mesa/src
Fetching project platform/external/chromium-trace
Fetching project platform/external/bouncycastle
Fetching project platform/frameworks/opt/net/wifi
Fetching project platform/prebuilts/clang/linux-x86/3.2

Fetching projects:  46% (219/475)  Fetching project platform/external/ltrace
Fetching project platform/prebuilts/python/darwin-x86/2.7.5
Fetching project platform/external/apache-qp
Fetching project platform/packages/apps/HTMLViewer
Fetching project android_system_core

Fetching projects:  47% (224/475)  Fetching project platform/prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6
Fetching project platform/external/chromium_org/third_party/webrtc
Fetching project platform/external/genext2fs
Fetching project platform/packages/apps/BasicSmsReceiver

Fetching projects:  48% (228/475)  Fetching project platform/packages/apps/Music
Fetching project android_hardware_broadcom_libbt
Fetching project platform/packages/wallpapers/NoiseField
Fetching project android_external_tremolo
Fetching project android_external_busybox

Fetching projects:  49% (233/475)  Fetching project platform/external/dexmaker
Fetching project platform/hardware/qcom/msm8x84
Fetching project android_packages_apps_Browser
Fetching project platform/frameworks/volley
Fetching project platform/prebuilts/devtools

Fetching projects:  50% (238/475)  Fetching project platform/hardware/ti/omap3
Fetching project platform/external/google-fonts/dancing-script
Fetching project platform/external/netperf
Fetching project platform/frameworks/opt/inputmethodcommon
Fetching project platform/tools/external/fat32lib

Fetching projects:  51% (243/475)  Fetching project android_packages_apps_Camera2
Fetching project platform/frameworks/compile/libbcc
Fetching project platform/frameworks/opt/bitmap
Fetching project platform/external/iptables

Fetching projects:  52% (247/475)  Fetching project platform/hardware/qcom/msm8x26
Fetching project android_external_skia
Fetching project platform/external/opencv
Fetching project platform/external/pixman
Fetching project platform/external/xmp_toolkit

Fetching projects:  53% (252/475)  Fetching project platform/external/robolectric
Fetching project platform/external/jsilver
Fetching project platform/frameworks/opt/bluetooth
Fetching project platform/external/libcxxabi
Fetching project platform/external/ceres-solver

Fetching projects:  54% (257/475)  Fetching project platform/external/regex-re2
Fetching project platform/external/clang
Fetching project platform/hardware/qcom/audio
Fetching project platform/external/chromium_org/third_party/WebKit
Fetching project platform/packages/services/Telephony

Fetching projects:  55% (262/475)  Fetching project platform/external/libvorbis
Fetching project platform/external/jsmn
Fetching project platform/external/droiddriver
Fetching project platform/hardware/intel/audio_media

Fetching projects:  56% (266/475)  Fetching project platform/external/apache-http
Fetching project platform/prebuilts/gcc/darwin-x86/mips/mips64el-linux-android-4.8
Fetching project android_system_extras
Fetching project android_hardware_qcom_wlan
Fetching project platform/external/libmtp

Fetching projects:  57% (271/475)  Fetching project platform/external/openfst
Fetching project platform/external/replicaisland
Fetching project platform/packages/providers/ContactsProvider
Fetching project platform/frameworks/compile/slang
Fetching project platform/external/valgrind

Fetching projects:  58% (276/475)  Fetching project platform/hardware/samsung_slsi/exynos5
Fetching project platform/external/webrtc
Fetching project platform/hardware/intel/img/libdrm
Fetching project platform/packages/apps/Nfc
Fetching project platform/external/nanohttpd

Fetching projects:  59% (281/475)  Fetching project platform/prebuilts/gcc/darwin-x86/mips/mips64el-linux-android-4.9
Fetching project platform/packages/apps/Dialer
Fetching project platform/hardware/intel/common/wrs_omxil_core
Fetching project platform/external/libyuv

Fetching projects:  60% (285/475)  Fetching project platform/external/conscrypt
Fetching project platform/external/chromium_org/third_party/icu
Fetching project uboot-imx
Fetching project platform/packages/apps/UnifiedEmail
Fetching project platform/packages/inputmethods/LatinIME

Fetching projects:  61% (290/475)  Fetching project android_hardware_realtek
Fetching project platform/external/naver-fonts
Fetching project platform/prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.8
Fetching project android_hardware_ril
Fetching project platform/packages/providers/DownloadProvider

Fetching projects:  62% (295/475)  Fetching project platform/prebuilts/clang/darwin-x86/host/3.4
Fetching project platform/packages/apps/ManagedProvisioning
Fetching project platform/packages/apps/Camera
Fetching project platform/external/chromium_org/v8
Fetching project platform/external/eigen

Fetching projects:  63% (300/475)  Fetching project platform/frameworks/opt/calendar
Fetching project platform/frameworks/opt/emoji
Fetching project platform/external/libcxx
Fetching project platform/external/libnfc-nxp

Fetching projects:  64% (304/475)  Fetching project platform/prebuilts/gcc/linux-x86/mips/mipsel-linux-android-4.8
Fetching project platform/external/libsepol
Fetching project android_packages_apps_fsl_imx_demo
Fetching project platform/external/expat
Fetching project platform/external/chromium_org

Fetching projects:  65% (309/475)  Fetching project platform/hardware/intel/common/libwsbm
Fetching project platform/prebuilts/python/linux-x86/2.7.5
Fetching project platform/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8
Fetching project platform/external/mtpd
Fetching project platform/external/libunwind

Fetching projects:  66% (314/475)  Fetching project android_hardware_ti_wpan
Fetching project device/generic/armv7-a-neon
Fetching project platform/prebuilts/clang/darwin-x86/arm/3.3
Fetching project platform/external/srtp
Fetching project android_external_ppp

Fetching projects:  67% (319/475)  Fetching project platform/external/openssh
Fetching project platform/packages/apps/DeskClock
Fetching project platform/prebuilts/gcc/darwin-x86/arm/arm-eabi-4.8
Fetching project android_packages_apps_LegacyCamera

Fetching projects:  68% (323/475)  Fetching project platform/external/chromium_org/third_party/sfntly/cpp/src
Fetching project platform/external/arduino
Fetching project platform/external/libexif
Fetching project platform/external/dhcpcd
Fetching project platform/frameworks/opt/photoviewer

Fetching projects:  69% (328/475)  Fetching project android_external_wpa_supplicant_8
Fetching project platform/external/cmockery
Fetching project platform/packages/experimental
Fetching project platform/hardware/intel/bootstub
Fetching project platform/external/chromium_org/testing/gtest

Fetching projects:  70% (333/475)  Fetching project platform/external/ksoap2
Fetching project platform/external/stlport
Fetching project platform/frameworks/mff
Fetching project platform/prebuilts/clang/darwin-x86/3.2
Fetching project platform/external/google-fonts/coming-soon

Fetching projects:  71% (338/475)  Fetching project platform/external/guava
Fetching project platform/sdk
Fetching project platform/external/liblzf
Fetching project android_external_linux-tools-perf

Fetching projects:  72% (342/475)  Fetching project platform/external/mdnsresponder
Fetching project platform/frameworks/opt/carddav
Fetching project platform/packages/providers/UserDictionaryProvider
Fetching project platform/packages/providers/PartnerBookmarksProvider
Fetching project platform/external/lohit-fonts

Fetching projects:  73% (347/475)  Fetching project platform/external/zxing
Fetching project platform/external/chromium_org/third_party/usrsctp/usrsctplib
Fetching project platform/external/webp
Fetching project android_bionic
Fetching project android_device_fsl

Fetching projects:  74% (352/475)  Fetching project platform/external/stressapptest
Fetching project platform/external/deqp
Fetching project platform/external/doclava
Fetching project platform/hardware/invensense
Fetching project platform/packages/wallpapers/LivePicker

Fetching projects:  75% (357/475)  Fetching project platform/external/chromium_org/third_party/openmax_dl
Fetching project platform/external/libusb-compat
Fetching project platform/external/chromium_org/third_party/libphonenumber/src/resources
Fetching project platform/packages/apps/InCallUI

Fetching projects:  76% (361/475)  Fetching project platform/external/smack
Fetching project platform/external/chromium_org/third_party/libjpeg_turbo
Fetching project platform/prebuilts/clang/darwin-x86/host/3.5
Fetching project platform/external/oauth
Fetching project platform/external/junit

Fetching projects:  77% (366/475)  Fetching project platform/external/jpeg
Fetching project platform/development
Fetching project platform/external/easymock
Fetching project android_device_udoo
Fetching project android_packages_apps_Settings

Fetching projects:  78% (371/475)  Fetching project platform/frameworks/opt/datetimepicker
Fetching project platform/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6
Fetching project platform/prebuilts/clang/darwin-x86/3.1
Fetching project platform/external/nist-pkits
Fetching project platform/hardware/ti/omap4-aah

Fetching projects:  79% (376/475)  Fetching project platform/external/gcc-demangle
Fetching project platform/external/messageformat
Fetching project platform/external/chromium_org/sdch/open-vcdiff
Fetching project platform/external/markdown

Fetching projects:  80% (380/475)  Fetching project platform/external/libpcap
Fetching project platform/external/chromium_org/third_party/boringssl/src
Fetching project android_frameworks_base
Fetching project platform/packages/apps/FMRadio
Fetching project platform/external/svox

Fetching projects:  81% (385/475)  Fetching project platform/external/libnl
Fetching project platform/external/fio
Fetching project platform/system/security
Fetching project platform/external/strace
Fetching project platform/external/jsoncpp

Fetching projects:  82% (390/475)  Fetching project platform/external/google-diff-match-patch
Fetching project platform/external/libssh2
Fetching project platform/libnativehelper
Fetching project platform/external/ipsec-tools
Fetching project android_external_tinyalsa

Fetching projects:  83% (395/475)  Fetching project platform/external/vixl
Fetching project platform/external/scrypt
Fetching project platform/external/chromium_org/third_party/freetype
Fetching project platform/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9

Fetching projects:  84% (399/475)  Fetching project device/google/atv
Fetching project linux_kernel
Fetching project platform/external/bison
Fetching project platform/frameworks/opt/mms
Fetching project platform/prebuilts/gradle-plugin

Fetching projects:  85% (404/475)  Fetching project platform/external/neven
Fetching project android_external_tools
Fetching project platform/external/blktrace
Fetching project platform/packages/wallpapers/PhaseBeam
Fetching project platform/pdk

Fetching projects:  86% (409/475)  Fetching project platform/external/qemu
Fetching project platform/external/openssl
Fetching project platform/external/libedit
Fetching project device/generic/x86_64
Fetching project platform/external/elfutils

Fetching projects:  87% (414/475)  Fetching project platform/external/libnfc-nci
Fetching project android_packages_apps_SoundRecorder
Fetching project platform/external/timezonepicker-support
Fetching project platform/external/fsck_msdos

Fetching projects:  88% (418/475)  Fetching project platform/external/littlemock
Fetching project platform/prebuilts/misc
Fetching project platform/external/compiler-rt
Fetching project platform/hardware/intel/img/psb_video
Fetching project platform/external/tinyxml2

Fetching projects:  89% (423/475)  Fetching project platform/packages/apps/Protips
Fetching project platform/hardware/qcom/msm8x74
Fetching project platform/external/mksh
Fetching project platform/external/chromium_org/third_party/libsrtp
Fetching project platform/prebuilts/gcc/darwin-x86/host/headers

Fetching projects:  90% (428/475)  Fetching project android_external_linux-test
Fetching project android_libcore
Fetching project platform/external/chromium_org/third_party/opus/src
Fetching project platform/external/chromium_org/third_party/ots
Fetching project platform/packages/apps/KeyChain

Fetching projects:  91% (433/475)  Fetching project platform/packages/apps/Calendar
Fetching project platform/tools/external/gradle
Fetching project platform/frameworks/rs
Fetching project platform/prebuilts/gcc/linux-x86/host/x86_64-w64-mingw32-4.8

Fetching projects:  92% (437/475)  Fetching project platform/external/jdiff
Fetching project platform/prebuilts/android-emulator
Fetching project platform/external/e2fsprogs
Fetching project platform/external/chromium_org/third_party/angle
Fetching project platform/prebuilts/libs/libedit

Fetching projects:  93% (442/475)  Fetching project platform/packages/wallpapers/Basic
Fetching project platform/hardware/qcom/gps
Fetching project platform/external/chromium_org/tools/gyp
Fetching project platform/prebuilts/clang/linux-x86/host/3.5
Fetching project platform/cts

Fetching projects:  94% (447/475)  Fetching project platform/art
Fetching project platform/external/zopfli
Fetching project android_bootable_recovery
Fetching project platform/external/antlr
Fetching project platform/external/jmdns

Fetching projects:  95% (452/475)  Fetching project platform/packages/services/Telecomm
Fetching project platform/bootable/bootloader/legacy
Fetching project platform/external/libusb
Fetching project platform/external/iputils

Fetching projects:  96% (456/475)  Fetching project platform/prebuilts/gcc/linux-x86/x86/x86_64-linux-android-4.9
Fetching project platform/packages/apps/SpeechRecorder
Fetching project platform/external/chromium_org/third_party/brotli/src
Fetching project platform/prebuilts/clang/linux-x86/x86/3.3
Fetching project platform/packages/apps/VoiceDialer
remote: Counting objects: 1           
remote: Counting objects: 5882, done        
remote: Finding sources:  12% (1/8)           
remote: Finding sources:  25% (2/8)           
remote: Finding sources:  37% (3/8)           
remote: Finding sources:  50% (4/8)           
remote: Finding sources:  62% (5/8)           
remote: Finding sources:  75% (6/8)           
remote: Finding sources:  87% (7/8)           
remote: Finding sources: 100% (8/8)           
remote: Finding sources: 100% (8/8)        
remote: Total 8 (delta 5), reused 8 (delta 5)        
From https://android.googlesource.com/platform/art
   4d20c08..41bcd81  master     -> aosp/master

Fetching projects:  97% (461/475)  Fetching project platform/prebuilts/clang/linux-x86/arm/3.3
Fetching project platform/external/libxml2
Fetching project platform/external/flac
Fetching project platform/external/esd
Fetching project platform/external/qemu-pc-bios

Fetching projects:  98% (466/475)  Fetching project platform/hardware/qcom/bt
Fetching project platform/hardware/qcom/power
Fetching project platform/external/speex
Fetching project platform/hardware/qcom/media
Fetching project platform/external/chromium_org/third_party/smhasher/src

Fetching projects:  99% (471/475)  Fetching project platform/prebuilts/gcc/darwin-x86/host/i686-apple-darwin-4.2.1

Fetching projects: 100% (475/475)  
Fetching projects: 100% (475/475), done.  

Syncing work tree:   1% (9/475)  
Syncing work tree:   2% (10/475)  
Syncing work tree:   3% (15/475)  
Syncing work tree:   4% (19/475)  
Syncing work tree:   5% (24/475)  
Syncing work tree:   6% (29/475)  
Syncing work tree:   7% (34/475)  
Syncing work tree:   8% (38/475)  
Syncing work tree:   9% (43/475)  
Syncing work tree:  10% (48/475)  
Syncing work tree:  11% (53/475)  
Syncing work tree:  12% (57/475)  
Syncing work tree:  13% (62/475)  
Syncing work tree:  14% (67/475)  
Syncing work tree:  15% (72/475)  
Syncing work tree:  16% (76/475)  
Syncing work tree:  17% (81/475)  
Syncing work tree:  18% (86/475)  
Syncing work tree:  19% (91/475)  
Syncing work tree:  20% (95/475)  
Syncing work tree:  21% (100/475)  
Syncing work tree:  22% (105/475)  
Syncing work tree:  23% (110/475)  
Syncing work tree:  24% (114/475)  
Syncing work tree:  25% (119/475)  
Syncing work tree:  26% (124/475)  
Syncing work tree:  27% (129/475)  
Syncing work tree:  28% (133/475)  
Syncing work tree:  29% (138/475)  
Syncing work tree:  30% (143/475)  
Syncing work tree:  31% (148/475)  
Syncing work tree:  32% (152/475)  
Syncing work tree:  33% (157/475)  
Syncing work tree:  34% (162/475)  
Syncing work tree:  35% (167/475)  
Syncing work tree:  36% (171/475)  
Syncing work tree:  37% (176/475)  
Syncing work tree:  38% (181/475)  
Syncing work tree:  39% (186/475)  
Syncing work tree:  40% (190/475)  
Syncing work tree:  41% (195/475)  
Syncing work tree:  42% (200/475)  
Syncing work tree:  43% (205/475)  
Syncing work tree:  44% (209/475)  
Syncing work tree:  45% (214/475)  
Syncing work tree:  46% (219/475)  
Syncing work tree:  47% (224/475)  
Syncing work tree:  48% (228/475)  
Syncing work tree:  49% (233/475)  
Syncing work tree:  50% (238/475)  
Syncing work tree:  51% (243/475)  
Syncing work tree:  52% (247/475)  
Syncing work tree:  53% (252/475)  
Syncing work tree:  54% (257/475)  
Syncing work tree:  55% (262/475)  
Syncing work tree:  56% (266/475)  
Syncing work tree:  57% (271/475)  
Syncing work tree:  58% (276/475)  
Syncing work tree:  59% (281/475)  
Syncing work tree:  60% (285/475)  
Syncing work tree:  61% (290/475)  
Syncing work tree:  62% (295/475)  
Syncing work tree:  63% (300/475)  
Syncing work tree:  64% (304/475)  
Syncing work tree:  65% (309/475)  
Syncing work tree:  66% (314/475)  
Syncing work tree:  67% (319/475)  
Syncing work tree:  68% (323/475)  
Syncing work tree:  69% (328/475)  
Syncing work tree:  70% (333/475)  
Syncing work tree:  71% (338/475)  
Syncing work tree:  72% (342/475)  
Syncing work tree:  73% (347/475)  
Syncing work tree:  74% (352/475)  
Syncing work tree:  75% (357/475)  
Syncing work tree:  76% (361/475)  
Syncing work tree:  77% (366/475)  
Syncing work tree:  78% (371/475)  
Syncing work tree:  79% (376/475)  
Syncing work tree:  80% (380/475)  
Syncing work tree:  81% (385/475)  
Syncing work tree:  82% (390/475)  
Syncing work tree:  83% (395/475)  
Syncing work tree:  84% (399/475)  
Syncing work tree:  85% (404/475)  
Syncing work tree:  86% (409/475)  
Syncing work tree:  87% (414/475)  
Syncing work tree:  88% (418/475)  
Syncing work tree:  89% (423/475)  
Syncing work tree:  90% (428/475)  
Syncing work tree:  91% (433/475)  
Syncing work tree:  92% (437/475)  
Syncing work tree:  93% (442/475)  
Syncing work tree:  94% (447/475)  
Syncing work tree:  95% (452/475)  
Syncing work tree:  96% (456/475)  
Syncing work tree:  97% (461/475)  
Syncing work tree:  98% (466/475)  
Syncing work tree:  99% (471/475)  
Syncing work tree: 100% (475/475)  
Syncing work tree: 100% (475/475), done.  

+ source build/envsetup.sh
++ VARIANT_CHOICES=(user userdebug eng)
++ unset LUNCH_MENU_CHOICES
++ add_lunch_combo aosp_arm-eng
++ local new_combo=aosp_arm-eng
++ local c
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ add_lunch_combo aosp_arm64-eng
++ local new_combo=aosp_arm64-eng
++ local c
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_arm64-eng = aosp_arm-eng ']'
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ add_lunch_combo aosp_mips-eng
++ local new_combo=aosp_mips-eng
++ local c
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_mips-eng = aosp_arm-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_mips-eng = aosp_arm64-eng ']'
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ add_lunch_combo aosp_mips64-eng
++ local new_combo=aosp_mips64-eng
++ local c
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_mips64-eng = aosp_arm-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_mips64-eng = aosp_arm64-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_mips64-eng = aosp_mips-eng ']'
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ add_lunch_combo aosp_x86-eng
++ local new_combo=aosp_x86-eng
++ local c
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86-eng = aosp_arm-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86-eng = aosp_arm64-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86-eng = aosp_mips-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86-eng = aosp_mips64-eng ']'
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ add_lunch_combo aosp_x86_64-eng
++ local new_combo=aosp_x86_64-eng
++ local c
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86_64-eng = aosp_arm-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86_64-eng = aosp_arm64-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86_64-eng = aosp_mips-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86_64-eng = aosp_mips64-eng ']'
++ for c in '${LUNCH_MENU_CHOICES[@]}'
++ '[' aosp_x86_64-eng = aosp_x86-eng ']'
++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ complete -F _lunch lunch
++ case `uname -s` in
+++ uname -s
++ case `uname -s` in
+++ uname -s
++ '[' x/bin/bash '!=' x/bin/bash ']'
+++ test -d device
+++ find -L device -maxdepth 4 -name vendorsetup.sh
+++ test -d vendor
++ for f in '`test -d device && find -L device -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`' '`test -d vendor && find -L vendor -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`'
++ echo 'including device/fsl/imx6/vendorsetup.sh'
including device/fsl/imx6/vendorsetup.sh
++ . device/fsl/imx6/vendorsetup.sh
+++ add_lunch_combo sabresd_6dq-eng
+++ local new_combo=sabresd_6dq-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-eng = aosp_x86_64-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabresd_6dq-user
+++ local new_combo=sabresd_6dq-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6dq-user = sabresd_6dq-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabreauto_6q-eng
+++ local new_combo=sabreauto_6q-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-eng = sabresd_6dq-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabreauto_6q-user
+++ local new_combo=sabreauto_6q-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6q-user = sabreauto_6q-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo evk_6sl-eng
+++ local new_combo=evk_6sl-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-eng = sabreauto_6q-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo evk_6sl-user
+++ local new_combo=evk_6sl-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' evk_6sl-user = evk_6sl-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabresd_6sx-eng
+++ local new_combo=sabresd_6sx-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-eng = evk_6sl-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabresd_6sx-user
+++ local new_combo=sabresd_6sx-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_6sx-user = sabresd_6sx-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabreauto_6sx-eng
+++ local new_combo=sabreauto_6sx-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-eng = sabresd_6sx-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabreauto_6sx-user
+++ local new_combo=sabreauto_6sx-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabresd_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabreauto_6sx-user = sabreauto_6sx-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ for f in '`test -d device && find -L device -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`' '`test -d vendor && find -L vendor -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`'
++ echo 'including device/fsl/imx7/vendorsetup.sh'
including device/fsl/imx7/vendorsetup.sh
++ . device/fsl/imx7/vendorsetup.sh
+++ add_lunch_combo sabresd_7d-eng
+++ local new_combo=sabresd_7d-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabresd_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabreauto_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-eng = sabreauto_6sx-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo sabresd_7d-user
+++ local new_combo=sabresd_7d-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabresd_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabreauto_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabreauto_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' sabresd_7d-user = sabresd_7d-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ for f in '`test -d device && find -L device -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`' '`test -d vendor && find -L vendor -maxdepth 4 -name '\''vendorsetup.sh'\'' 2> /dev/null`'
++ echo 'including device/udoo/imx6/vendorsetup.sh'
including device/udoo/imx6/vendorsetup.sh
++ . device/udoo/imx6/vendorsetup.sh
+++ add_lunch_combo udooneo_6sx-eng
+++ local new_combo=udooneo_6sx-eng
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabreauto_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabreauto_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_7d-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-eng = sabresd_7d-user ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
+++ add_lunch_combo udooneo_6sx-user
+++ local new_combo=udooneo_6sx-user
+++ local c
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_arm-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_arm64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_mips-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_mips64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_x86-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = aosp_x86_64-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_6dq-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_6dq-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabreauto_6q-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabreauto_6q-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = evk_6sl-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = evk_6sl-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabreauto_6sx-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabreauto_6sx-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_7d-eng ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = sabresd_7d-user ']'
+++ for c in '${LUNCH_MENU_CHOICES[@]}'
+++ '[' udooneo_6sx-user = udooneo_6sx-eng ']'
+++ LUNCH_MENU_CHOICES=(${LUNCH_MENU_CHOICES[@]} $new_combo)
++ unset f
++ addcompletions
++ local T dir f
++ '[' -z '4.3.42(1)-release' ']'
++ '[' 4 -lt 3 ']'
++ dir=sdk/bash_completion
++ '[' -d sdk/bash_completion ']'
+++ /bin/ls sdk/bash_completion/adb.bash
++ for f in '`/bin/ls ${dir}/[a-z]*.bash 2> /dev/null`'
++ echo 'including sdk/bash_completion/adb.bash'
including sdk/bash_completion/adb.bash
++ . sdk/bash_completion/adb.bash
++++ type -t compopt
+++ [[ builtin = \b\u\i\l\t\i\n ]]
+++ complete -F _adb adb
+ lunch udooneo_6sx-eng
+ local answer
+ '[' udooneo_6sx-eng ']'
+ answer=udooneo_6sx-eng
+ local selection=
+ '[' -z udooneo_6sx-eng ']'
+ echo -n udooneo_6sx-eng
+ grep -q -e '^[0-9][0-9]*$'
+ echo -n udooneo_6sx-eng
+ grep -q -e '^[^\-][^\-]*-[^\-][^\-]*$'
+ selection=udooneo_6sx-eng
+ '[' -z udooneo_6sx-eng ']'
+ export TARGET_BUILD_APPS=
+ TARGET_BUILD_APPS=
++ echo -n udooneo_6sx-eng
++ sed -e 's/-.*$//'
+ local product=udooneo_6sx
+ check_product udooneo_6sx
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
+ TARGET_PRODUCT=udooneo_6sx
+ TARGET_BUILD_VARIANT=
+ TARGET_BUILD_TYPE=
+ TARGET_BUILD_APPS=
+ get_build_var TARGET_DEVICE
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
+ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
+ CALLED_FROM_SETUP=true
+ BUILD_SYSTEM=build/core
+ command make --no-print-directory -f build/core/config.mk dumpvar-TARGET_DEVICE
+ make --no-print-directory -f build/core/config.mk dumpvar-TARGET_DEVICE
+ '[' 0 -ne 0 ']'
++ echo -n udooneo_6sx-eng
++ sed -e 's/^[^\-]*-//'
+ local variant=eng
+ check_variant eng
+ for v in '${VARIANT_CHOICES[@]}'
+ '[' user = eng ']'
+ for v in '${VARIANT_CHOICES[@]}'
+ '[' userdebug = eng ']'
+ for v in '${VARIANT_CHOICES[@]}'
+ '[' eng = eng ']'
+ return 0
+ '[' 0 -ne 0 ']'
+ '[' -z udooneo_6sx -o -z eng ']'
+ export TARGET_PRODUCT=udooneo_6sx
+ TARGET_PRODUCT=udooneo_6sx
+ export TARGET_BUILD_VARIANT=eng
+ TARGET_BUILD_VARIANT=eng
+ export TARGET_BUILD_TYPE=release
+ TARGET_BUILD_TYPE=release
+ echo

+ set_stuff_for_environment
+ settitle
+ '[' '' = '' ']'
++ gettargetarch
++ get_build_var TARGET_ARCH
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-TARGET_ARCH
++ make --no-print-directory -f build/core/config.mk dumpvar-TARGET_ARCH
+ local arch=arm
+ local product=udooneo_6sx
+ local variant=eng
+ local apps=
+ '[' -z '' ']'
+ export 'PROMPT_COMMAND=echo -ne "\033]0;[arm-udooneo_6sx-eng] @1dddbffd6d7e: /var/jenkins_home/jobs/build_android_udooneo/workspace\007"'
+ PROMPT_COMMAND='echo -ne "\033]0;[arm-udooneo_6sx-eng] @1dddbffd6d7e: /var/jenkins_home/jobs/build_android_udooneo/workspace\007"'
+ set_java_home
+ '[' -n '' ']'
+ '[' '!' /usr/lib/jvm/java-8-openjdk-amd64 ']'
+ setpaths
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
+ '[' -n '' ']'
+ '[' -n '' ']'
++ getprebuilt
++ get_abs_build_var ANDROID_PREBUILTS
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-abs-ANDROID_PREBUILTS
++ make --no-print-directory -f build/core/config.mk dumpvar-abs-ANDROID_PREBUILTS
+ prebuiltdir=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilt/linux-x86
++ get_abs_build_var ANDROID_GCC_PREBUILTS
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-abs-ANDROID_GCC_PREBUILTS
++ make --no-print-directory -f build/core/config.mk dumpvar-abs-ANDROID_GCC_PREBUILTS
+ gccprebuiltdir=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86
++ get_build_var TARGET_GCC_VERSION
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-TARGET_GCC_VERSION
++ make --no-print-directory -f build/core/config.mk dumpvar-TARGET_GCC_VERSION
+ targetgccversion=4.8
++ get_build_var 2ND_TARGET_GCC_VERSION
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-2ND_TARGET_GCC_VERSION
++ make --no-print-directory -f build/core/config.mk dumpvar-2ND_TARGET_GCC_VERSION
+ targetgccversion2=
+ export TARGET_GCC_VERSION=4.8
+ TARGET_GCC_VERSION=4.8
+ export ANDROID_TOOLCHAIN=
+ ANDROID_TOOLCHAIN=
+ export ANDROID_TOOLCHAIN_2ND_ARCH=
+ ANDROID_TOOLCHAIN_2ND_ARCH=
++ get_build_var TARGET_ARCH
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-TARGET_ARCH
++ make --no-print-directory -f build/core/config.mk dumpvar-TARGET_ARCH
+ local ARCH=arm
+ case $ARCH in
+ toolchaindir=arm/arm-linux-androideabi-4.8/bin
+ '[' -d /var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin ']'
+ export ANDROID_TOOLCHAIN=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin
+ ANDROID_TOOLCHAIN=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin
+ '[' -d /var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/ ']'
+ export ANDROID_TOOLCHAIN_2ND_ARCH=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/
+ ANDROID_TOOLCHAIN_2ND_ARCH=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/
+ unset ANDROID_KERNEL_TOOLCHAIN_PATH
+ case $ARCH in
+ toolchaindir=arm/arm-eabi-4.8/bin
+ '[' -d /var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin ']'
+ export ARM_EABI_TOOLCHAIN=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin
+ ARM_EABI_TOOLCHAIN=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin
+ ANDROID_KERNEL_TOOLCHAIN_PATH=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:
+ export ANDROID_DEV_SCRIPTS=/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools
+ ANDROID_DEV_SCRIPTS=/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools
++ get_build_var ANDROID_BUILD_PATHS
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-ANDROID_BUILD_PATHS
++ make --no-print-directory -f build/core/config.mk dumpvar-ANDROID_BUILD_PATHS
+ export ANDROID_BUILD_PATHS=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:
+ ANDROID_BUILD_PATHS=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:
+ case $(uname -s) in
++ uname -s
+ ANDROID_EMULATOR_PREBUILTS=/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64
+ '[' -n /var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64 -a -d /var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64 ']'
+ ANDROID_BUILD_PATHS=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64:
+ export ANDROID_EMULATOR_PREBUILTS
+ export PATH=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+ PATH=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+ unset ANDROID_JAVA_TOOLCHAIN
+ unset ANDROID_PRE_BUILD_PATHS
+ '[' -n /usr/lib/jvm/java-8-openjdk-amd64 ']'
+ export ANDROID_JAVA_TOOLCHAIN=/usr/lib/jvm/java-8-openjdk-amd64/bin
+ ANDROID_JAVA_TOOLCHAIN=/usr/lib/jvm/java-8-openjdk-amd64/bin
+ export ANDROID_PRE_BUILD_PATHS=/usr/lib/jvm/java-8-openjdk-amd64/bin:
+ ANDROID_PRE_BUILD_PATHS=/usr/lib/jvm/java-8-openjdk-amd64/bin:
+ export PATH=/usr/lib/jvm/java-8-openjdk-amd64/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+ PATH=/usr/lib/jvm/java-8-openjdk-amd64/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:/var/jenkins_home/jobs/build_android_udooneo/workspace/development/scripts:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/devtools/tools:/var/jenkins_home/jobs/build_android_udooneo/workspace/prebuilts/android-emulator/linux-x86_64:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
+ unset ANDROID_PRODUCT_OUT
++ get_abs_build_var PRODUCT_OUT
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-abs-PRODUCT_OUT
++ make --no-print-directory -f build/core/config.mk dumpvar-abs-PRODUCT_OUT
+ export ANDROID_PRODUCT_OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx
+ ANDROID_PRODUCT_OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx
+ export OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx
+ OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/target/product/udooneo_6sx
+ unset ANDROID_HOST_OUT
++ get_abs_build_var HOST_OUT
+++ gettop
+++ local TOPFILE=build/core/envsetup.mk
+++ '[' -n '' -a -f /build/core/envsetup.mk ']'
+++ '[' -f build/core/envsetup.mk ']'
+++ PWD=
+++ /bin/pwd
++ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
++ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
++ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
++ CALLED_FROM_SETUP=true
++ BUILD_SYSTEM=build/core
++ command make --no-print-directory -f build/core/config.mk dumpvar-abs-HOST_OUT
++ make --no-print-directory -f build/core/config.mk dumpvar-abs-HOST_OUT
+ export ANDROID_HOST_OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86
+ ANDROID_HOST_OUT=/var/jenkins_home/jobs/build_android_udooneo/workspace/out/host/linux-x86
+ set_sequence_number
+ export BUILD_ENV_SEQUENCE_NUMBER=10
+ BUILD_ENV_SEQUENCE_NUMBER=10
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ export ANDROID_BUILD_TOP=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ ANDROID_BUILD_TOP=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ export 'GCC_COLORS=error=01;31:warning=01;35:note=01;36:caret=01;32:locus=01:quote=01'
+ GCC_COLORS='error=01;31:warning=01;35:note=01;36:caret=01;32:locus=01:quote=01'
+ printconfig
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
+ get_build_var report_config
++ gettop
++ local TOPFILE=build/core/envsetup.mk
++ '[' -n '' -a -f /build/core/envsetup.mk ']'
++ '[' -f build/core/envsetup.mk ']'
++ PWD=
++ /bin/pwd
+ T=/var/jenkins_home/jobs/build_android_udooneo/workspace
+ '[' '!' /var/jenkins_home/jobs/build_android_udooneo/workspace ']'
+ cd /var/jenkins_home/jobs/build_android_udooneo/workspace
+ CALLED_FROM_SETUP=true
+ BUILD_SYSTEM=build/core
+ command make --no-print-directory -f build/core/config.mk dumpvar-report_config
+ make --no-print-directory -f build/core/config.mk dumpvar-report_config
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

+ make -j 8
++ date +%s
+ local start_time=1458121535
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
including ./art/Android.mk ...
including ./bionic/Android.mk ...
including ./bootable/recovery/Android.mk ...
including ./build/libs/host/Android.mk ...
including ./build/target/board/Android.mk ...
including ./build/target/product/security/Android.mk ...
including ./build/tools/Android.mk ...
including ./cts/Android.mk ...
including ./dalvik/Android.mk ...
including ./development/apps/BluetoothDebug/Android.mk ...
including ./development/apps/BuildWidget/Android.mk ...
including ./development/apps/CustomLocale/Android.mk ...
including ./development/apps/Development/Android.mk ...
including ./development/apps/DevelopmentSettings/Android.mk ...
including ./development/apps/Fallback/Android.mk ...
including ./development/apps/GestureBuilder/Android.mk ...
including ./development/apps/NinePatchLab/Android.mk ...
including ./development/apps/OBJViewer/Android.mk ...
including ./development/apps/SdkSetup/Android.mk ...
including ./development/apps/SettingInjectorSample/Android.mk ...
including ./development/apps/WidgetPreview/Android.mk ...
including ./development/apps/launchperf/Android.mk ...
including ./development/build/Android.mk ...
including ./development/cmds/monkey/Android.mk ...
including ./development/host/Android.mk ...
including ./development/ndk/Android.mk ...
including ./development/perftests/panorama/Android.mk ...
including ./development/samples/AccelerometerPlay/Android.mk ...
including ./development/samples/ActionBarCompat/Android.mk ...
including ./development/samples/AliasActivity/Android.mk ...
including ./development/samples/AndroidBeamDemo/Android.mk ...
including ./development/samples/ApiDemos/Android.mk ...
including ./development/samples/AppNavigation/Android.mk ...
including ./development/samples/BackupRestore/Android.mk ...
including ./development/samples/BasicGLSurfaceView/Android.mk ...
including ./development/samples/BluetoothChat/Android.mk ...
including ./development/samples/BluetoothHDP/Android.mk ...
including ./development/samples/BusinessCard/Android.mk ...
including ./development/samples/Compass/Android.mk ...
including ./development/samples/ContactManager/Android.mk ...
including ./development/samples/CubeLiveWallpaper/Android.mk ...
including ./development/samples/FixedGridLayout/Android.mk ...
including ./development/samples/HeavyWeight/Android.mk ...
including ./development/samples/HelloActivity/Android.mk ...
including ./development/samples/HelloEffects/Android.mk ...
including ./development/samples/Home/Android.mk ...
including ./development/samples/HoneycombGallery/Android.mk ...
including ./development/samples/JetBoy/Android.mk ...
including ./development/samples/KeyChainDemo/Android.mk ...
including ./development/samples/LunarLander/Android.mk ...
including ./development/samples/MultiResolution/Android.mk ...
including ./development/samples/MySampleRss/Android.mk ...
including ./development/samples/NotePad/Android.mk ...
including ./development/samples/Obb/Android.mk ...
including ./development/samples/RSSReader/Android.mk ...
including ./development/samples/RandomMusicPlayer/Android.mk ...
including ./development/samples/RenderScript/Android.mk ...
including ./development/samples/SampleSyncAdapter/Android.mk ...
including ./development/samples/SearchableDictionary/Android.mk ...
including ./development/samples/SimpleJNI/Android.mk ...
including ./development/samples/SipDemo/Android.mk ...
including ./development/samples/SkeletonApp/Android.mk ...
including ./development/samples/Snake/Android.mk ...
including ./development/samples/SoftKeyboard/Android.mk ...
including ./development/samples/SpellChecker/Android.mk ...
including ./development/samples/StackWidget/Android.mk ...
including ./development/samples/Support13Demos/Android.mk ...
including ./development/samples/Support4Demos/Android.mk ...
including ./development/samples/Support7Demos/Android.mk ...
including ./development/samples/SupportAppNavigation/Android.mk ...
including ./development/samples/SupportLeanbackDemos/Android.mk ...
including ./development/samples/ToyVpn/Android.mk ...
including ./development/samples/TtsEngine/Android.mk ...
including ./development/samples/USB/Android.mk ...
including ./development/samples/UiAutomator/Android.mk ...
including ./development/samples/Vault/Android.mk ...
including ./development/samples/VoiceRecognitionService/Android.mk ...
including ./development/samples/VoicemailProviderDemo/Android.mk ...
including ./development/samples/WeatherListWidget/Android.mk ...
including ./development/samples/WiFiDirectDemo/Android.mk ...
including ./development/samples/WiFiDirectServiceDiscovery/Android.mk ...
including ./development/samples/Wiktionary/Android.mk ...
including ./development/samples/WiktionarySimple/Android.mk ...
including ./development/samples/XmlAdapters/Android.mk ...
including ./development/samples/training/NsdChat/Android.mk ...
including ./development/testrunner/Android.mk ...
including ./development/tools/apkcheck/Android.mk ...
including ./development/tools/elftree/Android.mk ...
including ./development/tools/emulator/test-apps/SmokeTests/Android.mk ...
including ./development/tools/etc1tool/Android.mk ...
including ./development/tools/hosttestlib/Android.mk ...
including ./development/tools/idegen/Android.mk ...
including ./development/tools/line_endings/Android.mk ...
including ./development/tools/mkstubs/Android.mk ...
including ./development/tools/recovery_l10n/Android.mk ...
including ./development/tools/rmtypedefs/Android.mk ...
including ./development/tools/yuv420sp2rgb/Android.mk ...
including ./development/tutorials/MoarRam/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv1/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv1Solution/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv2/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv2Solution/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv3/Android.mk ...
including ./development/tutorials/NotepadCodeLab/Notepadv3Solution/Android.mk ...
including ./development/tutorials/ReverseDebug/Android.mk ...
including ./device/fsl-proprietary/ar3k/Android.mk ...
including ./device/fsl-proprietary/ath6k/firmware/Android.mk ...
including ./device/fsl-proprietary/ath6k/tools/Android.mk ...
including ./device/fsl-proprietary/bcm/Android.mk ...
including ./device/fsl-proprietary/fsl-hwc/Android.mk ...
including ./device/fsl-proprietary/gpu-viv/Android.mk ...
including ./device/fsl-proprietary/pcie-wifi/Android.mk ...
including ./device/fsl-proprietary/ril/Android.mk ...
including ./device/fsl-proprietary/test/Android.mk ...
including ./device/fsl/common/extended/freescale-extended/Android.mk ...
including ./device/fsl/common/recovery/Android.mk ...
including ./device/fsl/common/wifi/Android.mk ...
including ./device/google/accessory/arduino/Android.mk ...
including ./device/google/accessory/demokit/Android.mk ...
including ./device/google/atv/LeanbackSampleApp/Android.mk ...
including ./device/google/atv/sdk/Android.mk ...
including ./device/udoo/common/recovery/Android.mk ...
including ./device/udoo/common/wifi/Android.mk ...
including ./external/aac/Android.mk ...
including ./external/android-clat/Android.mk ...
including ./external/ant-glob/Android.mk ...
including ./external/antlr/Android.mk ...
including ./external/apache-harmony/Android.mk ...
including ./external/apache-http/Android.mk ...
including ./external/apache-xml/Android.mk ...
including ./external/bison/Android.mk ...
including ./external/blktrace/Android.mk ...
including ./external/bluetooth/bluedroid/Android.mk ...
including ./external/bouncycastle/Android.mk ...
including ./external/bsdiff/Android.mk ...
including ./external/bzip2/Android.mk ...
including ./external/ceres-solver/Android.mk ...
including ./external/checkpolicy/Android.mk ...
including ./external/chromium-libpac/Android.mk ...
including ./external/chromium_org/Android.mk ...
including ./external/clang/Android.mk ...
including ./external/compiler-rt/Android.mk ...
including ./external/conscrypt/Android.mk ...
including ./external/deqp/Android.mk ...
including ./external/dexmaker/Android.mk ...
including ./external/dhcpcd/Android.mk ...
including ./external/dnsmasq/Android.mk ...
including ./external/doclava/Android.mk ...
including ./external/droiddriver/Android.mk ...
including ./external/e2fsprogs/Android.mk ...
including ./external/easymock/Android.mk ...
including ./external/eigen/Android.mk ...
including ./external/elfutils/Android.mk ...
including ./external/emma/Android.mk ...
including ./external/expat/Android.mk ...
including ./external/eyes-free/Android.mk ...
including ./external/f2fs-tools/Android.mk ...
including ./external/fdlibm/Android.mk ...
including ./external/fio/Android.mk ...
including ./external/flac/Android.mk ...
including ./external/freetype/Android.mk ...
including ./external/fsck_msdos/Android.mk ...
including ./external/fsl_vpu_omx/Android.mk ...
including ./external/gcc-demangle/Android.mk ...
including ./external/genext2fs/Android.mk ...
including ./external/giflib/Android.mk ...
including ./external/glide/Android.mk ...
including ./external/google-diff-match-patch/Android.mk ...
including ./external/google-fonts/carrois-gothic-sc/Android.mk ...
including ./external/google-fonts/coming-soon/Android.mk ...
including ./external/google-fonts/cutive-mono/Android.mk ...
including ./external/google-fonts/dancing-script/Android.mk ...
including ./external/google-tv-pairing-protocol/Android.mk ...
including ./external/gtest/Android.mk ...
including ./external/guava/Android.mk ...
including ./external/hamcrest/Android.mk ...
including ./external/harfbuzz_ng/Android.mk ...
including ./external/icu/Android.mk ...
including ./external/iproute2/Android.mk ...
including ./external/ipsec-tools/Android.mk ...
including ./external/iptables/Android.mk ...
including ./external/iputils/Android.mk ...
including ./external/jarjar/Android.mk ...
including ./external/javasqlite/Android.mk ...
including ./external/javassist/Android.mk ...
including ./external/jdiff/Android.mk ...
including ./external/jemalloc/Android.mk ...
including ./external/jhead/Android.mk ...
including ./external/jmdns/Android.mk ...
including ./external/jpeg/Android.mk ...
including ./external/jsilver/Android.mk ...
including ./external/jsmn/Android.mk ...
including ./external/jsoncpp/Android.mk ...
including ./external/jsr305/Android.mk ...
including ./external/junit/Android.mk ...
including ./external/ksoap2/Android.mk ...
including ./external/libcap-ng/Android.mk ...
including ./external/libcxx/Android.mk ...
including ./external/libcxxabi/Android.mk ...
including ./external/libedit/Android.mk ...
including ./external/libexif/Android.mk ...
including ./external/libgsm/Android.mk ...
including ./external/libhevc/Android.mk ...
including ./external/liblzf/Android.mk ...
including ./external/libnfc-nci/Android.mk ...
including ./external/libnfc-nxp/Android.mk ...
including ./external/libnl/Android.mk ...
including ./external/libogg/Android.mk ...
including ./external/libopus/Android.mk ...
including ./external/libpcap/Android.mk ...
including ./external/libpng/Android.mk ...
including ./external/libselinux/Android.mk ...
including ./external/libsepol/Android.mk ...
including ./external/libunwind/Android.mk ...
including ./external/libutf/Android.mk ...
including ./external/libvorbis/Android.mk ...
including ./external/libvpx/Android.mk ...
including ./external/libvterm/Android.mk ...
including ./external/libxml2/Android.mk ...
including ./external/libyuv/Android.mk ...
including ./external/linux-firmware-imx/firmware/Android.mk ...
including ./external/linux-lib/hdmi-cec/Android.mk ...
including ./external/linux-lib/ipu/Android.mk ...
including ./external/linux-lib/vpu/Android.mk ...
including ./external/linux-test/test/memtool/Android.mk ...
including ./external/linux-test/test/mmdc/Android.mk ...
including ./external/linux-test/test/mxc_v4l2_test/Android.mk ...
including ./external/linux-test/test/mxc_vpu_test/Android.mk ...
including ./external/linux-tools-perf/Android.mk ...
including ./external/littlemock/Android.mk ...
including ./external/lldb/Android.mk ...
including ./external/llvm/Android.mk ...
including ./external/lohit-fonts/Android.mk ...
including ./external/ltrace/Android.mk ...
including ./external/lzma/C/Util/Lzma/Android.mk ...
including ./external/lzma/xz-embedded/Android.mk ...
including ./external/markdown/Android.mk ...
including ./external/mdnsresponder/Android.mk ...
including ./external/mesa3d/Android.mk ...
including ./external/mksh/Android.mk ...
including ./external/mockito/Android.mk ...
including ./external/mockwebserver/Android.mk ...
including ./external/mp4parser/Android.mk ...
including ./external/mtd-utils/Android.mk ...
including ./external/mtpd/Android.mk ...
including ./external/nanohttpd/Android.mk ...
including ./external/nanopb-c/Android.mk ...
including ./external/naver-fonts/Android.mk ...
including ./external/netcat/Android.mk ...
including ./external/netperf/Android.mk ...
including ./external/neven/Android.mk ...
including ./external/nfacct/Android.mk ...
including ./external/nist-pkits/Android.mk ...
including ./external/noto-fonts/Android.mk ...
including ./external/oauth/core/src/main/java/Android.mk ...
including ./external/objenesis/Android.mk ...
including ./external/okhttp/Android.mk ...
including ./external/opencv/Android.mk ...
including ./external/openfst/Android.mk ...
including ./external/openssl/Android.mk ...
including ./external/oprofile/Android.mk ...
including ./external/owasp/sanitizer/Android.mk ...
including ./external/pcre/Android.mk ...
including ./external/pdfium/core/Android.mk ...
including ./external/pdfium/fpdfsdk/Android.mk ...
including ./external/pixman/Android.mk ...
including ./external/powerdebug/Android.mk ...
including ./external/ppp/android/Android.mk ...
including ./external/ppp/pppd/Android.mk ...
including ./external/proguard/Android.mk ...
including ./external/protobuf/Android.mk ...
including ./external/qemu/Android.mk ...
including ./external/regex-re2/Android.mk ...
including ./external/replicaisland/Android.mk ...
including ./external/robolectric/Android.mk ...
including ./external/safe-iop/Android.mk ...
including ./external/scrypt/Android.mk ...
including ./external/sepolicy/Android.mk ...
including ./external/sfntly/Android.mk ...
including ./external/skia/Android.mk ...
including ./external/smack/Android.mk ...
including ./external/smali/baksmali/Android.mk ...
including ./external/smali/deodexerant/Android.mk ...
including ./external/smali/dexlib2/Android.mk ...
including ./external/smali/smali/Android.mk ...
including ./external/sonivox/Android.mk ...
including ./external/speex/Android.mk ...
including ./external/sqlite/android/Android.mk ...
including ./external/sqlite/dist/Android.mk ...
including ./external/srec/Android.mk ...
including ./external/srtp/Android.mk ...
including ./external/stlport/Android.mk ...
including ./external/strace/Android.mk ...
including ./external/stressapptest/Android.mk ...
including ./external/svox/Android.mk ...
including ./external/tagsoup/Android.mk ...
including ./external/tcpdump/Android.mk ...
including ./external/tinyalsa/Android.mk ...
including ./external/tinycompress/Android.mk ...
including ./external/tinyxml/Android.mk ...
including ./external/tinyxml2/Android.mk ...
including ./external/tools/iw/Android.mk ...
including ./external/tools/prebuilt/Android.mk ...
including ./external/tremolo/Android.mk ...
including ./external/valgrind/Android.mk ...
including ./external/vixl/Android.mk ...
including ./external/webp/Android.mk ...
including ./external/webrtc/Android.mk ...
including ./external/wpa_supplicant_8/Android.mk ...
including ./external/xmp_toolkit/Android.mk ...
including ./external/yaffs2/Android.mk ...
including ./external/zlib/Android.mk ...
including ./external/zopfli/Android.mk ...
including ./external/zxing/Android.mk ...
including ./frameworks/av/camera/Android.mk ...
including ./frameworks/av/cmds/screenrecord/Android.mk ...
including ./frameworks/av/cmds/stagefright/Android.mk ...
including ./frameworks/av/drm/common/Android.mk ...
including ./frameworks/av/drm/drmserver/Android.mk ...
including ./frameworks/av/drm/libdrmframework/Android.mk ...
including ./frameworks/av/drm/mediadrm/plugins/clearkey/Android.mk ...
including ./frameworks/av/drm/mediadrm/plugins/mock/Android.mk ...
including ./frameworks/av/media/common_time/Android.mk ...
including ./frameworks/av/media/img_utils/Android.mk ...
including ./frameworks/av/media/libcpustats/Android.mk ...
including ./frameworks/av/media/libeffects/downmix/Android.mk ...
including ./frameworks/av/media/libeffects/factory/Android.mk ...
including ./frameworks/av/media/libeffects/loudness/Android.mk ...
including ./frameworks/av/media/libeffects/lvm/lib/Android.mk ...
including ./frameworks/av/media/libeffects/lvm/wrapper/Android.mk ...
including ./frameworks/av/media/libeffects/preprocessing/Android.mk ...
including ./frameworks/av/media/libeffects/proxy/Android.mk ...
including ./frameworks/av/media/libeffects/visualizer/Android.mk ...
including ./frameworks/av/media/libmedia/Android.mk ...
including ./frameworks/av/media/libmediaplayerservice/Android.mk ...
including ./frameworks/av/media/libnbaio/Android.mk ...
including ./frameworks/av/media/libstagefright/Android.mk ...
including ./frameworks/av/media/mediaserver/Android.mk ...
including ./frameworks/av/media/mtp/Android.mk ...
including ./frameworks/av/media/ndk/Android.mk ...
including ./frameworks/av/services/audioflinger/Android.mk ...
including ./frameworks/av/services/audiopolicy/Android.mk ...
including ./frameworks/av/services/camera/libcameraservice/Android.mk ...
including ./frameworks/av/services/medialog/Android.mk ...
including ./frameworks/av/services/soundtrigger/Android.mk ...
including ./frameworks/av/soundtrigger/Android.mk ...
including ./frameworks/av/tools/resampler_tools/Android.mk ...
including ./frameworks/base/Android.mk ...
find: ‘phone/java’: No such file or directory
find: ‘phone/java’: No such file or directory
including ./frameworks/compile/libbcc/Android.mk ...
including ./frameworks/compile/mclinker/Android.mk ...
including ./frameworks/compile/slang/Android.mk ...
including ./frameworks/ex/camera2/Android.mk ...
including ./frameworks/ex/common/Android.mk ...
including ./frameworks/ex/framesequence/Android.mk ...
including ./frameworks/ex/variablespeed/Android.mk ...
including ./frameworks/minikin/libs/minikin/Android.mk ...
including ./frameworks/minikin/sample/Android.mk ...
including ./frameworks/ml/Android.mk ...
including ./frameworks/multidex/Android.mk ...
including ./frameworks/native/cmds/atrace/Android.mk ...
including ./frameworks/native/cmds/bugreport/Android.mk ...
including ./frameworks/native/cmds/dumpstate/Android.mk ...
including ./frameworks/native/cmds/dumpsys/Android.mk ...
including ./frameworks/native/cmds/flatland/Android.mk ...
including ./frameworks/native/cmds/installd/Android.mk ...
including ./frameworks/native/cmds/ip-up-vpn/Android.mk ...
including ./frameworks/native/cmds/rawbu/Android.mk ...
including ./frameworks/native/cmds/service/Android.mk ...
including ./frameworks/native/cmds/servicemanager/Android.mk ...
including ./frameworks/native/libs/binder/Android.mk ...
including ./frameworks/native/libs/diskusage/Android.mk ...
including ./frameworks/native/libs/gui/Android.mk ...
including ./frameworks/native/libs/input/Android.mk ...
including ./frameworks/native/libs/ui/Android.mk ...
including ./frameworks/native/opengl/libagl/Android.mk ...
including ./frameworks/native/opengl/libs/Android.mk ...
including ./frameworks/native/opengl/tests/Android.mk ...
including ./frameworks/native/services/batteryservice/Android.mk ...
including ./frameworks/native/services/inputflinger/Android.mk ...
including ./frameworks/native/services/powermanager/Android.mk ...
including ./frameworks/native/services/sensorservice/Android.mk ...
including ./frameworks/native/services/surfaceflinger/Android.mk ...
including ./frameworks/opt/bitmap/Android.mk ...
including ./frameworks/opt/bluetooth/Android.mk ...
including ./frameworks/opt/calendar/Android.mk ...
including ./frameworks/opt/chips/Android.mk ...
including ./frameworks/opt/colorpicker/Android.mk ...
including ./frameworks/opt/datetimepicker/Android.mk ...
including ./frameworks/opt/emoji/Android.mk ...
including ./frameworks/opt/inputmethodcommon/Android.mk ...
including ./frameworks/opt/mms/Android.mk ...
including ./frameworks/opt/net/ethernet/Android.mk ...
including ./frameworks/opt/net/ims/Android.mk ...
including ./frameworks/opt/net/voip/Android.mk ...
including ./frameworks/opt/net/wifi/service/Android.mk ...
including ./frameworks/opt/photoviewer/Android.mk ...
including ./frameworks/opt/setupwizard/navigationbar/Android.mk ...
including ./frameworks/opt/telephony/Android.mk ...
including ./frameworks/opt/timezonepicker/Android.mk ...
including ./frameworks/opt/vcard/Android.mk ...
including ./frameworks/rs/Android.mk ...
including ./frameworks/support/Android.mk ...
find: ‘dummy’: No such file or directory
including ./frameworks/volley/Android.mk ...
including ./frameworks/webview/Android.mk ...
including ./frameworks/wilhelm/src/Android.mk ...
including ./frameworks/wilhelm/tests/Android.mk ...
including ./hardware/akm/AK8975_FS/akmdfs/Android.mk ...
including ./hardware/akm/AK8975_FS/libsensors/Android.mk ...
including ./hardware/broadcom/libbt/Android.mk ...
including ./hardware/broadcom/wlan/bcmdhd/Android.mk ...
including ./hardware/imx/Android.mk ...
including ./hardware/intel/audio_media/hdmi/Android.mk ...
including ./hardware/intel/bootstub/Android.mk ...
including ./hardware/intel/common/libmix/Android.mk ...
including ./hardware/intel/common/libstagefrighthw/Android.mk ...
including ./hardware/intel/common/libva/Android.mk ...
including ./hardware/intel/common/libwsbm/src/Android.mk ...
including ./hardware/intel/common/omx-components/Android.mk ...
including ./hardware/intel/common/utils/ISV/Android.mk ...
including ./hardware/intel/common/utils/ituxd/Android.mk ...
including ./hardware/intel/common/wrs_omxil_core/Android.mk ...
including ./hardware/intel/img/hwcomposer/Android.mk ...
including ./hardware/intel/img/libdrm/Android.mk ...
including ./hardware/intel/img/psb_headers/Android.mk ...
including ./hardware/intel/img/psb_video/Android.mk ...
including ./hardware/invensense/Android.mk ...
including ./hardware/libhardware/Android.mk ...
including ./hardware/libhardware_legacy/Android.mk ...
including ./hardware/qcom/audio/Android.mk ...
including ./hardware/qcom/bt/Android.mk ...
including ./hardware/qcom/display/Android.mk ...
including ./hardware/qcom/gps/Android.mk ...
including ./hardware/qcom/keymaster/Android.mk ...
including ./hardware/qcom/media/Android.mk ...
including ./hardware/qcom/power/Android.mk ...
including ./hardware/qcom/wlan/qcwcn/Android.mk ...
including ./hardware/realtek/wlan/Android.mk ...
including ./hardware/ril/libril/Android.mk ...
including ./hardware/ril/librilutils/Android.mk ...
including ./hardware/ril/reference-ril/Android.mk ...
including ./hardware/ril/rild/Android.mk ...
including ./hardware/ril/runtime-ril-port/Android.mk ...
including ./hardware/samsung_slsi/exynos5/Android.mk ...
including ./hardware/ti/omap3/Android.mk ...
including ./hardware/ti/omap4-aah/Android.mk ...
including ./hardware/ti/omap4xxx/Android.mk ...
including ./hardware/ti/wlan/WILINK8/wpa_supplicant_lib/Android.mk ...
including ./hardware/ti/wpan/Android.mk ...
including ./kernel_imx/tools/gator/daemon/Android.mk ...
including ./libcore/Android.mk ...
including ./libnativehelper/Android.mk ...
including ./ndk/Android.mk ...
including ./packages/apps/BasicSmsReceiver/Android.mk ...
including ./packages/apps/Bluetooth/Android.mk ...
including ./packages/apps/Browser/Android.mk ...
including ./packages/apps/Calculator/Android.mk ...
including ./packages/apps/Calendar/Android.mk ...
including ./packages/apps/Camera/Android.mk ...
including ./packages/apps/Camera2/Android.mk ...
including ./packages/apps/CellBroadcastReceiver/Android.mk ...
including ./packages/apps/CertInstaller/Android.mk ...
including ./packages/apps/Contacts/Android.mk ...
including ./packages/apps/ContactsCommon/Android.mk ...
including ./packages/apps/DeskClock/Android.mk ...
including ./packages/apps/Dialer/Android.mk ...
including ./packages/apps/Email/Android.mk ...
including ./packages/apps/Exchange/Android.mk ...
including ./packages/apps/FMRadio/Android.mk ...
including ./packages/apps/Gallery/Android.mk ...
including ./packages/apps/Gallery2/Android.mk ...
including ./packages/apps/HTMLViewer/Android.mk ...
including ./packages/apps/KeyChain/Android.mk ...
including ./packages/apps/Launcher2/Android.mk ...
including ./packages/apps/Launcher3/Android.mk ...
including ./packages/apps/LegacyCamera/Android.mk ...
including ./packages/apps/ManagedProvisioning/Android.mk ...
including ./packages/apps/Mms/Android.mk ...
including ./packages/apps/Music/Android.mk ...
including ./packages/apps/MusicFX/Android.mk ...
including ./packages/apps/Nfc/Android.mk ...
including ./packages/apps/OneTimeInitializer/Android.mk ...
including ./packages/apps/PackageInstaller/Android.mk ...
including ./packages/apps/PhoneCommon/Android.mk ...
including ./packages/apps/Protips/Android.mk ...
including ./packages/apps/Provision/Android.mk ...
including ./packages/apps/QuickSearchBox/Android.mk ...
including ./packages/apps/Settings/Android.mk ...
including ./packages/apps/SoundRecorder/Android.mk ...
including ./packages/apps/SpareParts/Android.mk ...
including ./packages/apps/SpeechRecorder/Android.mk ...
including ./packages/apps/Stk/Android.mk ...
including ./packages/apps/Tag/Android.mk ...
including ./packages/apps/Terminal/Android.mk ...
including ./packages/apps/TvSettings/QuickSettings/Android.mk ...
including ./packages/apps/TvSettings/Settings/Android.mk ...
including ./packages/apps/UnifiedEmail/Android.mk ...
including ./packages/apps/VoiceDialer/Android.mk ...
including ./packages/apps/fsl_imx_demo/Android.mk ...
including ./packages/experimental/Android.mk ...
including ./packages/inputmethods/LatinIME/Android.mk ...
including ./packages/inputmethods/OpenWnn/Android.mk ...
including ./packages/providers/CalendarProvider/Android.mk ...
including ./packages/providers/ContactsProvider/Android.mk ...
including ./packages/providers/DownloadProvider/Android.mk ...
including ./packages/providers/MediaProvider/Android.mk ...
including ./packages/providers/PartnerBookmarksProvider/Android.mk ...
including ./packages/providers/TelephonyProvider/Android.mk ...
including ./packages/providers/TvProvider/Android.mk ...
including ./packages/providers/UserDictionaryProvider/Android.mk ...
including ./packages/screensavers/Basic/Android.mk ...
including ./packages/screensavers/PhotoTable/Android.mk ...
including ./packages/screensavers/WebView/Android.mk ...
including ./packages/services/Mms/Android.mk ...
including ./packages/services/Telecomm/Android.mk ...
including ./packages/services/Telephony/Android.mk ...
including ./packages/wallpapers/Basic/Android.mk ...
including ./packages/wallpapers/Galaxy4/Android.mk ...
including ./packages/wallpapers/HoloSpiral/Android.mk ...
including ./packages/wallpapers/LivePicker/Android.mk ...
including ./packages/wallpapers/MagicSmoke/Android.mk ...
including ./packages/wallpapers/MusicVisualization/Android.mk ...
including ./packages/wallpapers/NoiseField/Android.mk ...
including ./packages/wallpapers/PhaseBeam/Android.mk ...
including ./pdk/apps/HelloPDK/Android.mk ...
including ./pdk/apps/TestingCamera/Android.mk ...
including ./pdk/apps/TestingCamera2/Android.mk ...
including ./prebuilts/gcc/darwin-x86/aarch64/aarch64-linux-android-4.8/Android.mk ...
including ./prebuilts/gcc/darwin-x86/aarch64/aarch64-linux-android-4.9/Android.mk ...
including ./prebuilts/gcc/darwin-x86/arm/arm-linux-androideabi-4.8/Android.mk ...
including ./prebuilts/gcc/darwin-x86/mips/mipsel-linux-android-4.8/Android.mk ...
including ./prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.8/Android.mk ...
including ./prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/Android.mk ...
including ./prebuilts/gcc/linux-x86/arm/arm-linux-androideabi-4.8/Android.mk ...
including ./prebuilts/gcc/linux-x86/mips/mipsel-linux-android-4.8/Android.mk ...
including ./prebuilts/misc/Android.mk ...
including ./prebuilts/ndk/Android.mk ...
including ./prebuilts/qemu-kernel/x86/pc-bios/Android.mk ...
including ./prebuilts/sdk/Android.mk ...
including ./prebuilts/tools/Android.mk ...
including ./sdk/adtproductbuild/Android.mk ...
including ./sdk/annotations/Android.mk ...
including ./sdk/avdlauncher/Android.mk ...
including ./sdk/dumpeventlog/Android.mk ...
including ./sdk/emulator/mksdcard/Android.mk ...
including ./sdk/emulator/opengl/Android.mk ...
including ./sdk/eventanalyzer/Android.mk ...
including ./sdk/find_java/Android.mk ...
including ./sdk/find_lock/Android.mk ...
including ./sdk/hierarchyviewer/etc/Android.mk ...
including ./sdk/hierarchyviewer/src/Android.mk ...
including ./sdk/monitor/Android.mk ...
including ./sdk/sdklauncher/Android.mk ...
including ./system/core/Android.mk ...
including ./system/extras/Android.mk ...
including ./system/keymaster/Android.mk ...
including ./system/media/audio_route/Android.mk ...
including ./system/media/audio_utils/Android.mk ...
including ./system/media/camera/src/Android.mk ...
including ./system/media/camera/tests/Android.mk ...
including ./system/netd/client/Android.mk ...
including ./system/netd/server/Android.mk ...
including ./system/security/keystore-engine/Android.mk ...
including ./system/security/keystore/Android.mk ...
including ./system/security/softkeymaster/Android.mk ...
including ./system/vold/Android.mk ...
including ./tools/external/fat32lib/Android.mk ...
No private recovery resources for TARGET_DEVICE udooneo_6sx
build/core/main.mk:1056: warning: overriding recipe for target 'clean'
external/tools/iw/Makefile:126: warning: ignoring old recipe for target 'clean'
host Java: doclava (out/host/common/obj/JAVA_LIBRARIES/doclava_intermediates/classes)
target Java: conscrypt (out/target/common/obj/JAVA_LIBRARIES/conscrypt_intermediates/classes)
Install: out/host/linux-x86/framework/jarjar.jar
target Java: ext (out/target/common/obj/JAVA_LIBRARIES/ext_intermediates/classes)
target Java: core-junit (out/target/common/obj/JAVA_LIBRARIES/core-junit_intermediates/classes)
Copy: rmtypedefs (out/host/linux-x86/obj/EXECUTABLES/rmtypedefs_intermediates/rmtypedefs)
Copying: out/target/common/obj/JAVA_LIBRARIES/apache-xml_intermediates/classes-jarjar.jar
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
make -C kernel_imx udoo_neo_android_defconfig ARCH=arm CROSS_COMPILE=`pwd`/prebuilts/gcc/linux-x86/arm/arm-eabi-4.6/bin/arm-eabi- LOADADDR=0x80008000 O=`pwd`/out/target/product/udooneo_6sx/obj/KERNEL_OBJ 
host SharedLib: libc++_32 (out/host/linux-x86/obj32/lib/libc++.so)
host SharedLib: libnativehelper_32 (out/host/linux-x86/obj32/lib/libnativehelper.so)
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(abort_message.o): unsupported reloc 43 against global symbol stderr
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_aux_runtime.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_aux_runtime.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_aux_runtime.o): unsupported reloc 43 against global symbol std::bad_typeid::~bad_typeid()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_aux_runtime.o): unsupported reloc 43 against global symbol typeinfo for std::bad_typeid
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_default_handlers.o): unsupported reloc 43 against global symbol typeinfo for std::exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_default_handlers.o): unsupported reloc 43 against global symbol __cxa_unexpected_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_default_handlers.o): unsupported reloc 43 against global symbol __cxa_terminate_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_unexpected_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_unexpected_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_terminate_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_terminate_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_new_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_handlers.o): unsupported reloc 43 against global symbol __cxa_new_handler
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol vtable for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol vtable for std::bad_array_new_length
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol vtable for std::bad_array_length
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol vtable for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_new_delete.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_personality.o): unsupported reloc 43 against global symbol vtable for std::bad_exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_personality.o): unsupported reloc 43 against global symbol typeinfo for std::bad_exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(cxa_personality.o): unsupported reloc 43 against global symbol std::bad_exception::~bad_exception()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for __cxxabiv1::__class_type_info
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for __cxxabiv1::__shim_type_info
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for decltype(nullptr)
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for decltype(nullptr)
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for __cxxabiv1::__pointer_type_info
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for __cxxabiv1::__shim_type_info
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for void
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(private_typeinfo.o): unsupported reloc 43 against global symbol typeinfo for __cxxabiv1::__class_type_info
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::logic_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(stdexcept.o): unsupported reloc 43 against global symbol vtable for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(typeinfo.o): unsupported reloc 43 against global symbol vtable for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(typeinfo.o): unsupported reloc 43 against global symbol vtable for std::bad_typeid
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(eprintf.o): unsupported reloc 43 against global symbol stderr
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(algorithm.o): unsupported reloc 43 against global symbol std::__1::__rs_default::__c_
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(algorithm.o): unsupported reloc 43 against global symbol std::__1::__rs_default::__c_
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(algorithm.o): unsupported reloc 43 against global symbol std::__1::__rs_default::__c_
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(algorithm.o): unsupported reloc 43 against global symbol std::__1::__rs_default::__c_
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::__libcpp_db::~__libcpp_db()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::__libcpp_db::~__libcpp_db()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol vtable for std::__1::__c_node
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol vtable for std::__1::__c_node
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(debug.o): unsupported reloc 43 against global symbol std::__1::mutex::~mutex()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(exception.o): unsupported reloc 43 against global symbol vtable for std::nested_exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(exception.o): unsupported reloc 43 against global symbol vtable for std::nested_exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(exception.o): unsupported reloc 43 against global symbol vtable for std::nested_exception
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::__assoc_sub_state
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol std::__1::future_error::~future_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol typeinfo for std::__1::future_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::__assoc_sub_state
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(future.o): unsupported reloc 43 against global symbol vtable for std::__1::__assoc_sub_state
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(hash.o): unsupported reloc 43 against global symbol vtable for std::overflow_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(hash.o): unsupported reloc 43 against global symbol std::overflow_error::~overflow_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(hash.o): unsupported reloc 43 against global symbol typeinfo for std::overflow_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::ios_base::failure
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ios_base::failure::~failure()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol typeinfo for std::__1::ios_base::failure
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::ios_base
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::bad_alloc::~bad_alloc()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol typeinfo for std::bad_alloc
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::error_category::~error_category()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::ios_base::failure
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::ios_base::failure
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ios_base::__xindex_
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ios<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol construction vtable for std::__1::basic_istream<char, std::__1::char_traits<char> >-in-std::__1::basic_iostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_iostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol construction vtable for std::__1::basic_istream<char, std::__1::char_traits<char> >-in-std::__1::basic_iostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(ios.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_iostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol stdin
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::cin
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol stdout
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::cout
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol stderr
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::cerr
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::clog
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_istream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wcin
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_ostream<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wcout
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wcerr
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wclog
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::cout
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::clog
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wcout
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::wclog
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<wchar_t, std::__1::char_traits<wchar_t> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol vtable for std::__1::basic_streambuf<char, std::__1::char_traits<char> >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::__start_std_streams
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(iostream.o): unsupported reloc 43 against global symbol std::__1::ios_base::Init::~Init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<char, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<char16_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char16_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<char32_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char32_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct<char, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct<char, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct<wchar_t, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct<wchar_t, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::money_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::money_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::money_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::money_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char16_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char16_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char32_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char32_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<char, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<char, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<wchar_t, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<wchar_t, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put_byname<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put_byname<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char16_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char16_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt_byname<char32_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char32_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<char, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<char, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<wchar_t, false>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::moneypunct_byname<wchar_t, true>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put_byname<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put_byname<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::messages_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::collate<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char16_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<char32_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::codecvt<wchar_t, char, __mbstate_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<char, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, false>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::moneypunct<wchar_t, true>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::money_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::messages<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__init()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::bad_cast::~bad_cast()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::bad_cast
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::locale::id::__next_id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::numpunct<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__weeks() const::weeks
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__months() const::months
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__am_pm() const::am_pm
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<char, std::__1::char_traits<char>, std::__1::allocator<char> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<wchar_t, std::__1::char_traits<wchar_t>, std::__1::allocator<wchar_t> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__x() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<char, std::__1::char_traits<char>, std::__1::allocator<char> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<wchar_t, std::__1::char_traits<wchar_t>, std::__1::allocator<wchar_t> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__X() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<char, std::__1::char_traits<char>, std::__1::allocator<char> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<wchar_t, std::__1::char_traits<wchar_t>, std::__1::allocator<wchar_t> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__c() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<char>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<char, std::__1::char_traits<char>, std::__1::allocator<char> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<char>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol guard variable for std::__1::__time_get_c_storage<wchar_t>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol __dso_handle
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::basic_string<wchar_t, std::__1::char_traits<wchar_t>, std::__1::allocator<wchar_t> >::~basic_string()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__time_get_c_storage<wchar_t>::__r() const::s
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::runtime_error::~runtime_error()
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol typeinfo for std::runtime_error
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::ctype_byname<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::codecvt<wchar_t, char, __mbstate_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<char>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::collate<wchar_t>
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::__num_get_base::__src
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::num_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::numpunct<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<wchar_t>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<char, std::__1::istreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_get_byname<wchar_t, std::__1::istreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol std::__1::ctype<char>::id
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<char, std::__1::ostreambuf_iterator<char, std::__1::char_traits<char> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::locale::facet
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/ld: error: out/host/linux-x86/obj32/STATIC_LIBRARIES/libc++_intermediates/libc++.a(locale.o): unsupported reloc 43 against global symbol vtable for std::__1::time_put<wchar_t, std::__1::ostreambuf_iterator<wchar_t, std::__1::char_traits<wchar_t> > >
prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.11-4.6//x86_64-linux/bin/
