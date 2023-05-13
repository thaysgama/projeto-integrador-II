FROM maven:3.8.1-openjdk-17-slim AS build

COPY pom.xml /build/
COPY src /build/src

WORKDIR /build/

RUN mvn package

FROM openjdk:17-jdk-slim

COPY --from=build /build/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
