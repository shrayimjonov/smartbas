FROM maven:3.9.6-eclipse-temurin-21-jammy
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests


FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE $SPRING_PROFILES_ACTIVE
COPY --from=0 /app/target/*.jar ./app.jar
