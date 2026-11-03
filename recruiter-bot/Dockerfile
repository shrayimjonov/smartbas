FROM internal.example.com/hub.docker.com/maven:3.9.3-eclipse-temurin-17-focal
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean install -DskipTests

FROM internal.example.com/docker/openjdk:17.0.2-jdk-slim-bullseye_sh
WORKDIR /app
COPY --from=0 /app/target/*.jar ./app.jar