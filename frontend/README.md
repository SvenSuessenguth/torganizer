# frontend

docker network create -d bridge carboncopy  
docker build -t "carboncopy/frontend" .  
docker run -d -p 8080:8080 --name frontend --network=carboncopy "carboncopy/frontend":latest
