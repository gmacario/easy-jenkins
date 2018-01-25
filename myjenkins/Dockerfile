# Project: gmacario/easy-jenkins
#
# Extend the standard Jenkins LTS Docker image with:
# - Jenkins workflow plugins
# - Docker
#
# Credits to https://github.com/camiloribeiro/cdeasy
# - https://github.com/camiloribeiro/cdeasy/blob/master/docker/jenkins/Dockerfile

FROM jenkins/jenkins:lts

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
    ant \
    antisamy-markup-formatter \
    blueocean \
    blueocean-pipeline-editor \
    build-timeout \
    command-launcher \
    delivery-pipeline-plugin \
    docker-build-publish \
    docker-custom-build-environment \
    docker-plugin \
    email-ext \
    ez-templates \
    ghprb \
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
    jobConfigHistory \
    join \
    ldap \
    mailer \
    mapdb-api \
    matrix-auth \
    matrix-project \
    mercurial \
    mock-slave \
    pam-auth \
    pipeline-github-lib \
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
    ws-cleanup

#    build-flow-test-aggregator:1.2 \
#    scriptler:2.9 \

COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

RUN touch /var/run/docker.sock

USER jenkins

# EOF
