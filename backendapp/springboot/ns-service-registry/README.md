# ns-service-registry

A Spring Boot Eureka Service Registry used by NeerSeva microservices.

Requirements
- Java 25 (Temurin/OpenJDK)
- Maven (or use the included Maven Wrapper)
- Docker (optional, for container builds)

Quick start (local)

1. Build the project:

```bash
./mvnw.cmd -DskipTests package
```

2. Run the application (you can set admin credentials via env vars):

```bash
set EUREKA_ADMIN_USER=admin
set EUREKA_ADMIN_PASSWORD=admin123
java -jar target/ns-service-registry-0.0.1-SNAPSHOT.jar
```

Run with Docker

1. Build the image:

```bash
docker build -t ns-service-registry:latest .
```

2. Run the container (example, pass credentials as env):

```bash
docker run -p 8761:8761 -e EUREKA_ADMIN_USER=admin -e EUREKA_ADMIN_PASSWORD=admin123 ns-service-registry:latest
```

CI

This repo includes a GitHub Actions workflow that builds and tests the project on push and pull requests to `main`.

Dependabot

Dependabot is configured to open weekly PRs for Maven dependency updates.

Notes
- If you see warnings about native access when running the Maven wrapper on Java 25, add MAVEN_OPTS:

```bash
set MAVEN_OPTS=--enable-native-access=ALL-UNNAMED
./mvnw.cmd -DskipTests package
```

- If your Windows `JAVA_HOME` has a trailing space, it can appear corrupted. Fix with:

```bash
setx JAVA_HOME "C:\Program Files\Java\jdk-21"
```
