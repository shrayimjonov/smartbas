FROM internal.example.com/hub.docker.com/maven:3.9.9-eclipse-temurin-17-alpine
WORKDIR /app
COPY . .
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN chmod +x mvnw
RUN mvn clean install -DskipTests
RUN ln -sf /usr/share/zoneinfo/Asia/Tashkent /etc/localtime && echo "Asia/Tashkent" > /etc/timezone



FROM internal.example.com/bas/openjdk-17-net:latest
WORKDIR /app
COPY --from=0 /app/target/*.jar ./app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Tashkent /etc/localtime && echo "Asia/Tashkent" > /etc/timezone

