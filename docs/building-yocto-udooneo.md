**WORK-IN-PROGRESS**

# Building Yocto distro for UDOO Neo inside easy-jenkins

This document explains how to buil from sources a Yocto-based distribution for the UDOO Neo.

This procedure may be useful to verify non-regressions of a new feature/bugfix before merging it to the master branch.

The following instructions were tested on

* Docker client: mac-tizy (MacBook Pro, OS X)
* Docker engine: Oracle VirtualBox VM created by Docker Toolbox

## Preparation

Install easy-jenkins from https://github.com/gmacario/easy-jenkins

Refer to [preparation.md](https://github.com/gmacario/easy-jenkins/blob/master/docs/preparation.md) for details.

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
        - Branch Specifier (blank for `any`): `*/dev-udooneo` (TODO?)
      - Repository browser: (Auto)
  - Build Environment
    - Build inside a Docker container: Yes
      - Docker image to use: Pull docker image from repository
        - Image id/tag: `gmacario/build-yocto-new`
  - Build
    - Execute shell
      - Command

```
#!/bin/bash -xe

# DEBUG
id
pwd
ls -la
printenv

# Prepare Yocto build
source init.sh

# Prevent error "Do not use Bitbake as root"
touch conf/sanity.conf

bitbake genivi-demo-platform
```
  
  then click **Save**

### Build project `build_yocto_udooneo`

<!-- (2016-02-20 07:52 CET) -->

Browse `${JENKINS_URL}/job/build_yocto_udooneo`, then click **Build Now**

Result: FAILURE

```
...
[89A[JCurrently 2 running tasks (1099 of 4500):
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
1: qtbase-5.3.2-r0 do_configure (pid 11490)
[133A[J[1;31mERROR[0m: [31mFunction failed: do_configure (log file is located at /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/temp/log.do_configure.11490)[0m
Currently 2 running tasks (1099 of 4500):
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
1: qtbase-5.3.2-r0 do_configure (pid 11490)
[133A[J[1;31mERROR[0m: [31mLogfile of failure stored in: /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/temp/log.do_configure.11490[0m
Log data follows:
| DEBUG: Executing python function sysroot_cleansstate
| DEBUG: Python function sysroot_cleansstate finished
| DEBUG: Executing shell function qmake5_base_preconfigure
| DEBUG: Shell function qmake5_base_preconfigure finished
| DEBUG: Executing shell function do_configure
| 
| This is the Qt Open Source Edition.
| 
| You are licensed to use this software under the terms of
| the Lesser GNU General Public License (LGPL) versions 2.1.
| You are also licensed to use this software under the terms of
| the GNU General Public License (GPL) versions 3.
| 
| You have already accepted the terms of the Open Source license.
| 
| Performing shadow build...
| Preparing build tree...
| arm-poky-linux-gnueabi-g++ -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -fvisibility=hidden fvisibility.c
| Symbol visibility control enabled.
| arm-poky-linux-gnueabi-g++ -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -o libtest.so -shared -Wl,-Bsymbolic-functions -fPIC bsymbolic_functions.c
| bsymbolic_functions.c:2:2: error: #error "Symbolic function binding on this architecture may be broken, disabling it (see QTBUG-36129)."
|  #error "Symbolic function binding on this architecture may be broken, disabling it (see QTBUG-36129)."
|   ^
| Symbolic function binding disabled.
| DEFAULT_INCDIRS="/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include/c++/4.9.1
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include/c++/4.9.1/arm-poky-linux-gnueabi
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include/c++/4.9.1/backward
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/x86_64-linux/usr/lib/arm-poky-linux-gnueabi/gcc/arm-poky-linux-gnueabi/4.9.1/include
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib/gcc/arm-poky-linux-gnueabi/4.9.1/include
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/x86_64-linux/usr/lib/arm-poky-linux-gnueabi/gcc/arm-poky-linux-gnueabi/4.9.1/include-fixed
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include
| "
| DEFAULT_LIBDIRS="/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/x86_64-linux/usr/lib/arm-poky-linux-gnueabi/gcc/arm-poky-linux-gnueabi/4.9.1
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/lib
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib/arm-poky-linux-gnueabi/4.9.1
| "
| <srcbase> = /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/qtbase-opensource-src-5.3.2
| <outbase> = /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/build
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/animation/ { qabstractanimation.h (1), qabstractanimation_p.h (1), qanimationgroup.h (1), qanimationgroup_p.h (1), qparallelanimationgroup.h (1), qparallelanimationgroup_p.h (1), qpauseanimation.h (1), qpropertyanimation.h (1), qpropertyanimation_p.h (1), qsequentialanimationgroup.h (1), qsequentialanimationgroup_p.h (1), qvariantanimation.h (1), qvariantanimation_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/arch/ { qatomic_armv5.h (1), qatomic_armv6.h (1), qatomic_armv7.h (1), qatomic_bootstrap.h (1), qatomic_cxx11.h (1), qatomic_gcc.h (1), qatomic_ia64.h (1), qatomic_mips.h (1), qatomic_msvc.h (1), qatomic_unix.h (1), qatomic_x86.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/codecs/ { cp949codetbl_p.h (1), qbig5codec_p.h (1), qeucjpcodec_p.h (1), qeuckrcodec_p.h (1), qgb18030codec_p.h (1), qiconvcodec_p.h (1), qicucodec_p.h (1), qisciicodec_p.h (1), qjiscodec_p.h (1), qjpunicode_p.h (1), qlatincodec_p.h (1), qsimplecodec_p.h (1), qsjiscodec_p.h (1), qtextcodec.h (1), qtextcodec_p.h (1), qtsciicodec_p.h (1), qutfcodec_p.h (1), qwindowscodec_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/global/ { qcompilerdetection.h (1), qconfig-dist.h (1), qconfig-large.h (1), qconfig-medium.h (1), qconfig-minimal.h (1), qconfig-nacl.h (1), qconfig-small.h (1), qendian.h (1), qflags.h (1), qglobal.h (1), qglobalstatic.h (1), qisenum.h (1), qlibraryinfo.h (1), qlogging.h (1), qnamespace.h (1), qnumeric.h (1), qnumeric_p.h (1), qprocessordetection.h (1), qsysinfo.h (1), qsystemdetection.h (1), qt_pch.h (1), qt_windows.h (1), qtypeinfo.h (1), qtypetraits.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/io/ { qabstractfileengine_p.h (1), qbuffer.h (1), qdatastream.h (1), qdatastream_p.h (1), qdataurl_p.h (1), qdebug.h (1), qdir.h (1), qdir_p.h (1), qdiriterator.h (1), qfile.h (1), qfile_p.h (1), qfiledevice.h (1), qfiledevice_p.h (1), qfileinfo.h (1), qfileinfo_p.h (1), qfileselector.h (1), qfileselector_p.h (1), qfilesystemengine_p.h (1), qfilesystementry_p.h (1), qfilesystemiterator_p.h (1), qfilesystemmetadata_p.h (1), qfilesystemwatcher.h (1), qfilesystemwatcher_fsevents_p.h (1), qfilesystemwatcher_inotify_p.h (1), qfilesystemwatcher_kqueue_p.h (1), qfilesystemwatcher_p.h (1), qfilesystemwatcher_polling_p.h (1), qfilesystemwatcher_win_p.h (1), qfsfileengine_iterator_p.h (1), qfsfileengine_p.h (1), qiodevice.h (1), qiodevice_p.h (1), qipaddress_p.h (1), qlockfile.h (1), qlockfile_p.h (1), qloggingcategory.h (1), qloggingcategory_p.h (1), qloggingregistry_p.h (1), qnoncontiguousbytedevice_p.h (1), qprocess.h (1), qprocess_p.h (1), qresource.h (1), qresource_iterator_p.h (1), qresource_p.h (1), qsavefile.h (1), qsavefile_p.h (1), qsettings.h (1), qsettings_p.h (1), qstandardpaths.h (1), qtemporarydir.h (1), qtemporaryfile.h (1), qtemporaryfile_p.h (1), qtextstream.h (1), qtextstream_p.h (1), qtldurl_p.h (1), qurl.h (1), qurl_p.h (1), qurlquery.h (1), qurltlds_p.h (1), qwindowspipereader_p.h (1), qwindowspipewriter_p.h (1), qwinoverlappedionotifier_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/itemmodels/ { qabstractitemmodel.h (1), qabstractitemmodel_p.h (1), qabstractproxymodel.h (1), qabstractproxymodel_p.h (1), qidentityproxymodel.h (1), qitemselectionmodel.h (1), qitemselectionmodel_p.h (1), qsortfilterproxymodel.h (1), qstringlistmodel.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/json/ { qjson_p.h (1), qjsonarray.h (1), qjsondocument.h (1), qjsonobject.h (1), qjsonparser_p.h (1), qjsonvalue.h (1), qjsonwriter_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/kernel/ { qabstracteventdispatcher.h (1), qabstracteventdispatcher_p.h (1), qabstractnativeeventfilter.h (1), qbasictimer.h (1), qcore_mac_p.h (1), qcore_unix_p.h (1), qcoreapplication.h (1), qcoreapplication_p.h (1), qcorecmdlineargs_p.h (1), qcoreevent.h (1), qcoreglobaldata_p.h (1), qcrashhandler_p.h (1), qeventdispatcher_blackberry_p.h (1), qeventdispatcher_glib_p.h (1), qeventdispatcher_unix_p.h (1), qeventdispatcher_win_p.h (1), qeventdispatcher_winrt_p.h (1), qeventloop.h (1), qeventloop_p.h (1), qfunctions_nacl.h (1), qfunctions_p.h (1), qfunctions_vxworks.h (1), qfunctions_wince.h (1), qfunctions_winrt.h (1), qjni_p.h (1), qjnihelpers_p.h (1), qmath.h (1), qmetaobject.h (1), qmetaobject_moc_p.h (1), qmetaobject_p.h (1), qmetaobjectbuilder_p.h (1), qmetatype.h (1), qmetatype_p.h (1), qmetatypeswitcher_p.h (1), qmimedata.h (1), qobject.h (1), qobject_impl.h (1), qobject_p.h (1), qobjectcleanuphandler.h (1), qobjectdefs.h (1), qobjectdefs_impl.h (1), qpointer.h (1), qppsattribute_p.h (1), qppsattributeprivate_p.h (1), qppsobject_p.h (1), qppsobjectprivate_p.h (1), qsharedmemory.h (1), qsharedmemory_p.h (1), qsignalmapper.h (1), qsocketnotifier.h (1), qsystemerror_p.h (1), qsystemsemaphore.h (1), qsystemsemaphore_p.h (1), qtimer.h (1), qtimerinfo_unix_p.h (1), qtranslator.h (1), qtranslator_p.h (1), qvariant.h (1), qvariant_p.h (1), qwineventnotifier.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/mimetypes/ { qmimedatabase.h (1), qmimedatabase_p.h (1), qmimeglobpattern_p.h (1), qmimemagicrule_p.h (1), qmimemagicrulematcher_p.h (1), qmimeprovider_p.h (1), qmimetype.h (1), qmimetype_p.h (1), qmimetypeparser_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/plugin/ { qelfparser_p.h (1), qfactoryinterface.h (1), qfactoryloader_p.h (1), qlibrary.h (1), qlibrary_p.h (1), qmachparser_p.h (1), qplugin.h (1), qpluginloader.h (1), qsystemlibrary_p.h (1), quuid.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/statemachine/ { qabstractstate.h (1), qabstractstate_p.h (1), qabstracttransition.h (1), qabstracttransition_p.h (1), qeventtransition.h (1), qeventtransition_p.h (1), qfinalstate.h (1), qhistorystate.h (1), qhistorystate_p.h (1), qsignaleventgenerator_p.h (1), qsignaltransition.h (1), qsignaltransition_p.h (1), qstate.h (1), qstate_p.h (1), qstatemachine.h (1), qstatemachine_p.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/thread/ { qatomic.h (1), qbasicatomic.h (1), qexception.h (1), qfuture.h (1), qfutureinterface.h (1), qfutureinterface_p.h (1), qfuturesynchronizer.h (1), qfuturewatcher.h (1), qfuturewatcher_p.h (1), qgenericatomic.h (1), qmutex.h (1), qmutex_p.h (1), qmutexpool_p.h (1), qorderedmutexlocker_p.h (1), qreadwritelock.h (1), qreadwritelock_p.h (1), qresultstore.h (1), qrunnable.h (1), qsemaphore.h (1), qthread.h (1), qthread_p.h (1), qthreadpool.h (1), qthreadpool_p.h (1), qthreadstorage.h (1), qwaitcondition.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/tools/ { qalgorithms.h (1), qarraydata.h (1), qarraydataops.h (1), qarraydatapointer.h (1), qbitarray.h (1), qbytearray.h (1), qbytearraymatcher.h (1), qbytedata_p.h (1), qcache.h (1), qchar.h (1), qcollator.h (1), qcollator_p.h (1), qcommandlineoption.h (1), qcommandlineparser.h (1), qcontainerfwd.h (1), qcontiguouscache.h (1), qcryptographichash.h (1), qdatetime.h (1), qdatetime_p.h (1), qdatetimeparser_p.h (1), qeasingcurve.h (1), qelapsedtimer.h (1), qfreelist_p.h (1), qharfbuzz_p.h (1), qhash.h (1), qiterator.h (1), qline.h (1), qlinkedlist.h (1), qlist.h (1), qlocale.h (1), qlocale_blackberry.h (1), qlocale_data_p.h (1), qlocale_p.h (1), qlocale_tools_p.h (1), qmap.h (1), qmargins.h (1), qmessageauthenticationcode.h (1), qpair.h (1), qpodlist_p.h (1), qpoint.h (1), qqueue.h (1), qrect.h (1), qrefcount.h (1), qregexp.h (1), qregularexpression.h (1), qringbuffer_p.h (1), qscopedpointer.h (1), qscopedpointer_p.h (1), qscopedvaluerollback.h (1), qset.h (1), qshareddata.h (1), qsharedpointer.h (1), qsharedpointer_impl.h (1), qsimd_p.h (1), qsize.h (1), qstack.h (1), qstring.h (1), qstringbuilder.h (1), qstringiterator_p.h (1), qstringlist.h (1), qstringmatcher.h (1), qtextboundaryfinder.h (1), qtimeline.h (1), qtimezone.h (1), qtimezoneprivate_data_p.h (1), qtimezoneprivate_p.h (1), qtools_p.h (1), qunicodetables_p.h (1), qunicodetools_p.h (1), qvarlengtharray.h (1), qvector.h (1) }
| QtCore: created fwd-include header(s) for <srcbase>/src/corelib/xml/ { qxmlstream.h (1), qxmlstream_p.h (1), qxmlutils_p.h (1) }
| Running configuration tests...
| Determining architecture... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -g -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../qtbase-opensource-src-5.3.2/config.tests/arch -I. -o arch.o ../../../qtbase-opensource-src-5.3.2/config.tests/arch/arch.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -o arch arch.o
|     Found architecture in binary
| CFG_ARCH="arm"
| CFG_CPUFEATURES=" neon"
| Determining architecture... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -g -Wall -W -fPIE  -I/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/x86_64-linux/usr/lib/qt5/mkspecs/linux-oe-g++ -I../../../qtbase-opensource-src-5.3.2/config.tests/arch -I. -o arch.o ../../../qtbase-opensource-src-5.3.2/config.tests/arch/arch.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -o arch arch.o
|     Found architecture in binary
| CFG_HOST_ARCH="arm"
| CFG_HOST_CPUFEATURES=" neon"
| System architecture: 'arm'
| Host architecture: 'arm'
| C++11 auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -O2 -DLINUX=1 -DEGL_API_FB=1 -std=c++0x -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/common/c++11 -I. -o c++11.o ../../../../qtbase-opensource-src-5.3.2/config.tests/common/c++11/c++11.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o c++11 c++11.o
| C++11 enabled.
| floatmath auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -O2 -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/unix/floatmath -I. -o floatmath.o ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/floatmath/floatmath.cpp
| ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/floatmath/floatmath.cpp:44:14: warning: unused parameter 'argc' [-Wunused-parameter]
|  int main(int argc, char **argv)
|               ^
| ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/floatmath/floatmath.cpp:44:27: warning: unused parameter 'argv' [-Wunused-parameter]
|  int main(int argc, char **argv)
|                            ^
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o floatmath floatmath.o
| floatmath enabled.
| sse2 auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -msse2 -g -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/common/sse2 -I. -o sse2.o ../../../../qtbase-opensource-src-5.3.2/config.tests/common/sse2/sse2.cpp
| arm-poky-linux-gnueabi-g++: error: unrecognized command line option '-msse2'
| make: *** [sse2.o] Error 1
| sse2 disabled.
| D-Bus auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -O2 -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/unix/dbus -I/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include/dbus-1.0 -I/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib/dbus-1.0/include -I. -o dbus.o ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/dbus/dbus.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o dbus dbus.o   -ldbus-1
| D-Bus enabled.
| ICU auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -O2 -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/unix/icu -I. -o icu.o ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/icu/icu.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o icu icu.o   -licui18n -licuuc -licudata
| ICU enabled.
| PulseAudio auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -D_REENTRANT -O2 -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/unix/pulseaudio -I/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/include/glib-2.0 -I/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib/glib-2.0/include -I. -o pulseaudio.o ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/pulseaudio/pulseaudio.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o pulseaudio pulseaudio.o   -lpulse-mainloop-glib -lpulse -lglib-2.0
| PulseAudio enabled.
| OpenGL ES 2.x auto-detection... ()
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -c -pipe  -O2 -pipe -g -feliminate-unused-debug-types -fvisibility-inlines-hidden -O2 -DLINUX=1 -DEGL_API_FB=1 -Wall -W -fPIE  -I../../../../qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++ -I../../../../qtbase-opensource-src-5.3.2/config.tests/unix/opengles2 -I. -o opengles2.o ../../../../qtbase-opensource-src-5.3.2/config.tests/unix/opengles2/opengles2.cpp
| arm-poky-linux-gnueabi-g++  -march=armv7-a -marm -mthumb-interwork -mfloat-abi=hard -mfpu=neon -mtune=cortex-a9 --sysroot=/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo -Wl,-O1 -Wl,--hash-style=gnu -Wl,--as-needed -Wl,-O1 -o opengles2 opengles2.o   -lGLESv2 -lEGL -lGAL
| /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/sysroots/udooneo/usr/lib/libEGL.so: undefined reference to `wl_display_roundtrip_queue'
| collect2: error: ld returned 1 exit status
| make: *** [opengles2] Error 1
| OpenGL ES 2.x disabled.
| The OpenGL ES 2.0 functionality test failed!
|  You might need to modify the include and library search paths by editing
|  QMAKE_INCDIR_OPENGL_ES2, QMAKE_LIBDIR_OPENGL_ES2 and QMAKE_LIBS_OPENGL_ES2 in
|  /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/qtbase-opensource-src-5.3.2/mkspecs/linux-oe-g++.
| WARNING: exit code 1 from a shell command.
| ERROR: Function failed: do_configure (log file is located at /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/tmp/work/cortexa9hf-vfp-neon-mx6sx-poky-linux-gnueabi/qtbase/5.3.2-r0/temp/log.do_configure.11490)
Currently 1 running tasks (1099 of 4500):
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
[89A[J[1;31mERROR[0m: [31mTask 2110 (/var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/../meta-qt5/recipes-qt/qt5/qtbase_5.3.2.bb, do_configure) failed with exit code '1'[0m
Currently 1 running tasks (1099 of 4500):
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
[89A[JCurrently 1 running tasks (1100 of 4500):
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
[89A[JWaiting for 2 running tasks to finish:
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
1: wayland-1.5.0-r0 do_package (pid 12437)
[129A[JWaiting for 1 running tasks to finish:
0: qtdeclarative-5.3.2-r0 do_fetch (pid 10703)
[86A[JWaiting for 0 running tasks to finish:
[39A[J[1;29mNOTE[0m: [29mTasks Summary: Attempted 1100 tasks of which 0 didn't need to be rerun and 1 failed.[0m
Waiting for 0 running tasks to finish:

Summary: 1 task failed:
  /var/jenkins_home/workspace/GENIVI/build_yocto_udooneo/gdp-src-build/../meta-qt5/recipes-qt/qt5/qtbase_5.3.2.bb, do_configure
Summary: There were 3 WARNING messages shown.
Summary: There was 1 ERROR message shown, returning a non-zero exit code.
Build step 'Execute shell' marked build as failure
Stopping Docker container after build completion
Notifying upstream projects of job completion
Finished: FAILURE
```

<!-- EOF -->
