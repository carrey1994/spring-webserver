FROM azul/zulu-openjdk:17-latest AS backend

### [NOTE]
### Execute dockerfile on root project and copy files from root project ###
COPY . .
RUN ./mvnw package -DskipTests=true

FROM node:21-alpine AS frontend
COPY frontend frontend
RUN npm --prefix frontend install
RUN npm run --prefix frontend build

FROM azul/zulu-openjdk:17-latest
WORKDIR webserver
COPY --from=backend target/demo-0.0.1-SNAPSHOT.jar webserver.jar
COPY --from=frontend frontend/out static

ENTRYPOINT ["java", "-jar", "webserver.jar"]