# Configure Kubernetes slaves on easy-jenkins

**WORK-IN-PROGRESS*

### Introduction

This document explains how to configure easy-enkins to connect to a Kubernetes cluster.

Tested on cc-vm1.solarma.it (Ubuntu 16.04.x LTS 64-bit) and Kubernetes cluster "kube-101" on GCP.

### References

* <https://cloud.google.com/solutions/configuring-jenkins-kubernetes-engine>

* <https://github.com/jenkinsci/kubernetes-plugin/blob/master/README.md> sections
  - Running locally with minikube
  - Running in Google Container Engine GKE

### Step-by-step instructions

#### Connect to the Kubernetes cluster on GCP

Browse <https://console.cloud.google.com> > Kubernetes Engine > Clusters

* Select cluster "kube-101"
  - In tab "Details", section "Cluster", take note of Endpoint (public IP address)
  - Click "Show credentials"
  - Click "Connect"

Alternatively, install the Google Cloud SDK and type the following commands

```shell
gcloud init
gcloud container clusters list
gcloud container clusters describe kube-101 \
  --zone europe-west1-b --project kubernetes-workshop-218213
gcloud container clusters get-credentials kube-101 \
  --zone europe-west1-b --project kubernetes-workshop-218213
```

#### Create namespace on Kubernetes cluster

Inspect the Kubernetes cluster and create a new namespace "easy-jenkins"

```shell
kubectl config view
kubectl cluster-info
kubectl get namespaces
kubectl create namespace easy-jenkins
```

Jenkins > Manage Jenkins > Configure System

* Cloud > Add a new cloud > Kubernetes
  - Name: `kube-101` (from GCP console > Clusters)
  - Kubernetes URL: <https://35.241.146.224> (paste from GCP console: Cluster Details > Endpoint)
  - Kubernetes server certificate key: (paste from GCP console: Cluster credentials > Cluster CA certificate)
  - Disable https certificate check: No
  - Kubernetes Namespace: `easy-jenkins`
  - Credentials > Add > Jenkins
    - Domain: Global credentials (unrestricted)
    - Kind: Username with password
      - Scope: Global (Jenkins, nodes, items, all child items, etc)
      - Username: `admin` (paste from GCP console: Cluster credentials > Username)
      - Password: (paste from GCP console: Cluster credentials > Password)
      - ID: (empty)
      - Description: (empty)
    - Click "Add"

Click "Test Connection" and make sure that

> Connection test successful

Continue configuring Kubernetes Cloud "kube-101"

- Jenkins URL: <http://cc-vm1.solarma.it:9080>
- Jenkins tunnel: (empty)
- Connection Timeout: 0 (leave default)
- Read Timeout: 0 (leave default)
- Container Cap: 10 (leave default)
- Pod Retention: Never
- Images > Kubernetes Pod Template
  - Name: `jnlp slave`
  - Namespace: (emtpy)
  - Labels: (empty)
  - Usage: Use this node as much as possible (TODO)
  - The name of the pod template to inherit from: (empty)
  - Containers > Add > Container Template
    - Name: (empty)
    - Docker image: `jenkins/jnlp-slave`
    - Always pull image: No
    - Working directory: `/home/jenkins`
    - Command to run: `/bin/sh -c`
    - Arguments to pass to the command: `cat`
    - Allocate pseudo-TTY: Yes
    - EnvVars: (none)
  - EnvVars: (none)
  - Volumes: (none)
  - Max number of instances: (empty)
  - Pod Retention: Default
  - Time in minutes to retain slave when idle: (empty)
  - Time in seconds for Pod deadline: (empty)
  - Time in seconds for Jenkins connection: 100 (default)
  - Annotations: (none)
  - Raw yaml for the Pod: (empty)

then click "Save"

### Execute a sample pipeline on kube-101

TODO


Test:

Jenkins > Open Blue Ocean > New Pipeline

* Where do you store your code? GitHub
* Which organization does the repository belong to? gmacario
* Choose a repository: test-jenkins-kubernetes-plugin

The multibranch pipeline is loaded and started, however the build of branch "feature/kube-declarative-pipeline" never starts

![screenshot from 2018-10-05 15-04-22](https://user-images.githubusercontent.com/75182/46536832-f892c380-c8af-11e8-8f53-172d5d2d54ac.png)

This situation can also be detected by looking at the Kubernetes dashboard of cluster "kube-101", namespace "easy-jenkins":

![screenshot from 2018-10-05 15-09-12](https://user-images.githubusercontent.com/75182/46537099-be75f180-c8b0-11e8-9c62-c26fcc2300eb.png)

It looks look like that the Pods `mypod-kr0c7-xxxx` running inside Kubernetes cluster "kube-101" on GCP are unable to connect to our Jenkins master, since the URL <http://nemo.gmacario.it:9080> is behind a corporate firewall.

Closing issue, will reopen when we able to test both Jenkins master as well as the slaves deployed on the same Kubernetes cluster, or at least with some capability of communicating to each other. 


<!-- EOF -->
