# frontend

Prepare for Coding  
mvn clean dependency:copy liberty:create

Running 
mvn clean package dependency:copy liberty:run

docker network create -d bridge carboncopy  
docker build -t "carboncopy/frontend" .  
docker run -d -p 8080:8080 --name frontend --network=carboncopy "carboncopy/frontend":latest

After starting postgresql via docker, you can start the app using the maven-liberty-pluing by typing 'mvn liberty:create dependency:copy@copy liberty:dev'
