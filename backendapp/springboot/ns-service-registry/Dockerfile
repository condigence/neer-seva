# Multi-stage Dockerfile for building and running the Spring Boot app on Java 25
# Build stage
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Copy only what we need to run maven build efficiently
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw
COPY pom.xml pom.xml
COPY src src

# Build the application (skip tests by default to speed up builds; change if you want tests in CI)
RUN ./mvnw -B -DskipTests package

# Run stage
FROM eclipse-temurin:25-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/${JAR_FILE} app.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","/app/app.jar"]

