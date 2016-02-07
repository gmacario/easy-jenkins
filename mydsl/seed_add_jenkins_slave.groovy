/*
  Project: https://github.com/gmacario/easy-jenkins
  File:    mydsl/seed_add_jenkins_slave.groovy

  References:
    - https://jenkinsci.github.io/job-dsl-plugin/#
 */
 
job('add_jenkins_slave') {
  scm {
    git('https://github.com/gmacario/easy-jenkins', '*/master')
    
    parameters {
      textParam('AgentList', 'build-yocto-slave', 'Names of the agent(s) to create (each line makes one agent)')
      stringParam('AgentDescription', 'Auto-created Jenkins agent', 'Description that will be set for _every_ created agent')
      stringParam('AgentExecutors', '2', 'Number of executors for the agent')
      stringParam('AgentHome', '/home/jenkins', 'Remote filesystem root for the agent')
      textParam('AgentLabels', 'yocto', 'Labels associated to the agent (each line makes one label)')
    }
    
    steps {
      systemGroovyScriptFile ('myscripts/add_slave_nodes.groovy') {
	      // TODO
      }
    }
  }
}
