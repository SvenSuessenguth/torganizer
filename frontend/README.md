# frontend

docker build -t "org.cc.torganizer/frontend" .  
docker run -d -p 8080:8080 --name frontend --network=torganizer "torganizer/frontend":latest
