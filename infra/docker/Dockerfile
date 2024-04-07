FROM maven:3.9.6-eclipse-temurin-17-alpine AS backend
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR backend
COPY . .
#RUN mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION
#RUN mvn clean package -DskipTests=true --quiet

FROM node:21-alpine AS frontend
COPY frontend frontend
RUN npm --prefix frontend install --silent
RUN npm run --prefix frontend build --silent

FROM azul/zulu-openjdk-alpine:17
WORKDIR webserver
COPY --from=backend backend/webserver/target/webserver-0.0.1-SNAPSHOT.jar webserver.jar
COPY --from=frontend frontend/out static

ENTRYPOINT ["java", "-jar", "webserver.jar"]