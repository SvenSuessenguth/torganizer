pipeline {
  agent any

  parameters {
    boolean(name: 'doRelease', defaultValue: 'false', description: 'should a release be performed?')
  }
	
  options {
    buildDiscarder(logRotator(daysToKeepStr: '3', numToKeepStr: '3', artifactNumToKeepStr: '3'))
    durabilityHint('PERFORMANCE_OPTIMIZED')
  }
	
  triggers {
    pollSCM('* * * * *')
  }
	
  tools {
    maven 'apache-maven-3.5.3'
    jdk 'jdk1.8.0_162'
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
	stage('release') {
	  when {
        ${param.doRelease}
      }
      steps {
        // Run the maven build
        bat 'mvn release:prepare release:perform -B'
      }
    }
    stage('report') {
      steps {
        withSonarQubeEnv('SonarQube') {
          bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
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