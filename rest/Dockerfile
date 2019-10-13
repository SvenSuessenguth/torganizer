# https://www.ibm.com/support/knowledgecenter/SS7K4U_liberty/com.ibm.websphere.wlp.zseries.doc/ae/twlp_icp_docker.html

FROM open-liberty:javaee8-java12

USER root

COPY --chown=1001:0 /target/rest.war /config/dropins/
COPY --chown=1001:0 /src/main/config/wlp/server.xml /config/
COPY --chown=1001:0 /src/main/config/wlp/server.env /config/
COPY --chown=1001:0 /src/main/config/wlp/jvm.options /config/

ADD --chown=1001:0 https://jdbc.postgresql.org/download/postgresql-42.2.8.jar /opt/ol/wlp/usr/shared/resources/

USER 1001

ENV LICENSE accept

