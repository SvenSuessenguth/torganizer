FROM jenkins/jenkins

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

RUN install-plugins.sh \
    # prerequisite
      jdk-tool:latest \
      script-security:latest \
      command-launcher:latest \
    # suggested plugins
      cloudbees-folder:latest \
      antisamy-markup-formatter:latest \
      build-timeout:latest \
      credentials-binding:latest \
      timestamper:latest \
      ws-cleanup:latest \
      ant:latest \
      gradle:latest \
      workflow-aggregator \
      github-branch-source:latest \
      pipeline-github-lib:latest \
      pipeline-stage-view:latest \
      git:latest \
      subversion:latest \
      ssh-slaves:latest \
      matrix-auth:latest \
      pam-auth:latest \
      ldap:latest \
      email-ext:latest \
      mailer:latest \
    # torganizer
      sonar:latest

COPY init.groovy /usr/share/jenkins/ref/init.groovy.d/init.groovy