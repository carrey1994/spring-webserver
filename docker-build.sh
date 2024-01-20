#!/bin/bash
docker network create dev
docker run -d --network dev --name spring-postgres -e POSTGRES_PASSWORD=postgrez -p 5432:5432 postgres:16
docker build -t spring-webserver .
docker run -d --network dev --name dev-webserver -p 8080:8080 spring-webserver