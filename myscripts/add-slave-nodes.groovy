/* WORK-IN-PROGRESS

Add slave nodes

Execute it from the Script Console:

- Browse ${JENKINS_URL}/script
- Paste this page
- Click "Run"

You may also execute it from the Scriptler plugin:

- Browse ${JENKINS_URL}/scriptler
*/

// See also:
//
// https://github.com/MovingBlocks/GroovyJenkins/blob/master/src/main/groovy/AddNodeToJenkins.groovy
// https://github.com/MovingBlocks/GroovyJenkins/blob/master/src/main/groovy/RemoveAgent.groovy

// Jenkins APIs:
//
// http://javadoc.jenkins-ci.org/allclasses-noframe.html
// http://javadoc.jenkins-ci.org/hudson/PluginManager.html
// http://javadoc.jenkins-ci.org/hudson/PluginWrapper.html
// http://javadoc.jenkins-ci.org/hudson/model/Node.Mode.html
// http://javadoc.jenkins-ci.org/hudson/model/UpdateSite.Plugin.html
// http://javadoc.jenkins-ci.org/hudson/slaves/ComputerLauncher.html
// http://javadoc.jenkins-ci.org/hudson/slaves/DumbSlave.html
// http://javadoc.jenkins-ci.org/hudson/slaves/JNLPLauncher.html
// http://javadoc.jenkins-ci.org/hudson/slaves/RetentionStrategy.html
// http://javadoc.jenkins-ci.org/hudson/slaves/RetentionStrategy.Always.html

import jenkins.model.Jenkins
import hudson.model.Node.Mode
import hudson.slaves.ComputerLauncher
import hudson.slaves.JNLPLauncher
import hudson.slaves.RetentionStrategy
import hudson.slaves.RetentionStrategy.Always

import hudson.slaves.DumbSlave

def debugPrint(String s) {
  println "DEBUG: " + s
}

debugPrint("Jenkins.instance.computers=" + Jenkins.instance.computers);

DumbSlave slave = new DumbSlave(
  "build-yocto-slave",						// String name
  "A slave for building Yocto images",		// String nodeDescription
  "/home/jenkins",							// String remoteFS
  "3",										// String numExecutors
  Node.Mode.NORMAL,							// Node.Mode mode
  "yocto",									// String labelString
  JNLPLauncher,								// ComputerLauncher launcher
  RetentionStrategy.Always,					// RetentionStrategy retentionStrategy
  null										// List<? extends NodeProperty<?>> nodeProperties
  );

// TODO

// println(Jenkins.instance.pluginManager.plugins)
// println Jenkins.instance.pluginManager.getPlugins()
// println ""

/**
println "# pluginName pluginVersion # latestVersion"
for (p in Jenkins.instance.pluginManager.plugins.sort()) {
  debugPrint "p=" + p
  debugPrint "p.getClass()=" + p.getClass()
  debugPrint "p.getVersion()=" + p.getVersion()
  debugPrint "p.getBackupVersion()=" + p.getBackupVersion()
  debugPrint "p.hasUpdate()=" + p.hasUpdate()
  debugPrint "p.getUpdateInfo()=" + p.getUpdateInfo()
  
  line = p.toString()
  line = line.substring(line.lastIndexOf(':') + 1)
  if (p.hasUpdate()) {
    hudson.model.UpdateSite.Plugin usp = p.getUpdateInfo();
    debugPrint "usp.title=" + usp.title
    debugPrint "usp.wiki=" + usp.wiki
    debugPrint "usp.version=" + usp.version
    line = line + " " + p.getVersion() + " # " + usp.version 
  } else {
    line = line + " " + p.getVersion()
  }
  // debugPrint p.api.toString()
  // debugPrint p.getInfo()
  println line
  // println "# ---------------------"
}
**/

// println Jenkins.instance.getViews()
