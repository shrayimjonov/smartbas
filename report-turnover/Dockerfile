FROM internal.example.com/docker/openjdk:17.0.2-jdk-slim-bullseye_sh
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Tashkent","-jar","app.jar"]
