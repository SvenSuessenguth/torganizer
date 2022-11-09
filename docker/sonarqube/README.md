docker build -t "carboncopy/sonarqube" .  
docker volume create --name sonarqube_data  
docker volume create --name sonarqube_logs  
docker volume create --name sonarqube_extensions

# https://spin.atomicobject.com/2019/07/11/docker-volumes-explained/
# to find the volumes on disk:
# cd \\wsl$\docker-desktop-data\version-pack-data\community\docker\volumes
docker run ^
  -d ^
  --restart always ^
  -p 9000:9000 ^
  -v sonarqube_extensions:/opt/sonarqube/extensions ^
  -v sonarqube_data:/opt/sonarqube/data ^
  -v sonarqube_logs:/opt/sonarqube/logs ^
  --name sonarqube
  carboncopy/sonarqube
  
docker run -d --restart always -p 9000:9000 -v sonarqube_extensions:/opt/sonarqube/extensions -v sonarqube_data:/opt/sonarqube/data -v sonarqube_logs:/opt/sonarqube/logs --name sonarqube carboncopy/sonarqube