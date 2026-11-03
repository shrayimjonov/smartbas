FROM internal.example.com/hub.docker.com/openjdk:17.0.2-jdk
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Tashkent","-jar","app.jar"]