# Preparing easy-jenkins demo, 2018-06-22

See <https://github.com/gmacario/easy-jenkins/issues/268>

YouTube Video: <https://www.youtube.com/watch?v=1ZX9qA0AZjM>

## Prerequisites

Please refer to section "System Requirements" of <https://github.com/gmacario/easy-jenkins>

## Demo Storyboard

Login to a Bash shell on the machine where you installed Docker (in our example, login as _gmacario@mac-tizy_)

### Deploy easy-jenkins inside a VirtualBox VM

Clone easy-jenkins source code from GitHub

```shell
mkdir -p $HOME/github/gmacario && cd $HOME/github/gmacario && \
[ ! -e easy-jenkins ] && git clone https://github.com/gmacario/easy-jenkins && \
cd $HOME/github/gmacario/easy-jenkins && \
git checkout wip/try-java10-jasc && git pull --all --prune
```

Bring-up easy-jenkins

```shell
cd $HOME/github/gmacario/easy-jenkins && \
./runme.sh
```

If everything is OK, you should get an output similar to the following:

```
...
Creating easy-jenkins_myjenkins_1 ... done
INFO: Run the following command to configure your shell:
INFO: eval $(docker-machine env easy-jenkins)
INFO: Browse http://192.168.99.100:9080/ to access the Jenkins dashboard
INFO: Initial administrator password: c253570cfcec43b79e92f47008dc0ac0
gmacario@mac-tizy:~/github/gmacario/easy-jenkins (wip/try-java10-jcasc)$
```

**NOTE**: Take note of the displayed URL and initial administrator password (they may differ from the ones shown here) since you will need them later on.

(Optional) Check docker-compose logs

```shell
eval $(docker-machine env easy-jenkins) && \
docker-compose logs -f
```

#### Configure easy-jenkins instance

Browse `${JENKINS_URL}` -- example: <http://192.168.99.100:9080/>

* On page "Unlock Jenkins", paste the Jenkins initial administrator password (see output of `./runme.sh` or type `docker-compose logs`), then click "Continue"
* On page "Getting Started", click "Install suggested plugins"
* On page "Create First Admin User", fill in the requested details, then click "Save and Continue"
* On page "Instance Configuration", accept the proposed value for Jenkins URL, then click "Save and Finish"
* If requested, click button "Restart" and refresh the browser until you get the "Welcome to Jenkins!" login page
* On page "Welcome to Jenkins!", login with the credentials of the first admin user you have created, then click "Sign in"
* On the main Jenkins dashboard page, click "Build Executor Status", then click the "Configure" icon of node "master" and fill in the following data
  - Labels: `docker`
  - Click "Save"


#### Install sample Pipelines (my-jenkins-pipelines)

Browse `${JENKINS_URL}`

* Click "Open Blue Ocean"
* Click "Create a new Pipeline"
* Where do you store your code? GitHub
* Connect to GitHub: Create an access token
  - On page "New personal access token" fill in the following data:
    - Token description: Choose a meaningful name (example: `easy-jenkins @mac-tizy`)
    - Click "Generate token"
  - Copy the access token and paste it to the Pipeline setup page, then click "Connect"
* Which organization does the repository belong to? An organization you belong to (example: `gmacario`)
* Choose a repository: my-jenkins-pipelines (you may fork it from <https://github.com/gmacario/my-jenkins-pipelines>)
* Click "Create Pipeline"

Result: TODO

### Tear-down

```shell
cd $HOME/github/gmacario/easy-jenkins && \
eval $(docker-machine env easy-jenkins) && \
docker-compose down -v && \
docker-machine stop easy-jenkins
```

### If the storyboard fails

Logged as gmacario@mac-tizy

### Demo easy-jenkins@gmpowerhorse

```shell
cd && \ 
source $HOME/.bashrc && \
gmhome-tunnel.sh
```

(Optional) Login to remote Docker Host with the following command

```shell
ssh -p 20022 gmacario@localhost
```

* Browse <http://localhost:29080>
* Click "Open Blue Ocean"
* Inspect pipeline "my-jenkins-pipelines"
* Inspect pipeline "my-genivi-pipelines"
* Inspect pipeline "easy-jenkins"

### Demo easy-jenkins@cc-vm1

NOTE: Currently on easy-jenkins master

* Browse <http://cc-vm1.solarma.it:9080>
* Click "Open Blue Ocean"
* Inspect pipeline "my-jenkins-pipelines"
* Inspect pipeline "easy-jenkins"

(Optional) Login to remote Docker Host with the following command

```shell
ssh -p root@cc-vm1.solarma.it
```

<!-- EOF -->
