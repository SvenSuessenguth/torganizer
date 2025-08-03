FROM mysql:9.4.0

ENV MYSQL_ROOT_PASSWORD=root

RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

USER nonroot

COPY init.sql /docker-entrypoint-initdb.d

EXPOSE 3306