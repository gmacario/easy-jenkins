job('add-jenkins-slave') {
  scm {
    git('https://github.com/gmacario/easy-jenkins', 'fix-issue-16-v2')
    
    parameters {
      textParam('AgentList', 'build-yocto-slave', 'Name of agents to create, optionally more than one (each line makes one agent)')
      stringParam('AgentDescription', 'Auto-created Jenkins agent', 'Description that will be set for _every_ created agent')
      stringParam('AgentHome', '/home/jenkins', 'Remote filesystem root for the agent')
      stringParam('AgentExecutors', '2', 'Number of executors for the agent')
    }
    
    steps {
      text(readFileFromWorkspace('myscripts/add-slave-nodes.groovy'))
      removeAction('DELETE')
      // TODO
    }
  }
}
