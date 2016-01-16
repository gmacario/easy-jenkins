/*
  sysinfo.groovy

  Assumes to run on a POSIX-like environment (i.e. Linux + bash)

  See https://learnxinyminutes.com/docs/groovy/
*/

println "INFO: sysinfo.groovy"
println ""

println "INFO: Executing uname -a"
println "uname -a".execute().text

println "INFO: Executing id"
println "id".execute().text

println "INFO: Inspecting PATH"
println System.getenv("PATH")
println ""

println "INFO: Executing df -h"
println "df -h".execute().text

//println "INFO: Executing sudo id"
//println "sudo id".execute().text // java.io.IOException: Cannot run program "sudo": error=2, No such file or directory

println "INFO: Executing printenv"
println "printenv".execute().text

/* EOF */
