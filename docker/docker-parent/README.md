# carboncopy docker parent

docker login --username=username --password=password  
docker build . -t "carboncopy/torganizer"  
docker tag "carboncopy/torganizer" "carboncopy/torganizer:latest"  
docker push carboncopy/torganizer

