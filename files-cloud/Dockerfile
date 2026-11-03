FROM internal.example.com/hub.docker.com/maven:3.9.3-eclipse-temurin-17-focal AS build
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean install -DskipTests

FROM internal.example.com/hub.docker.com/openjdk:17.0.2-jdk-slim-bullseye
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar

# Define ARG for build time
ARG JASYPT_PASSWORD_ZI
ENV JASYPT_PASSWORD_ZI=${JASYPT_PASSWORD_ZI}

ENTRYPOINT ["sh", "-c", "java -Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD} -jar app.jar"]