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
    maven 'apache-maven-3.5.4'
    jdk 'jdk1.8.0_172'
  }

  stages {
    stage('compile') {
      steps {
        // Run the maven build
        bat 'mvn clean compile'
      }
    }
    stage('test') {
      steps {
        // Run the maven build
        bat 'mvn test'
      }
			
      // @see https://jenkins.io/blog/2017/02/07/declarative-maven-project/
      post {
        success {
          junit '**/target/surefire-reports/TEST-*.xml' 
        }
      }
    }
    stage('deploy') {
      steps {
        // Run the maven build
        bat 'mvn deploy -DskipTests'
      }
    }
    stage('report') {
      steps {
        withSonarQubeEnv('SonarQube') {
          bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.4.0.905:sonar'
        }        
      } 
    }
    stage('doc') {
      steps {
        bat 'mvn javadoc:javadoc org.asciidoctor:asciidoctor-maven-plugin:process-asciidoc -Dbackend=html'
      }
    }
  }
	
  post {
    always {
      archiveArtifacts artifacts: '*/target/*.war', fingerprint: true
    }
  }
}