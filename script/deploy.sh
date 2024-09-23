#!/bin/bash

# Define variables
BUILD_JAR=$(ls /home/ec2-user/action/target/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
IMAGE_NAME="shall-we-image"
CONTAINER_NAME="shall-we-server-container"
LOG_FILE="/home/ec2-user/action/deploy.log"

echo ">>> Build 파일명: $JAR_NAME" >> $LOG_FILE
if [ -z "$BUILD_JAR" ]; then
  echo ">>> No JAR file found. Exiting." >> $LOG_FILE
  exit 1
fi

echo ">>> Build 파일 복사" >> $LOG_FILE

cat <<EOF > /home/ec2-user/action/Dockerfile
FROM tomcat:8-jdk8-openjdk
COPY $BUILD_JAR /opt/tomcat/webapps/$JAR_NAME
CMD ["catalina.sh", "run"]
EOF

docker build -t $IMAGE_NAME /home/ec2-user/action

if [ $(docker ps -q -f name=$CONTAINER_NAME) ]; then
  echo ">>> Stopping and removing existing container: $CONTAINER_NAME" >> $LOG_FILE
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

echo ">>> Starting new container: $CONTAINER_NAME" >> $LOG_FILE
docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME

echo ">>> Application deployed in Docker container: $CONTAINER_NAME" >> $LOG_FILE
