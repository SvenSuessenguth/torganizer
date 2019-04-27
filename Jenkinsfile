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
    jdk 'jdk-11'
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
	stage ('analysis') {
      steps{
        bat 'mvn -batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd findbugs:findbugs'
		
		step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: '**/target/checkstyle-result.xml', unstableTotalAll:'0'])
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