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
    maven 'apache-maven-3.5'
    jdk 'jdk-10'
  }

  stages {
    stage('compile') {
      steps {
        bat 'mvn clean compile'
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
    stage('report') {
      steps {
        withSonarQubeEnv('SonarQube') {
          bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
        }
      }
    }

//    stage('doc') {
//      steps {
//        bat 'mvn javadoc:aggregate org.asciidoctor:asciidoctor-maven-plugin:process-asciidoc'
//      }
//    }

    // on master-branch only
    stage('deploy') {
      when { branch 'master' }
      steps {
        // Run the maven build
        bat 'mvn deploy -DskipTests'

        // archive artifacts in jenkins
        archiveArtifacts artifacts: '*/target/*.war', fingerprint: true
      }
    }
  }

//  post {
//    always {
//      archiveArtifacts artifacts: '*/target/*.war', fingerprint: true
//    }
//  }
}