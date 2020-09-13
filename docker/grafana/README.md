docker build -t cc-grafana .
docker run --name=cc-grafana -p 3000:3000 grafana/grafana