#!/bin/bash

BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
IMAGE_NAME="shall-we-image"
CONTAINER_NAME="shall-we-server-container"

echo ">>> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

if [ -z "$BUILD_JAR" ]; then
  echo ">>> No JAR file found. Exiting." >> /home/ec2-user/action/deploy.log
  exit 1
fi

echo ">>> build 파일 복사" >> /home/ec2-user/action/deploy.log

cat <<EOF > /home/ec2-user/action/Dockerfile
FROM openjdk:8-jdk-alpine
COPY $BUILD_JAR /app/$JAR_NAME
WORKDIR /app
CMD ["java", "-jar", "$JAR_NAME"]
EOF

docker build -t $IMAGE_NAME /home/ec2-user/action

if [ $(docker ps -q -f name=$CONTAINER_NAME) ]; then
  echo ">>> Stopping and removing existing container: $CONTAINER_NAME" >> /home/ec2-user/action/deploy.log
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

echo ">>> Starting new container: $CONTAINER_NAME" >> /home/ec2-user/action/deploy.log
docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME

echo ">>> Application deployed in Docker container: $CONTAINER_NAME" >> /home/ec2-user/action/deploy.log
