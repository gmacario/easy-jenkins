# Project: gmacario/easy-jenkins
#
# Extend the standard Jenkins LTS Docker image with:
# - Jenkins workflow plugins
# - Docker
#
# Based on https://github.com/camiloribeiro/cdeasy

FROM jenkins/jenkins
# FROM jenkins/jenkins:lts
# FROM jenkins/jenkins-experimental:latest-jdk10-incrementals

USER root
RUN apt-get update -qq && \
    apt-cache search groovy && \
    apt-get install -qqy \
    apt-transport-https \
    ca-certificates \
    curl \
    wget \
    lxc \
    iptables \
    dos2unix \
    groovy

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
    "https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m)" && \
    chmod +x /usr/local/bin/docker-compose

# Workaround to prevent install-plugins.sh to fail
RUN mkdir -p /usr/share/jenkins/ref/plugins/tmp

ENV JENKINS_UC_EXPERIMENTAL=https://updates.jenkins.io/experimental

# Install additional Jenkins plugins
RUN install-plugins.sh \
    ansicolor \
    ant \
    antisamy-markup-formatter \
    blueocean \
    blueocean-pipeline-editor \
    build-timeout \
    command-launcher \
    configuration-as-code \
    configuration-as-code-support \
    delivery-pipeline-plugin \
    docker-build-publish \
    docker-custom-build-environment \
    docker-plugin \
    email-ext \
    ez-templates \
    git \
    git-client \
    github-branch-source \
    github-oauth \
    github-organization-folder \
    github-pullrequest \
    gradle \
    greenballs \
    groovy \
    htmlpublisher \
    jenkins-multijob-plugin \
    job-dsl \
    jobConfigHistory \
    join \
    kubernetes \
    kubernetes-cli \
    ldap \
    mailer \
    mapdb-api \
    matrix-auth \
    matrix-project \
    mercurial \
    mock-slave \
    pam-auth \
    pipeline-github-lib \
    publish-over-ssh \
    resource-disposer \
    run-condition \
    sectioned-view \
    ssh-slaves \
    subversion \
    timestamper \
    translation \
    view-job-filters \
    windows-slaves \
    workflow-aggregator \
    workflow-job \
    workflow-support \
    ws-cleanup

#    "workflow-support:incrementals;org.jenkins-ci.plugins.workflow;2.19-rc295.e017dc58c0a3" \

COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

RUN touch /var/run/docker.sock

USER jenkins

# EOF
