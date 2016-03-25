# ===========================================================================================
# Project: gmacario/easy-jenkins
#
# File: myjenkins/Dockerfile
#
# Purpose: Extend the standard Jenkins LTS Docker image with:
#   - docker
#   - docker-compose
#   - gosu
#   - Additional Jenkins plugins (specified in plugins.txt)
#
# Credits to https://github.com/camiloribeiro/cdeasy
# ===========================================================================================
FROM jenkins:latest

MAINTAINER Gianpaolo Macario <gmacario@gmail.com>

USER root
RUN apt-get update -qq && apt-get install -qqy \
    apt-transport-https \
    ca-certificates \
    curl \
    dos2unix \
    groovy2 \
    iptables \
    lxc \
    wget

# Install gosu
RUN curl -o /usr/local/bin/gosu -fsSL \
    "https://github.com/tianon/gosu/releases/download/1.7/gosu-$(dpkg --print-architecture)" && \
    chmod +x /usr/local/bin/gosu

# Install Docker
RUN curl -fsSL https://get.docker.com/ | sh
RUN usermod -aG docker jenkins

# Workaround until user-namespaces are working
# Add user "jenkins" to group "docker" of host (works with boot2docker)
RUN usermod -aG 100 jenkins

# Install docker-compose
RUN curl -o /usr/local/bin/docker-compose -fsSL \
    "https://github.com/docker/compose/releases/download/1.6.2/docker-compose-$(uname -s)-$(uname -m)" && \
    chmod +x /usr/local/bin/docker-compose

# Install Jenkins plugins from the list at plugins.txt
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN dos2unix /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

COPY start.sh /tmp/start.sh
RUN chmod +x /tmp/start.sh && dos2unix /tmp/start.sh

COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

RUN touch /var/run/docker.sock

USER root
ENTRYPOINT ["/bin/bash", "-c", "/tmp/start.sh"]

# EOF
