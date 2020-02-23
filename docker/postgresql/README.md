# postgresql

docker pull postgres  
docker build -t "torganizer/postgresql" .  
docker run -d -e POSTGRES_PASSWORD=postgres -p 5432:5432 --name postgresql --network=torganizer "torganizer/postgresql":latest
