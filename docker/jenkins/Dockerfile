FROM jenkins/jenkins:2.521-jdk21

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

USER nonroot

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

COPY init.groovy /usr/share/jenkins/ref/init.groovy.d/init.groovy