FROM eclipse-temurin:21-jdk-jammy

MAINTAINER YanVelasco

WORKDIR /app

COPY target/cards-0.0.1-SNAPSHOT.jar cards.jar

ENTRYPOINT ["java","-jar","cards.jar"]
