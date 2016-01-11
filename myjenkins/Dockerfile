# Project: gmacario/easy-jenkins
#
# Extend the standard Jenkins LTS Docker image with:
# - Jenkins workflow plugins
# - Docker
#
# Credits to https://github.com/camiloribeiro/cdeasy
# - https://github.com/camiloribeiro/cdeasy/blob/master/docker/jenkins/Dockerfile
#
FROM jenkins:latest

MAINTAINER Gianpaolo Macario <gmacario@gmail.com>

USER root
RUN apt-get update -qq && apt-get install -qqy \
    apt-transport-https \
    ca-certificates \
    curl \
    wget \
    lxc \
    iptables \
    dos2unix

# Install Docker
RUN wget -qO- https://get.docker.com/ | sh

# Install docker-compose
RUN curl -L https://github.com/docker/compose/releases/download/1.5.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

# Install Jenkins plugins from the list at plugins.txt
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN dos2unix /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

RUN touch /var/run/docker.sock

RUN usermod -aG docker jenkins

# COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

COPY start.sh /tmp/start.sh
RUN chmod +x /tmp/start.sh && dos2unix /tmp/start.sh

ENTRYPOINT ["/bin/bash", "-c", "/tmp/start.sh"]

# EOF
