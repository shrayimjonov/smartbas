# Stage 1: Maven build
FROM internal.example.com/hub.docker.com/maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean
RUN mvn javadoc:javadoc
RUN mvn install -DskipTests

# Stage 2: Runtime image
FROM internal.example.com/hub.docker.com/amazoncorretto:21-alpine3.22

WORKDIR /app

# Копируем jar
COPY --from=build /app/target/*.jar ./app.jar

# Копируем Javadoc → /app/docs
COPY --from=build /app/target/reports/apidocs /home/

