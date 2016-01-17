/* 
Usage:

Browse ${JENKINS_URL}/view/All/newJob

* Item Name: seed-build-gdp
* Type: Freestyle project

Browse ${JENKINS_URL}/job/seed-build-gdp/configure

* Restrict where this project can be run
  - Label Expression: master
  
* Build > Add build step
  - Process Job DSLs
  - DSL Script: <paste those contents>
  
Browse ${JENKINS_URL}/job/seed-build-gdp/build?delay=0sec

*/

def jobName = "build-gdp"
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
