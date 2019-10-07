# https://www.ibm.com/support/knowledgecenter/SS7K4U_liberty/com.ibm.websphere.wlp.zseries.doc/ae/twlp_icp_docker.html

FROM open-liberty:latest

COPY --chown=1001:0  /target/webclient.war /config/dropins/
COPY --chown=1001:0 /src/main/config/wlp/server.xml /config/
COPY --chown=1001:0 /src/main/config/wlp/server.env /config/
COPY --chown=1001:0 /src/main/config/wlp/jvm.options /config/

ENV LICENSE accept

