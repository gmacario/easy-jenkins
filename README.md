# easy-jenkins

Easily deploy a [Jenkins](https://jenkins-ci.org/) CI/CD infrastructure via [docker-machine](https://www.docker.com/docker-machine) and [docker-compose](https://www.docker.com/docker-compose).

### System Requirements

* Docker and docker-compose (tested with Docker Toolbox 1.9.1d)
* At least 8MB RAM and 100 GB disk space
* A fast Internet connection
* A recent Internet browser

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

You may also use the following command

```
$ docker-machine ls
$ eval $(docker-machine env easy-jenkins)
```

to setup the environment variables so that `docker-compose` and `docker` will interact with the created VM.

### What I can do next?

Basically whatever you can do with Jenkins. An excellent book is [Jenkins: The Definitive Guide][1].

Under the [docs/][2] directory you can also find some examples about how to use easy-jenkins.

[1]: http://www.wakaleo.com/books/jenkins-the-definitive-guide
[2]: docs

### License

easy-jenkins is licensed under the MIT License - for details please see the `LICENSE` file.

Copyright 2016, [Gianpaolo Macario](http://gmacario.github.io/)
