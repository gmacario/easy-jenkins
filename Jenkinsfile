pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''#!/bin/bash

docker --version
docker-compose --version

docker-compose build --pull

# EOF'''
      }
    }
  }
}