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
        bat 'mvn checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs'
		// https://github.com/firemanphil/jenkinsfile/blob/master/Jenkinsfile
		// https://github.com/jenkinsci/warnings-ng-plugin/blob/master/doc/Documentation.md
	
        def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
        publishIssues issues: [checkstyle]
   
        def pmd = scanForIssues tool: pmdParser(pattern: '**/target/pmd.xml')
        publishIssues issues: [pmd]
        
        def cpd = scanForIssues tool: cpd(pattern: '**/target/cpd.xml')
        publishIssues issues: [cpd]
        
        def spotbugs = scanForIssues tool: spotBugs(pattern: '**/target/spotbugs.xml')
        publishIssues issues: [spotbugs]

        def maven = scanForIssues tool: mavenConsole()
        publishIssues issues: [maven]
        
        publishIssues id: 'analysis', name: 'All Issues', 
            issues: [checkstyle, pmd, spotbugs], 
            filters: [includePackage('io.jenkins.plugins.analysis.*')]
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