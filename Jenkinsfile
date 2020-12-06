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
    jdk 'openjdk-15'
  }

  stages {
    stage('compile') {
      steps {
        execute('mvn clean compile -T 4C')
      }
    }

    stage('test') {
      steps {
        execute('mvn test -T 4C')
      }

      // @see https://jenkins.io/blog/2017/02/07/declarative-maven-project/
      post {
        success {
          junit '**/target/surefire-reports/TEST-*.xml'
        }
      }
    }

    // on master-branch only
    stage('deploy') {
      when { branch 'master' }
      steps {
        // deploy already build artifact
        execute('mvn deploy -T 4C -DskipTests')
      }
    }

    stage ('docker') {
      steps {
        execute('docker build -t "torganizer/frontend" -f ./frontend/Dockerfile ./frontend')
      }
    }

    stage ('static-analysis') {
      steps{
        // https://github.com/jenkinsci/warnings-ng-plugin/blob/master/doc/Documentation.md
        execute('mvn checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs javadoc:aggregate')
      }
      post {
        always {
          recordIssues enabledForFailure: true, aggregatingResults : true, tools: [
            mavenConsole(),
            java(),
            javaDoc(),
            taskScanner(),
            checkStyle(),
            spotBugs(),
            cpd(pattern: '**/target/cpd.xml'),
            pmdParser(pattern: '**/target/pmd.xml')
          ]
        }
      }
    }

    stage('sonar-report') {
      steps {
        withSonarQubeEnv('SonarQube') {
          execute('mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar')
        }
      }
    }

    stage('dependency-check') {
      steps {
        execute('mvn dependency-check:aggregate')
      }
      post {
        always {
          dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
        }
      }
    }
  }
}

void execute(instruction) {
  if (isUnix()) {
    sh instruction
  } else {
    bat instruction
  }
}