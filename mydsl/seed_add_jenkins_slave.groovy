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
      textParam('AgentList', 'build-yocto-slave', 'Name of agents to create, optionally more than one (each line makes one agent)')
      stringParam('AgentDescription', 'Auto-created Jenkins agent', 'Description that will be set for _every_ created agent')
      stringParam('AgentHome', '/home/jenkins', 'Remote filesystem root for the agent')
      stringParam('AgentExecutors', '2', 'Number of executors for the agent')
    }
    
    steps {
      systemGroovyScriptFile ('myscripts/add_slave_nodes.groovy') {
	      // TODO
      }
    }
  }
}
