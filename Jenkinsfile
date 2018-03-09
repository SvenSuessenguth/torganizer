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
    maven 'apache-maven-3.5.2'
    jdk 'jdk1.8.0_144'
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
    stage('package') {
      steps {
        // Run the maven build
        bat 'mvn package -DskipTests'
      }
    }
    stage('report') {
      steps {
        withSonarQubeEnv('SonarQube') {
		  script {
		    try {
            bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
			} catch(Exception e){
			  currentBuild.result = 'SUCCESS'
			}
          }
        }
      } 
    }    
  }
	
  post {
    always {
      archiveArtifacts artifacts: '*/target/*.war', fingerprint: true
    }
  }
}