# carboncopy docker parent

docker login --username=username --password=password  
docker build . -t "carboncopy/torganizer-base"  
docker tag "carboncopy/torganizer-base" "carboncopy/torganizer-base:latest"  
docker push carboncopy/torganizer-base

