# Project: gmacario/easy-jenkins
#
# Extend the standard Jenkins LTS Docker image with:
# - Jenkins workflow plugins
# - Docker
#
# Credits to https://github.com/camiloribeiro/cdeasy
# - https://github.com/camiloribeiro/cdeasy/blob/master/docker/jenkins/Dockerfile

FROM jenkins:2.46.2

USER root
RUN apt-get update -qq && apt-get install -qqy \
    apt-transport-https \
    ca-certificates \
    curl \
    wget \
    lxc \
    iptables \
    dos2unix \
    groovy2

# Install gosu
RUN curl -o /usr/local/bin/gosu -fsSL \
    "https://github.com/tianon/gosu/releases/download/1.7/gosu-$(dpkg --print-architecture)" && \
    chmod +x /usr/local/bin/gosu

# Install Docker
RUN wget -qO- https://get.docker.com/ | sh
RUN usermod -aG docker jenkins

# Add user "jenkins" to group "docker" of the Docker host
# (on boot2docker, group "docker" has gid=100)
RUN usermod -aG 100 jenkins

# Install docker-compose
RUN curl -o /usr/local/bin/docker-compose -fsSL \
    "https://github.com/docker/compose/releases/download/1.6.2/docker-compose-$(uname -s)-$(uname -m)" && \
    chmod +x /usr/local/bin/docker-compose

# Workaround to prevent install-plugins.sh to fail
RUN mkdir -p /usr/share/jenkins/ref/plugins/tmp

# Install additional Jenkins plugins
RUN install-plugins.sh \
    ant:1.5 \
    antisamy-markup-formatter:1.5 \
    blueocean:1.0.1 \
    blueocean-pipeline-editor:0.2.0 \
    delivery-pipeline-plugin:1.0.1 \
    docker-build-publish:1.3.2 \
    docker-custom-build-environment:1.6.5 \
    docker-plugin:0.16.2 \
    email-ext:2.57.2 \
    ez-templates:1.2.5 \
    ghprb:1.36.2 \
    git-client:2.4.5 \
    github-oauth:0.27 \
    github-organization-folder:1.6 \
    github-pullrequest:0.1.0-rc24 \
    greenballs:1.15 \
    groovy:2.0 \
    htmlpublisher:1.13 \
    jenkins-multijob-plugin:1.24 \
    jobConfigHistory:2.16 \
    join:1.21 \
    ldap:1.15 \
    mailer:1.20 \
    mapdb-api:1.0.9.0 \
    matrix-auth:1.6 \
    matrix-project:1.11 \
    mercurial:1.60 \
    mock-slave:1.10 \
    pam-auth:1.3 \
    run-condition:1.0 \
    sectioned-view:1.20 \
    ssh-slaves:1.17 \
    translation:1.15 \
    view-job-filters:1.27 \
    windows-slaves:1.3.1 \
    workflow-aggregator:2.5 \
    workflow-job:2.11

#    build-flow-test-aggregator:1.2 \
#    scriptler:2.9 \

COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

RUN touch /var/run/docker.sock

USER jenkins

# EOF
