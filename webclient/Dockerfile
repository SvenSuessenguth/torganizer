FROM torganizer/wlp
COPY /target/webclient.war /config/dropins/
COPY /src/main/config/wlp/server.xml /config/
COPY /src/main/config/wlp/server-include.xml /config/
COPY /src/main/config/wlp/server.env /config/
RUN installUtility install --acceptLicense defaultServer

EXPOSE 8080