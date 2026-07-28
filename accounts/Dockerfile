FROM eclipse-temurin:21-jdk-jammy

MAINTAINER YanVelasco

WORKDIR /app

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts.jar

ENTRYPOINT ["java","-jar","accounts.jar"]