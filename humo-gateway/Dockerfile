FROM internal.example.com/hub.docker.com/maven:3.9.3-eclipse-temurin-8-focal
ARG MAVEN_OPTS="-Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8080 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=8080"
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM internal.example.com/hub.docker.com/eclipse-temurin:8-jre-jammy
WORKDIR /app
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE $SPRING_PROFILES_ACTIVE
COPY --from=0 /app/target/*.war ./app.war
