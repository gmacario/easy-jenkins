/*
List all the installed Jenkins plugins

Usage:

- Browse ${JENKINS_URL}/script
- Paste this page
- Click "Run"

*/

// Jenkins APIs:
//
// http://javadoc.jenkins-ci.org/allclasses-noframe.html
// http://javadoc.jenkins-ci.org/hudson/PluginManager.html
// http://javadoc.jenkins-ci.org/hudson/PluginWrapper.html
// http://javadoc.jenkins-ci.org/hudson/model/UpdateSite.Plugin.html

def debugPrint(String s) {
  //println "DEBUG: " + s
}

// println(Jenkins.instance.pluginManager.plugins)
// println Jenkins.instance.pluginManager.getPlugins()
// println ""

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

// println Jenkins.instance.getViews()
