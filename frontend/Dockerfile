FROM carboncopy/torganizer:latest

COPY --chown=1001:0  /target/frontend.war /config/dropins/
COPY --chown=1001:0 /src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 /src/main/liberty/config/server.env /config/
COPY --chown=1001:0 /src/main/liberty/config/jvm.options /config/

ENV LICENSE accept

