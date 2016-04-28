# easy-jenkins

Easily deploy a [Jenkins](https://jenkins-ci.org/) CI/CD infrastructure via [docker-machine](https://www.docker.com/docker-machine) and [docker-compose](https://www.docker.com/docker-compose).

Please see [CHANGELOG](CHANGELOG.md) for main changes since previous release.

I recently gave [a presentation about easy-jenkins](http://gmacario.github.io/images/easybuild-torinotech-2016-04-01.pdf) to explain the motivations behind this project.

### TL;DR

```
$ git clone https://github.com/gmacario/easy-jenkins
$ cd easy-jenkins
$ ./runme.sh
```

If the script executes successfully it will display a message like the following:

```
INFO: Now browse http://192.168.99.100:9080/ to access the Jenkins dashboard
```

The Jenkins dashboard may then be accessed by opening the displayed URL using a recent Internet browser.

The behavior of the `runme.sh` script may be customized through some environment variables - please refer to the comments inside the script for details.

You will also be reminded to use the following command

```
INFO: Run the following command to configure your shell:
INFO: eval $(docker-machine env easy-jenkins)
```

in order to setup the environment variables so that `docker-compose` and `docker` will interact with the correct Docker engine.

### System Requirements

In order to run easy-jenkins you need a recent 64-bit x86 host with: 

1. Minimum HW requirements: a dual-core CPU, 8 GB RAM, 100 GB disk space
2. [Docker](https://www.docker.com/) tools (see Note 1)
   * Docker Engine (see Note 2)
   * Docker Compose
   * Docker Machine
5. A recent Internet browser (i.e. [Google Chrome](https://www.google.com/chrome/))
6. A fast Internet connection

**Note 1**: By installing [Docker Toolbox](https://www.docker.com/products/docker-toolbox) (either on [OS X](http://www.apple.com/osx/) or [MS Windows](http://www.microsoft.com/en-us/windows)) you will get all the Docker tools (i.e. docker, docker-compose, docker-machine, etc.) required by easy-jenkins.

**Note 2**: Thanks to docker-machine you can configure easy-jenkins to deploy and run the containers on a remote Docker engine, for instance:

1. A fast, multi-core server on your local network
2. An instance on a public cloud, such as [Amazon EC2](https://aws.amazon.com/it/ec2/), [DigitalOcean](https://www.digitalocean.com/), etc.

### What can I do next?

Basically whatever you can do with Jenkins. An excellent book is [Jenkins: The Definitive Guide][1].

Under the [docs/][2] directory you can also find some examples about how to use easy-jenkins.

[1]: http://www.wakaleo.com/books/jenkins-the-definitive-guide
[2]: docs

### License

easy-jenkins is licensed under the MIT License - for details please see the `LICENSE` file.

Copyright 2016, [Gianpaolo Macario](http://gmacario.github.io/)
