pipeline {
    agent any
	triggers {
		pollSCM('* * * * *')
	}
    tools {
        maven 'apache-maven-3.5.0'
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
    }
}