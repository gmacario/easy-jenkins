/*
  Project: https://github.com/gmacario/easy-jenkins
  File:    myjenkins/seed.groovy
  
  Adapted from https://github.com/gmacario/cdeasy/blob/master/docker/jenkins/seed.groovy
  
  To test the script:
  - Browse ${JENKINS_URL}/script to access the Jenkins Script Console
  - Paste the contents of this file
  - Click "Run"
*/

import jenkins.model.*;
import hudson.model.FreeStyleProject;
import hudson.plugins.git.GitSCM;
import hudson.tasks.Shell;
import javaposse.jobdsl.plugin.*;

def url = "https://github.com/gmacario/easy-jenkins.git"
def jobName = "seed"

println "DEBUG: List all nodes"
for (node in Jenkins.instance.getNodes()) {
  println "DEBUG: node " + node
}

// TODO: If jobName exists should delete it
map = Jenkins.instance.getItemMap()
println "DEBUG: map = " + map
if (map.containsKey(jobName)) {
  println "ERROR: project " + jobName + " exists"
  return 1;
}

project = Jenkins.instance.createProject(FreeStyleProject, jobName)
def gitScm = new GitSCM(url)
gitScm.branches = [new hudson.plugins.git.BranchSpec("*/master")]
project.scm = gitScm

project.getBuildersList().clear()
//
// project.getBuildersList().add(new Shell("echo Hello world"));
//
// project.getBuildersList().add(new Shell("docker pull niaquinto/gradle"));
// project.getBuildersList().add(new Shell("docker run -v \$PWD:/usr/bin/app --entrypoint=gradle niaquinto/gradle build"));
//
project.getBuildersList().add(new ExecuteDslScripts(
  new ExecuteDslScripts.ScriptLocation("false","mydsl/**/*.groovy",null),
  false,
  RemovedJobAction.IGNORE,
  RemovedViewAction.IGNORE,
  LookupStrategy.JENKINS_ROOT,
  "src/main/groovy")
);
project.save()

// TODO: JENKINS_URL ???
// println "DEBUG: Executing printenv"
// println "printenv".execute().text

println "INFO: Script seed.groovy executed correctly. Now execute"
println "\$ curl \${JENKINS_URL}/job/" + jobName + "/build"
