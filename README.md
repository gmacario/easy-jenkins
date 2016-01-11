# easy-jenkins

Easily deploy a Jenkins CI/CD infrastructure via docker-compose

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

Then open <http://192.168.99.100:9080> from the Internet browser of your choice to access the Jenkins dashboard.

You may need to adjust the IP address in the URL above based on the actual URL of your Docker host, which may be displayed with the following command:

```
$ docker-machine url easy-jenkins
```

### License

easy-jenkins is licensed under the MIT License - for details please see the `LICENSE` file.

Copyright 2016, [Gianpaolo Macario](http://gmacario.github.io/)
