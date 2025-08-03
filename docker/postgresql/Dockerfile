FROM postgres:17.5
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

USER nonroot
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 5432