/* 
Usage:

Browse ${JENKINS_URL}/view/All/newJob

* Item Name: seed-configure-git
* Type: Freestyle project

Browse ${JENKINS_URL}/job/seed_configure_git/configure

* Restrict where this project can be run
  - Label Expression: master
  
* Build > Add build step
  - Process Job DSLs
  - DSL Script: <paste those contents>
  
Browse ${JENKINS_URL}/job/seed_configure_git/build?delay=0sec

*/

def jobName = "configure_git"

job(jobName) {
  
  // Warning: (script, line 26) plugin 'nodelabelparameter' needs to be installed
  /* parameters {
        nodeParam('build-yocto-slave') {
            description('select test host')
            defaultNodes(['node1'])
            allowedNodes(['node1', 'node2', 'node3'])
            trigger('multiSelectionDisallowed')
            eligibility('IgnoreOfflineNodeEligibility')
        }
  } */
    
  steps {
    shell "id"
    shell "printenv"
    shell "ps axf"
    shell "git config -l"
    shell "echo \$(whoami)"
    shell "echo \$(hostname)"
    shell "git config --global user.name \"\$(whoami)\""
    shell "git config --global user.email \"\$(whoami)@\$(hostname)\""
  }
}
