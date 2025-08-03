FROM nginx:1.29

RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

USER nonroot

COPY ./static/apache-maven-3.5.3-bin.tar.gz /static/jdk-8u172-linux-x64.tar.gz /usr/share/nginx/html/