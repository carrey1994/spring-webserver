# Use a specific version for consistency and reproducibility
ARG VERSION=0.0.1-SNAPSHOT
FROM maven:3.9.6-eclipse-temurin-17-alpine AS backend
ARG VERSION
WORKDIR /backend

# Copy only the files needed for dependency resolution
COPY pom.xml pom.xml
COPY webserver webserver
COPY integration-test integration-test

RUN mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${VERSION} && \
    mvn clean package -DskipTests=true --quiet

FROM node:21-alpine AS frontend
WORKDIR /frontend

# Copy only the package.json and package-lock.json to leverage Docker cache
COPY frontend/ .
RUN npm install --silent && \
    npm run build --silent

# Use a minimal base image for the final stage
FROM azul/zulu-openjdk-alpine:17-jre
ARG VERSION
WORKDIR /webserver

# Copy the built artifacts from previous stages
COPY --from=backend /backend/webserver/target/webserver-${VERSION}.jar webserver.jar
COPY --from=frontend /frontend/out static

# Set a non-root user (optional but recommended for security)
USER 1000

ENTRYPOINT ["java", "-jar", "webserver.jar"]