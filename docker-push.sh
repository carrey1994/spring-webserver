#!/bin/bash
./mvnw spotless:apply
docker build --no-cache -t carrey1994/spring-webserver -f docker/Dockerfile .