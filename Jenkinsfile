// Jenkins need to install the following additional plugins:
// https://updates.jenkins.io/download/plugins/
// SonarQube Scanner
// Warnings Next Generation

pipeline {
  agent any

  options {
    buildDiscarder(logRotator(daysToKeepStr: '3', numToKeepStr: '3', artifactNumToKeepStr: '3'))
    durabilityHint('PERFORMANCE_OPTIMIZED')
  }

  triggers {
    pollSCM('* * * * *')
  }

  tools {
    maven 'apache-maven-3.6'
    jdk 'openjdk-12'
  }

  stages {
    stage('compile') {
      steps {
        sh 'mvn clean compile'
      }
    }
    stage('test') {
      steps {
        bat 'mvn test'
      }

      // @see https://jenkins.io/blog/2017/02/07/declarative-maven-project/
      post {
        success {
          junit '**/target/surefire-reports/TEST-*.xml'
        }
      }
    }
    stage ('analysis') {
      steps{
        // https://github.com/jenkinsci/warnings-ng-plugin/blob/master/doc/Documentation.md
        
        bat 'mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs dependency-check:check'
      }
      post {
        always {
          recordIssues enabledForFailure: true, aggregatingResults : true, tools: [
            mavenConsole(),
            java(),
            checkStyle(),
            spotBugs(),
            cpd(pattern: '**/target/cpd.xml'),
            pmdParser(pattern: '**/target/pmd.xml')
          ]
        }
      }
    }

    stage('report') {
      steps {
        withSonarQubeEnv('SonarQube') {
          bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
        }
      }
    }

    // on master-branch only
    stage('deploy') {
      when { branch 'master' }
      steps {
        // Run the maven build
        bat 'mvn deploy -DskipTests'
      }
    }
  }
}