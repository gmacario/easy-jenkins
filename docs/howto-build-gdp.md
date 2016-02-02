### Introduction

<!-- (2016-02-02 07:58 CET) -->

Instructions for building from sources my fork of the [GENIVI Demo Platform](http://projects.genivi.org/genivi-demo-platform/home).

This is mainly used as a regression test suite for the [gmacario/easy-jenkins](https://github.com/gmacario/easy-jenkins) project.

The following instructions were tested on
* Client: itm-gmacario-w7 (MS Windows 7 64-bit, Docker Toolbox 1.9.1i)
* Server: dc7600-gm (Ubuntu 14.04.3 LTS 64-bit, Docker Engine 1.9.1)

### Preparation

Deploy master branch of [gmacario/easy-jenkins](https://github.com/gmacario/easy-jenkins) to docker-machine `dc7600-gm`

```
$ git clone https://github.com/gmacario/easy-jenkins
$ cd ~/easy-jenkins
$ eval $(docker-machine env dc7600-gm)
$ docker-compose stop; docker-compose rm -f; docker-compose build --pull
$ ./runme.sh
```

(Optional) Watch docker-compose logs until line `INFO: Jenkins is fully up and running` is displayed:

```
$ docker-compose logs
...
myjenkins_1         | INFO: Completed initialization
myjenkins_1         | Feb 02, 2016 7:05:31 AM hudson.WebAppMain$3 run
myjenkins_1         | INFO: Jenkins is fully up and running
...
```

Browse `${JENKINS_URL}` at <http://dc7600-gm.solarma.it:9080/> and verify that the Jenkins dashboard is displayed correctly.

Browse `${JENKINS_URL}/job/seed`, then click **Build Now**

Result: The following items will be generated and show up in the Jenkins dashboard:

1. add_jenkins_slave
2. build_gdp
3. configure_git

### Configure git on Jenkins node `master`

<!-- (2016-02-02 08:08 CET) -->

Workaround for [issues/26](https://github.com/gmacario/easy-jenkins/issues/26)

Browse `${JENKINS_URL}/job/configure_git/`, then click **Build Now**

Verify in the Console Output that the job was run on the master node (at this point there should not be any slave nodes yet)

### Create Jenkins node `build-yocto-slave`

<!-- (2016-02-02 08:10 CET) -->

Workaround for [issues/16](https://github.com/gmacario/easy-jenkins/issues/16)

Browse `${JENKINS_URL}/job/add_jenkins_slave/` then click **Build with Parameters**

- AgentList: `build-yocto-slave`
- AgentDescription: `Auto-created Jenkins agent`
- AgentHome: `/home/jenkins`
- AgentExecutors: `2`

TODO: Should add Text parameter `AgentLabels` - tracked as [issues/23](https://github.com/gmacario/easy-jenkins/issues/23)

then click **Build**

Result: SUCCESS

Browse `${JENKINS_URL}`, verify that node `build-yocto-slave` is running.

### Configure labels on Jenkins node `build-yocto-slave`

<!-- (2016-02-02 08:11 CET) -->

Workaround for [issues/23](https://github.com/gmacario/easy-jenkins/issues/23)

Browse `${JENKINS_URL}/computer/build-yocto-slave/`, then click **Configure**

- Labels: `yocto`

Then click **Save**.

### Configure git on Jenkins node `build-yocto-slave`

<!-- (2016-02-02 08:14 CET) -->

Workaround for [issues/27](https://github.com/gmacario/easy-jenkins/issues/27)

Browse `${JENKINS_URL}/job/configure_git/`, then click **Configure**

- Restrict where this project can be run: Yes
  - Label Expression: `build-yocto-slave`

Click **Save**, then click **Build Now**

Verify in the Console Output that the job was run on the slave.

### Configure project `build_gdp`

<!-- (2016-02-02 08:15 CET) -->

Workaround for [issues/25](https://github.com/gmacario/easy-jenkins/issues/25)

Browse `${JENKINS_URL}/job/build_gdp/`, then click **Configure**

- Restrict where this project can be run: Yes
  - Label Expression: `yocto`

Then click **Save**.

### Build project `build_gdp`

<!-- (2016-02-02 08:18 CET) -->

Browse `${JENKINS_URL}/job/build_gdp/`, then click **Build Now**

Result: TODO

<!-- EOF -->
