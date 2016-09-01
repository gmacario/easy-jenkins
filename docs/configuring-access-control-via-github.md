# Configuring Access Control in Jenkins via GitHub

After a default installation of easy-jenkins, the Jenkins instance can be controlled by whoever has accesses to `${JENKINS_URL}`.
This may be acceptable when your server is located behind a company firewall, but is definitely not suitable for publicly exposed servers.

This document explains how to enable access control, delegating user authentication to [GitHub](https://github.com/).

**NOTE**: In the configuration described in this page, `${JENKINS_URL}` corresponds to http://myserver.example.com:9080/ -- please replace details for your own instance of easy-jenkins wherever appropriate.

### Create a GitHub application registration

1. Visit https://github.com/settings/applications/new to create a GitHub application registration.
2. The values for application name, homepage URL, or application description don't matter. They can be customized however desired.
3. However, the authorization callback URL takes a specific value. It must be http://myserver.example.com:9080/securityRealm/finishLogin where http://myserver.example.com:9080/ is the location of the Jenkins server.

According to our example, fill in the following

* Application name: `easy-jenkins@myserver.example.com:9080`
* Homepage URL: `http://myserver.example.com:9080/`
* Application description: `Testing OAuth with easy-jenkins`
* Authorization callback URL: `http://myserver.example.com:9080/securityRealm/finishLogin`

then click **Register application**.

Keep the result page open, and take note of the following values (they will be used to configure the Github Authentication Plugin as explained in the following section)

* Client ID: xxx
* Client Secret: yyy

### Configure Global Security in Jenkins

Browse `${JENKINS_URL}` > Manage Jenkins > Configure Global Security

* Enable security: Yes
  * TCP port for JNLP agents: Fixed: 5000
  * Disable remember me: No
  * Access Control
    - Security Realm: Github Authentication Plugin
      - Global GitHub OAuth Settings
        - GitHub Web URI: `https://github.com`
        - GitHub API URI: `https://api.github.com`
        - Client ID: xxx (paste the value from the GitHub page above)
        - Client Secret: yyy (paste the value from the GitHub page above)
        - OAuth Scope(s): `read:org,user:email`
    - Authorization: Logged-in users can do anything (See NOTE below)
      * Allow anonymous read access: No
* Markup Formatter: Plain text
* Prevent Cross Site Request Forgery exploits: No
* Plugin Manager
  * Use browser for metadata download: No
* Enable Slave->Master Access Control: Yes

then click **Save**.

**NOTE**: In order to achieve a finer grain of access control choose instead

* Authorization: Project-based Matrix Authorization Strategy

then add each single GitHub user/group you want to enable.

**IMPORTANT**: Make sure you give all rights at least to one legitimate user, otherwise after clicking "Save" you will not be able to login any more!

### See also

* https://jenkins.io/solutions/github/
* https://wiki.jenkins-ci.org/display/JENKINS/Github+OAuth+Plugin#GithubOAuthPlugin-Setup
* https://developer.github.com/v3/oauth/

<!-- EOF -->
