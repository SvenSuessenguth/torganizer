FROM carboncopy/torganizer-base:latest

COPY --chown=1001:0  /target/frontend.war /config/apps/
COPY --chown=1001:0 /src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 /src/main/liberty/config/jvm.options /config/

COPY --chown=1001:0 /src/main/liberty/config/resources/security/key.p12 /config/resources/security
COPY --chown=1001:0 /src/main/liberty/config/resources/security/ltpa.keys /config/resources/security

ENV LICENSE=accept

RUN configure.sh

