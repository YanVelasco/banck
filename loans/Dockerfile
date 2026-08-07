FROM eclipse-temurin:21-jdk-jammy

MAINTAINER YanVelasco

WORKDIR /app

COPY target/loans-0.0.1-SNAPSHOT.jar loans.jar

ENTRYPOINT ["java","-jar","loans.jar"]
