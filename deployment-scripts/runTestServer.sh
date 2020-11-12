fuser -k 35503/tcp || true
source ./test-server/envVars.txt
java -Dspring.profiles.active=localDev -jar test-server/libs/server-0.0.1-SNAPSHOT.jar --server.port=35503
