# easy-jenkins/myscripts

This directory contains a collection of useful Jenkins scripts.

### How to run the scripts

Jenkins offers a scripting language based on [Groovy](http://www.groovy-lang.org/) to simplify administration tasks.

#### From Jenkins Script Console

The easiest way for running a Jenkins script is via the Jenkins Script Console which you may access via a web browser opening the
`${JENKINS_DASHBOARD}/console` URL, for instance `http://192.168.99.100:9080/console`.

See https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+Script+Console for details.

#### From a shell

You may also run the scripts directly from a shell (i.e. Bash)

```
$ curl -d "script=<your_script_here>" http://jenkins/script
```

Alternatively you may get a plain/text response with the following command

```
$ curl -d "script=<your_script_here>" http://jenkins/scriptText
```

According to the HTTP protocol, you will get a "Error 403" in case you have no rights to access the Jenkins Script Console -- in this case you may provide the `--user 'username:password'` option to curl to submit the proper user credentials.

Error 401 will be returned if the credentials are not valid.

If you add the `-s` option to curl the debugging information will be stripped, and only the actual result from the Jenkins server will be printed.

#### A few examples

```
$ curl -d "script=$(<./myscripts/hello.groovy)" http://192.168.99.100:9080/scriptText
```

```
$ curl -sd "script=$(<./myscripts/hello.groovy)" http://mv-linux-powerhorse.solarma.it:9080/scriptText
```

```
$ curl --user 'username:password' -d "script=$(<./myscripts/hello.groovy)" https://build.automotivelinux.org/scriptText
```

### See also

* https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+Script+Console
* https://www.cloudbees.com/jenkins/juc-2015/presentations/JUC-2015-USEast-Groovy-With-Jenkins-McCollum.pdf
* https://learnxinyminutes.com/docs/groovy/

<!-- EOF -->
