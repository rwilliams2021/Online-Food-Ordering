#!/bin/bash

BACKEND_DIR="./"
DOCKER_COMPOSE_FILE="docker-compose.yml"

if command -v mvn &> /dev/null
then
    echo "Maven is installed."
else
    echo "Maven is not installed"
    echo "On mac use brew install maven"
    echo "On windows, idk"
fi

if test -f "../src/main/resources/Online-Food-Ordering.properties"; then
  echo "File exists."
else
  echo "Online-Food-Ordering.properties does not exist. Please ask another developer for it!"
fi

cd ..
echo "Building the backend jar..."
mvn clean package -DskipTests

# Stop and remove existing containers
echo "Removing existing containers..."
docker compose down

echo "Starting the Docker Compose environment..."
docker-compose -f $DOCKER_COMPOSE_FILE up