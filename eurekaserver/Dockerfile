# syntax=docker/dockerfile:1.7

FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -Dmaven.test.skip=true dependency:go-offline

COPY src ./src/

RUN --mount=type=cache,target=/root/.m2 mvn -B -q -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system spring \
    && useradd --system --create-home --home-dir /home/spring --gid spring spring \
    && mkdir -p /home/spring/.config/jgit /app \
    && chown -R spring:spring /home/spring /app

COPY --from=build /workspace/target/eurekaserver-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8071

ENV HOME=/home/spring

USER spring:spring

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
