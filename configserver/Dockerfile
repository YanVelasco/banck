# syntax=docker/dockerfile:1.7

FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -Dmaven.test.skip=true dependency:go-offline

COPY src ./src/

RUN --mount=type=cache,target=/root/.m2 mvn -B -q -Dmaven.test.skip=true package

# Extract Spring Boot layers for better image layer caching
RUN java -Djarmode=tools -jar target/configserver-0.0.1-SNAPSHOT.jar extract --layers --launcher --destination extracted

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system spring \
    && useradd --system --create-home --home-dir /home/spring --gid spring spring \
    && mkdir -p /home/spring/.config/jgit /app \
    && chown -R spring:spring /home/spring /app

COPY --from=build /workspace/target/otel/opentelemetry-javaagent.jar /app/libs/opentelemetry-javaagent.jar
COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

EXPOSE 8071

ENV HOME=/home/spring

USER spring:spring

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "org.springframework.boot.loader.launch.JarLauncher"]
