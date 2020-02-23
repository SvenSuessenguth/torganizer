FROM open-liberty:javaee8-java12

USER root

COPY --chown=1001:0 /target/rest.war /config/dropins/
COPY --chown=1001:0 /src/main/liberty/config/server.xml /config/
# COPY --chown=1001:0 /src/main/liberty/config/server.env /opt/ol/wlp/etc/
COPY --chown=1001:0 /src/main/liberty/config/jvm.options /config/

ADD --chown=1001:0 https://jdbc.postgresql.org/download/postgresql-42.2.10.jar /opt/ol/wlp/usr/shared/resources/postgresql/

USER 1001

ENV LICENSE accept

