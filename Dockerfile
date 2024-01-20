FROM azul/zulu-openjdk:17-latest AS builder
COPY . .
RUN ./mvnw package -DskipTests=true

FROM azul/zulu-openjdk:17-latest
WORKDIR webserver
COPY --from=builder target/demo-0.0.1-SNAPSHOT.jar webserver.jar

ENTRYPOINT ["java", "-jar", "webserver.jar"]