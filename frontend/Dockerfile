FROM open-liberty:20.0.0.4-full-java8-openj9

COPY --chown=1001:0  /target/frontend.war /config/dropins/
COPY --chown=1001:0 /src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 /src/main/liberty/config/server.env /config/
COPY --chown=1001:0 /src/main/liberty/config/jvm.options /config/
ADD --chown=1001:0 https://jdbc.postgresql.org/download/postgresql-42.2.12.jar /opt/ol/wlp/usr/shared/resources/postgresql/postgresql.jar

ENV LICENSE accept

