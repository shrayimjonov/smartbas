# Build stage
FROM internal.example.com/hub.docker.com/eclipse-temurin:21-jdk
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Tashkent","-jar","app.jar"]