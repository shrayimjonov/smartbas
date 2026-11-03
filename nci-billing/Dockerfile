FROM maven:3.9.3-eclipse-temurin-17-focal
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean install -DskipTests

FROM openjdk:22-slim-bullseye
WORKDIR /app
COPY --from=0 /app/target/*.jar ./app.jar
