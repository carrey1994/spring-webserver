#!/bin/bash

HEALTH_URL="http://localhost:8080/api/v1/public/health"

# Start Docker containers with Docker Compose
./docker-starter.sh

# Function to check if a container is healthy
check_service_health() {
    container_name=$1
    local max_wait_time=120
    while [ "$(docker inspect -f '{{.State.Health.Status}}' $container_name)" != "healthy" ]; do
        echo "Waiting for $container_name to be healthy..."
        sleep 5
        max_wait_time=$((max_wait_time-5))
        echo "Remaining timeout: $max_wait_time"
        if [ "$max_wait_time" -le 0 ]; then
            echo "Timeout waiting for $container_name to be healthy."
            exit 1
        fi
    done
    echo "$container_name is healthy."
}

# Wait for each container to be healthy
echo "Going to check containers to be healthy..."
containers=("spring-redis-master" "spring-redis-slave" "spring-mailhog" "spring-postgres" "spring-rabbitmq")
for container in "${containers[@]}"; do
    check_service_health $container
done
echo "The services are all healthy."

# Run integration test
./mvnw test
echo "Finished webserver test"