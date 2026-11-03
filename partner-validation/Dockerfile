FROM internal.example.com/hub.docker.com/maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean install -DskipTests

FROM internal.example.com/hub.docker.com/eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
