/* 
  Project: https://github.com/gmacario/easy-jenkins
  File:    mydsl/build_gdp.groovy
*/
  
/*
Usage:

Browse `${JENKINS_URL}/view/All/newJob`, then click **New Item**

* Item Name: `build_gdp`
* Type: Freestyle project

then click **OK**.

Browse `${JENKINS_URL}/job/build_gdp/configure`

* Restrict where this project can be run: Yes
  - Label Expression: `yocto`
  
* Build > Add build step
  - Process Job DSLs
  - DSL Script: <paste these file>
  
then click **Save**.

Browse `${JENKINS_URL}/job/build_gdp/build?delay=0sec`
*/

def jobName = "build_gdp"
def gitUrl = "https://github.com/gmacario/genivi-demo-platform"
def gitBranch = "qemux86-64"

job(jobName) {
  // TODO
  // * Restrict where this project can be run
  //   - Label Expression: master
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
