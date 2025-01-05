#!/bin/bash

BACKEND_DIR="./"
DOCKER_COMPOSE_FILE="docker-compose.yml"

if test -f "../src/main/resources/Online-Food-Ordering.properties"; then
  echo "File exists."
else
  echo "Online-Food-Ordering.properties does not exist. Please ask another developer for it!"
fi

cd ..

# Stop and remove existing containers
echo "Removing existing containers..."
docker compose down

echo "Starting the Docker Compose environment..."
docker-compose -f $DOCKER_COMPOSE_FILE up