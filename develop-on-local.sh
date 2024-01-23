#!/bin/bash

docker run -d \
  --name spring-mysql-master \
  -e MYSQL_DATABASE=webserver \
  -e MYSQL_PASSWORD=my-sql-password \
  -e MYSQL_USER=user \
  -e MYSQL_ROOT_PASSWORD=my-sql-password \
  -p 3306:3306 \
  --network dev \
  --restart always \
  --health-cmd='mysqladmin ping -h localhost -u$$MYSQL_USER -p$$MYSQL_PASSWORD' \
  --health-interval=20s \
  --health-retries=5 \
  bitnami/mysql:8.0

# Start Redis Master container
docker run -d \
  --name spring-redis-master \
  -v $(pwd)/data/spring-redis-master/data:/data \
  --network dev \
  -p 6379:6379 \
  --health-cmd='redis-cli ping' \
  --health-interval=20s \
  --health-retries=5 \
  redis

# Start Redis Slave container
docker run -d \
  --name spring-redis-slave \
  -v $(pwd)/data/spring-redis-slave/data:/data \
  --network dev \
  -p 6380:6379 \
  --health-cmd='redis-cli ping' \
  --health-interval=20s \
  --health-retries=5 \
  --link spring-redis-master:spring-redis-master \
  redis \
  redis-server --slaveof spring-redis-master 6379

# Start Redis Commander container
docker run -d \
  --name spring-redis-commander \
  --hostname redis-commander \
  -v $(pwd)/data/spring-redis-commander/data:/data \
  --network dev \
  -p 8081:8081 \
  --link spring-redis-master:master \
  --link spring-redis-slave:slave-1 \
  rediscommander/redis-commander:latest

./mvnw spotless:apply
./mvnw spring-boot:run
