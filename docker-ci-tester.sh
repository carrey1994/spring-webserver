#!/bin/bash

HEALTH_URL="http://localhost:8080/api/v1/public/health"

# Start Docker containers with Docker Compose
./docker-starter.sh

# Function to check if a container is healthy
check_container_health() {
    container_name=$1
    while [ "$(docker inspect -f '{{.State.Health.Status}}' $container_name)" != "healthy" ]; do
        echo "Waiting for $container_name to be healthy..."
        sleep 5
    done
    echo "$container_name is healthy."
}

# Wait for each container to be healthy
containers=("spring-redis-commander" "spring-redis-slave" "spring-redis-master" "spring-mailhog" "spring-mysql-master" "spring-rabbitmq")
for container in "${containers[@]}"; do
    check_container_health $container
done
echo "All containers are healthy."

echo "Running webserver... "
nohup java -jar webserver/target/webserver-0.0.1-SNAPSHOT.jar &> /dev/null &

while true; do
    status_code=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)
    if [ $status_code -eq 200 ]; then
        echo "Health check successful"
        break
    else
        echo "Health check failed, status code: $status_code"
        sleep 5  # Wait for 5 seconds before retrying
    fi
done

# Run integration test
./mvnw test
echo "Finished webserver test"