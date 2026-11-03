FROM internal.example.com/hub.docker.com/maven:3.9.3-eclipse-temurin-17-focal
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN mvn clean install -DskipTests

FROM internal.example.com/hub.docker.com/eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=0 /app/target/*.jar ./app.jar
CMD ["java","-jar","app.jar"]