# Project: gmacario/easy-jenkins
#
# Extend the standard Jenkins LTS Docker image with:
# - Jenkins workflow plugins
# - Docker
#
# Credits to https://github.com/camiloribeiro/cdeasy
# - https://github.com/camiloribeiro/cdeasy/blob/master/docker/jenkins/Dockerfile

FROM jenkins:2.32.2
MAINTAINER Gianpaolo Macario <gmacario@gmail.com>

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

# Install docker-compose
RUN curl -o /usr/local/bin/docker-compose -fsSL \
    "https://github.com/docker/compose/releases/download/1.6.2/docker-compose-$(uname -s)-$(uname -m)" && \
    chmod +x /usr/local/bin/docker-compose

# Install additional Jenkins plugins
RUN install-plugins.sh \
    ant:1.4 \
    antisamy-markup-formatter:1.5 \
    build-flow-test-aggregator:1.2 \
    delivery-pipeline-plugin:0.10.2 \
    docker-build-publish:1.3.2 \
    docker-custom-build-environment:1.6.5 \
    docker-plugin:0.16.2 \
    ez-templates:1.2.5 \
    ghprb:1.35.0 \
    github-oauth:0.25 \
    github-organization-folder:1.6 \
    github-pullrequest:0.1.0-rc20 \
    greenballs:1.15 \
    groovy:1.30 \
    htmlpublisher:1.12 \
    jenkins-multijob-plugin:1.23 \
    jobConfigHistory:2.15 \
    join:1.21 \
    ldap:1.14 \
    mapdb-api:1.0.9.0 \
    matrix-auth:1.4 \
    mercurial:1.59 \
    mock-slave:1.10 \
    pam-auth:1.3 \
    run-condition:1.0 \
    scriptler:2.9 \
    sectioned-view:1.20 \
    translation:1.15 \
    view-job-filters:1.27 \
    windows-slaves:1.2 \
    workflow-aggregator:2.5 \

#    ace-editor:1.1 \
#    authentication-tokens:1.3 \
#    bouncycastle-api:2.16.0 \
#    branch-api:2.0.6 \
#    build-flow-plugin:0.20 \
#    build-pipeline-plugin:1.5.6 \
#    cloudbees-folder:5.17 \
#    conditional-buildstep:1.3.5 \
#    credentials:2.1.11 \
#    credentials-binding:1.10 \
#    cvs:2.13 \
#    dashboard-view:2.9.10 \
#    display-url-api:1.1.1 \
#    docker-commons:1.6 \
#    docker-workflow:1.10 \
#    durable-task:1.13 \
#    envinject:1.93.1 \
#    git:3.0.5 \
#    git-client:2.2.1 \
#    git-server:1.7 \
#    github:1.26.0 \
#    github-api:1.84 \
#    github-branch-source:2.0.3 \
#    handlebars:1.1.1 \
#    icon-shim:2.0.3 \
#    javadoc:1.4 \
#    job-dsl:1.57 \
#    jquery:1.11.2-0 \
#    jquery-detached:1.2.1 \
#    junit:1.20 \
#    mailer:1.19 \
#    matrix-project:1.8 \
#    maven-plugin:2.14 \
#    momentjs:1.1.1 \
#    multiple-scms:0.6 \
#    parameterized-trigger:2.32 \
#    pipeline-build-step:2.4 \
#    pipeline-github-lib:1.0 \
#    pipeline-graph-analysis:1.3 \
#    pipeline-input-step:2.5 \
#    pipeline-milestone-step:1.3 \
#    pipeline-model-api:1.0.1 \
#    pipeline-model-declarative-agent:1.0.1 \
#    pipeline-model-definition:1.0.1 \
#    pipeline-rest-api:2.5 \
#    pipeline-stage-step:2.2 \
#    pipeline-stage-tags-metadata:1.0.1 \
#    pipeline-stage-view:2.5 \
#    plain-credentials:1.3 \
#    promoted-builds:2.28.1 \
#    scm-api:2.0.4 \
#    script-security:1.26 \
#    ssh-agent:1.14 \
#    ssh-credentials:1.13 \
#    ssh-slaves:1.13 \
#    structs:1.6 \
#    subversion:2.7.1 \
#    token-macro:2.0 \
#    workflow-api:2.11 \
#    workflow-basic-steps:2.4 \
#    workflow-cps:2.27 \
#    workflow-cps-global-lib:2.6 \
#    workflow-durable-task-step:2.9 \
#    workflow-job:2.10 \
#    workflow-multibranch:2.12 \
#    workflow-scm-step:2.3 \
#    workflow-step-api:2.9 \
#    workflow-support:2.13

COPY start.sh /tmp/start.sh
RUN chmod +x /tmp/start.sh && dos2unix /tmp/start.sh

COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

RUN touch /var/run/docker.sock

USER root
ENTRYPOINT ["/bin/bash", "-c", "/tmp/start.sh"]

# EOF
