FROM openliberty/open-liberty:full-java13-openj9-ubi

COPY --chown=1001:0  /target/frontend.war /config/dropins/
COPY --chown=1001:0 /src/main/liberty/config/server.xml /config/
# COPY --chown=1001:0 /src/main/liberty/config/server.env /opt/ol/wlp/etc/
COPY --chown=1001:0 /src/main/liberty/config/jvm.options /config/
ADD --chown=1001:0 https://jdbc.postgresql.org/download/postgresql-42.2.10.jar /opt/ol/wlp/usr/shared/resources/postgresql/

ENV LICENSE accept

