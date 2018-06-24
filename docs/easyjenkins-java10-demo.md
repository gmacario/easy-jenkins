# Preparing easy-jenkins demo, 2018-06-22

See <https://github.com/gmacario/easy-jenkins/issues/268>

YouTube Video: <https://www.youtube.com/watch?v=1ZX9qA0AZjM>

## Demo Storyboard

Logged as gmacario@mac-tizy

### Set-up

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

(Optional) Check logs

```shell
eval $(docker-machine env easy-jenkins) && \
docker-compose logs -f
```

#### Configure easy-jenkins instance

Browse `${JENKINS_URL}` -- example: <http://192.168.99.103:9080/>

* Paste initial Administrator password (from `docker-compose logs`), then click "Continue"
* Click "Install suggested plugins"
* On page "Create First Admin User", fill in details, then click "Save and Continue"
* On page "Instance Configuration", accept proposed value for Jenkins URL, then click "Save and Finish"
* Click "Start using Jenkins"
* If requested, click "Restart" and refresh browser page until you get the "Welcome to Jenkins!" login page
* On page "Welcome to Jenkins!", login with the credentials of the first admin user you have created, then click "Sign in"
* Click "Build Executor Status", then select "master" > Configure
  - Labels: `docker`
  - Click "Save"


#### Install sample Pipelines (my-jenkins-pipelines)

Browse `${JENKINS_URL}`

* Click "Open Blue Ocean"
* Click "Create a new Pipeline"
* Where do you store your code? GitHub
* Connect to Github: Create an access key
  - Token description: easy-jenkins @mac-tizy
  - Click "Generate token"
  - Paste access token, then click "Connect"
* Which organization does the repository belong to? gmacario
* Choose a repository: my-jenkins-pipelines
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
