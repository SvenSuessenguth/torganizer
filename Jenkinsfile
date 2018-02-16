pipeline {
    agent any
	
    properties([
	  parameters
	  ([
	    string(defaultValue: '*/master', description: '', name: 'Branch Specifier', trim: false)
	  ]), 
	  pipelineTriggers([pollSCM('* * * * *')])
	])
	
	options {
      buildDiscarder(logRotator(daysToKeepStr: '3', numToKeepStr: '3', artifactNumToKeepStr: '3'))
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
                junit '**/target/surefire-reports/TEST-*.xml'
                archive 'target/*.jar'
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
}