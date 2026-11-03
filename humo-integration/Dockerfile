FROM maven:3.9.3-eclipse-temurin-8-focal
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:8-jre-slim-bullseye
WORKDIR /app
COPY --from=0 /app/target/*.war ./app.war
