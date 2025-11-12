# NS Notification Service - Local docker-compose

This repo contains a Spring Boot notification service with Kafka integration.

You can start Zookeeper, Kafka and the service locally using Docker Compose.

Prerequisites
- Docker & Docker Compose
- (Optional) Java/Maven if you want to run locally without Docker

Important: when using the Maven wrapper, always provide a goal or lifecycle phase. Running `./mvnw` with no arguments will produce an error: "No goals have been specified for this build...". Use `./mvnw package`, `./mvnw spring-boot:run`, or similar.

Bring up the stack (app uses host Kafka by default)

```bash
# Build the app image and start only the app (it will connect to host Kafka at host.docker.internal:9099 by default)
docker compose build ns-notification-service
docker compose up --no-build ns-notification-service
```

If you want to run the bundled Kafka/Zookeeper (not recommended if you already run Kafka on the host):

```bash
# Start Kafka and Zookeeper plus the app (use the kafka profile)
docker compose --profile kafka up --build
```

Overriding Kafka bootstrap servers

The application reads Kafka settings from `kafka.bootstrap-servers` (property) and from the environment variable `KAFKA_BOOTSTRAP_SERVERS` (env var has precedence when set). You can override the Kafka bootstrap servers in several ways:

- Local run (Windows cmd.exe):

```cmd
set KAFKA_BOOTSTRAP_SERVERS=localhost:9099
mvnw.cmd -DskipTests spring-boot:run
```

- Pass as a system property to the JVM:

```cmd
mvnw.cmd -DskipTests package
java -Dkafka.bootstrap-servers=localhost:9099 -jar target\ns-notification-service-0.0.1-SNAPSHOT.jar
```

- Docker Compose: `docker-compose.yml` (the `ns-notification-service` service already sets `KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9099` so the container connects to the host machine on port 9099 by default). To change it, edit the compose or run:

```bash
KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9099 docker compose up --build ns-notification-service
```

Fail-fast validation

The application now validates Kafka configuration on startup. If no `kafka.bootstrap-servers` is configured (by property or env), the app will fail fast with a clear error message. This prevents the service from starting without a configured Kafka.

What this exposes
- Kafka broker on localhost:9092
- App on http://localhost:9100

Test publish endpoint
```bash
curl -X POST -H "Content-Type: application/json" -d '{"id":1,"toNumber":"+919999999999","fromNumber":"+17175239452","message":"Hello from Kafka"}' http://localhost:9100/neerseva/api/v1/notifications/publish
```

Notes
- The containerized app is built using a multi-stage Dockerfile. The Dockerfile uses the project's Maven wrapper to ensure consistent builds.
- The environment property `kafka.bootstrap-servers` is set via docker-compose to `kafka:9092` so the app connects to the Kafka container.
- If you see: "[ERROR] No goals have been specified for this build", it means Maven was invoked without a goal; provide one such as `package` or `spring-boot:run`.
