# postgresql

docker build -t "torganizer/postgresql" .
docker run -d -p 5432:5432 --name postgresql --network=torganizer "torganizer/postgresql":latest
