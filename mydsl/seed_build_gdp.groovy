/* 
  Project: https://github.com/gmacario/easy-jenkins
  File:    mydsl/build_gdp.groovy
*/
  
/*
Usage:

Browse `${JENKINS_URL}`, then click **New Item**

* Item Name: `seed_build_gdp`
* Type: Freestyle project

then click **OK**.

Browse `${JENKINS_URL}/job/seed_build_gdp/configure` and change

* Build
  - Add build step > Process Job DSLs
    - Use the provided DSL script: Yes
      - DSL Script: <paste this file>
  
then click **Save**.

Browse `${JENKINS_URL}/job/seed_build_gdp/`, then click **Build Now**

Result: Project `build_gdp` will be listed in `${JENKINS_URL}`
*/

def jobName = "build_gdp"
def gitUrl = "https://github.com/gmacario/genivi-demo-platform"
def gitBranch = "qemux86-64"

job(jobName) {
  label('yocto')
  scm {
    git(gitUrl, gitBranch) {
      // TODO
    }
  }
  steps {
    shell "id"
    shell "printenv"
    shell "ps axf"
    shell "bash -xec \"source init.sh && bitbake genivi-demo-platform\""
  }
}

// EOF
