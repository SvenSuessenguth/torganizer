# postgresql

docker network create -d bridge carboncopy  
docker pull postgres  
docker build -t "carboncopy/postgresql" .  
docker run -d -e POSTGRES_PASSWORD=postgres -p 5432:5432 --name cc-postgresql --network=carboncopy "carboncopy/postgresql":latest
