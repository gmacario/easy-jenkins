# Building a Yocto distribution for the UDOO Neo

This document explains how to build from sources a [Yocto](https://www.yoctoproject.org/)-based distribution for the [UDOO Neo](http://www.udoo.org/udoo-neo/) using [easy-jenkins](https://github.com/gmacario/easy-jenkins).

The following instructions were tested on

* Docker client: mac-tizy (HW: MacBook Pro; SW: OS X 10.11.3, Docker Toolbox 10.0)
* Docker engine: mv-linux-powerhorse (HW: HP xw8600 Workstation, SW: Ubuntu 14.04.4 LTS 64-bit, Docker 1.10.0)

## Preparation

* Install and configure easy-jenkins - please refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.
* Verify that the Jenkins Dashboard is accessible at `${DOCKER_URL}` (example: http://192.168.99.100:9080/)

## Step-by-step instructions

### Configure project `build_yocto_udooneo`

Browse `${JENKINS_URL}`, then click **New Item**
  - Item name: `build_yocto_udooneo`
  - Type: **Freestyle project**

  then click **OK**.

Inside the project configuration page, fill-in the following information:
  - Discard Old Builds: Yes
    - Strategy: Log Rotation
      - Days to keep builds: (none)
      - Max # of builds to keep: 2
  - Source Code Management: Git
    - Repositories
      - Repository URL: `https://github.com/gmacario/genivi-demo-platform`
      - Credentials: - none -
      - Branches to build
        - Branch Specifier (blank for 'any'): `*/dev-udooneo-jethro`
      - Repository browser: (Auto)
  - Build Environment
    - Build inside a Docker container: Yes
      - Docker image to use: Pull docker image from repository
        - Image id/tag: `gmacario/build-yocto`
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

# Workaround in case USER is undefined
[ "$USER" = "" ] && export USER=jenkins

# Configure git
git config --global user.name "easy-jenkins"
git config --global user.email "$USER@$(hostname)"

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
[ $(whoami) = "root" ] && touch conf/sanity.conf

# Workaround for "DISTRO 'poky-ivi-systemd' not found."
export DISTRO="poky"

# Workaround for https://github.com/gmacario/easy-jenkins/issues/57
bitbake m4-firmware

bitbake core-image-minimal
# bitbake udoo-image-full-cmdline
# bitbake genivi-demo-platform

# EOF
```
- Post-build Actions
    - Archive the artifacts
      - Files to archive: `*/tmp-glibc/deploy/images/**/*.sdcard.gz`

then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-28 11:18 CET): Tested on dc7600-gm -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

You may watch the build logs at `${JENKINS_URL}/job/build_yocto_udooneo/lastBuild/console`

<!-- TODO: Update when build successful -->

```
Started by user anonymous
[EnvInject] - Loading node environment variables.
Building in workspace /var/jenkins_home/workspace/build_yocto_udooneo
Cloning the remote Git repository
Cloning repository https://github.com/gmacario/genivi-demo-platform
 > git init /var/jenkins_home/workspace/build_yocto_udooneo # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git --version # timeout=10
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/gmacario/genivi-demo-platform # timeout=10
Fetching upstream changes from https://github.com/gmacario/genivi-demo-platform
 > git -c core.askpass=true fetch --tags --progress https://github.com/gmacario/genivi-demo-platform +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/dev-udooneo-jethro^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/dev-udooneo-jethro^{commit} # timeout=10
Checking out Revision c83152c2cc1e87d0f6adfcc45358331635f34618 (refs/remotes/origin/dev-udooneo-jethro)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f c83152c2cc1e87d0f6adfcc45358331635f34618
First time build. Skipping changelog.
Pull Docker image gmacario/build-yocto from repository ...
$ docker pull gmacario/build-yocto
...
TODO
```

Result: SUCCESS

## Testing the image on UDOO Neo

* Browse `${JENKINS_URL}/jobs/build_yocto_udooneo`, in section "Last Successful Artifacts" click on `core-image-minimal-udooneo.sdcard.gz` to download the file to a machine with a SD Card writer.
* Write the image to a blank Micro SD card
  - On MS Windows first uncompress the image, then use [Win 32 Disk Imager](https://sourceforge.net/projects/win32diskimager/) to write the image to the Micro SD card
  - On Linux just use `$ zcat *.sdcard.gz | sudo dd of=/dev/xxx`
* Insert the Micro SD card on your UDOO Neo and power up the board.

Please see http://www.udoo.org/get-started-neo/ for details.

Please see [this blog post](http://gmacario.github.io/howto/udoo/neo/embedded/software/development/2015/11/08/connecting-to-udoo-neo-serial-console.html) to learn how to connect to the UDOO Neo serial console.

After powering up your UDOO Neo the serial console should display the boot messages up to a login prompt:

```
U-Boot SPL 2015.04 (Feb 24 2016 - 14:37:26)
Setting 1024MB RAM calibration data
port 1


U-Boot 2015.04 (Feb 24 2016 - 14:37:26)

CPU:   Freescale i.MX6SX rev1.2 at 792 MHz
CPU:   Temperature 50 C
Reset cause: POR
Board: UDOO Neo Full
I2C:   ready
DRAM:  1 GiB
PMIC:  PFUZE3000 DEV_ID=0x30 REV_ID=0x11
MMC:   FSL_SDHC: 0, FSL_SDHC: 1
*** Warning - bad CRC, using default environment

In:    serial
Out:   serial
Err:   serial
Net:   CPU Net Initialization Failed
No ethernet found.
Normal Boot
Hit any key to stop autoboot:
...

Starting syslogd/klogd: done

OpenEmbedded nodistro.0 udooneo /dev/ttymxc0

udooneo login:
```

Login to the target with user `root` (default password: none)

```
root@udooneo:~# cat /proc/version
Linux version 3.14.56_1.0.x-udoo+gfeef1c3 (root@1cfc10892ef9) (gcc version 5.2.0 (GCC) ) #1 SMP PREEMPT Wed Feb 24 14:30:31 UTC 2016
root@udooneo:~#  fdisk -l

Disk /dev/mmcblk0: 15.7 GB, 15720251392 bytes
4 heads, 32 sectors/track, 239872 cylinders
Units = cylinders of 128 * 512 = 65536 bytes

        Device Boot      Start         End      Blocks  Id System
/dev/mmcblk0p1              65         192        8192   c Win95 FAT32 (LBA)
/dev/mmcblk0p2             193         320        8192  83 Linux
root@udooneo:~# df -h
Filesystem                Size      Used Available Use% Mounted on
/dev/root                 6.7M      4.8M      1.4M  77% /
devtmpfs                337.1M         0    337.1M   0% /dev
tmpfs                   497.2M     84.0K    497.1M   0% /run
tmpfs                   497.2M     56.0K    497.2M   0% /var/volatile
root@udooneo:~#
```

<!-- EOF -->
