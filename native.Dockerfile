FROM ghcr.io/graalvm/native-image-community:22 AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN ./mvnw -B dependency:resolve dependency:resolve-plugins dependency:go-offline -DskipTests

COPY src src

RUN ./mvnw package -DskipTests -Dnative

#FROM registry.access.redhat.com/ubi8/ubi-minimal:8.10
FROM alpine:latest

WORKDIR /work/

RUN apk update --no-cache && \
    apk add --no-cache gcompat

RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
COPY --from=builder --chown=1001:root --link /app/target/*-runner /work/application

EXPOSE 8080
USER 1001

ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]