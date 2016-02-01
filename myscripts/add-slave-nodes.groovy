/**
 * This script is meant to be executed by a parameterized job in Jenkins and will then create new agents (slaves) as per the parameters
 *
 * SUGGESTED PAIRED PARAMETERS IN JENKINS (type, name, default values, description):
 *
 * Text - AgentList - "TestAutoAgent" - Name of agents to create, optionally more than one (each line makes one agent)
 * String - AgentDescription - "Auto-created Jenkins agent" - Description that'll be set for _every_ created agent
 * String - AgentHome - "D:\JenkinsAgent" - Remote filesystem root for the agent
 * String - AgentExecutors - 2 - Number of executors for the agent
 */

import hudson.model.Node.Mode
import hudson.slaves.*
import jenkins.model.Jenkins

/*
//Handy debug logging
Jenkins.instance.nodes.each {
    println "BEFORE - Agent: $it"
}
*/

// The "build" object is added by the Jenkins Groovy plugin and can resolve parameters and such
String agentList = build.buildVariableResolver.resolve('AgentList')
String agentDescription = build.buildVariableResolver.resolve('AgentDescription')
String agentHome = build.buildVariableResolver.resolve('AgentHome')
String agentExecutors = build.buildVariableResolver.resolve('AgentExecutors')

agentList.eachLine {

    // There is a constructor that also takes a list of properties (env vars) at the end, but haven't needed that yet
    DumbSlave dumb = new DumbSlave(it,  // Agent name, usually matches the host computer's machine name
            agentDescription,           // Agent description
            agentHome,                  // Workspace on the agent's computer
            agentExecutors,             // Number of executors
            Mode.EXCLUSIVE,             // "Usage" field, EXCLUSIVE is "only tied to node", NORMAL is "any"
            "",                         // Labels
            new JNLPLauncher(),         // Launch strategy, JNLP is the Java Web Start setting services use
            RetentionStrategy.INSTANCE) // Is the "Availability" field and INSTANCE means "Always"

    Jenkins.instance.addNode(dumb)
    println "Agent '$it' created with $agentExecutors executors and home '$agentHome'"
}

/*
Jenkins.instance.nodes.each {
    println "AFTER - Agent: $it"
}
*/
