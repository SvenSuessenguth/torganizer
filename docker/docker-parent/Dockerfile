# https://hub.docker.com/r/openliberty/open-liberty
FROM icr.io/appcafe/open-liberty:24.0.0.12-kernel-slim-java21-openj9-ubi-minimal

ADD --chown=1001:0 target/dependency/postgresql.jar /opt/ol/wlp/usr/shared/resources/postgresql/postgresql.jar