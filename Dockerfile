# syntax=docker/dockerfile:1.6

# 1) Build stage
FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn -q -Dmaven.test.skip=true package

# 2) Runtime stage
FROM eclipse-temurin:24-jre
WORKDIR /app

COPY --from=build /src/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
