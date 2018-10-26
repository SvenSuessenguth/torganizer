FROM websphere-liberty:kernel
COPY /target/webclient.war /config/dropins/
COPY server.xml /config/
RUN installUtility install --acceptLicense defaultServer