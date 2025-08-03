FROM prom/prometheus:v3.5.0

RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

USER nonroot

COPY prometheus.yml /etc/prometheus/
COPY targets.json /etc/prometheus/