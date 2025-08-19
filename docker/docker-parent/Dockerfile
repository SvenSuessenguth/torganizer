# https://hub.docker.com/r/openliberty/open-liberty
FROM icr.io/appcafe/open-liberty:25.0.0.8-kernel-slim-java21-openj9-ubi-minimal

COPY --chown=1001:0 target/dependency/postgresql.jar /opt/ol/wlp/usr/shared/resources/postgresql/postgresql.jar