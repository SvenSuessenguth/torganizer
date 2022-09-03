docker volume create --name jenkins_home

docker pull jenkins:latest
docker build -t "carboncopy/jenkins" .
docker run -d ^
  -v jenkins_home:/var/jenkins_home ^
  -p 8080:8080 ^
  -p 50000:50000 ^
  --name jenkins ^
  --network=carboncopy ^
  "carboncopy/jenkins":latest