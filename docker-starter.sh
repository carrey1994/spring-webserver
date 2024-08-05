#!/bin/bash
WEBSERVER=false;
while getopts w flag
do
    case "${flag}" in
        w )
          WEBSERVER=true;
    esac
done

echo "shutting down docker containers..."
docker compose -f infra/docker/docker-compose.yml --profile webserver down

echo "starting docker containers..."
if [ "$WEBSERVER" = true ]; then
    docker compose -f infra/docker/docker-compose.yml --profile webserver up -d
else
    docker compose -f infra/docker/docker-compose.yml up -d
fi
