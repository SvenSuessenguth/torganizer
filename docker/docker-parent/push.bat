docker build . -t "carboncopy/torganizer-base"
docker tag "carboncopy/torganizer-base" "carboncopy/torganizer-base:latest"
docker push carboncopy/torganizer-base