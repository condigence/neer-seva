# NS Messaging Service

Converted to a Maven-based Spring Boot 3 (Java 21) project with RabbitMQ and H2 integration.

What I changed/added

- Added `pom.xml` (Spring Boot 3.2.6, Java 21)
- Added `NsMessagingServiceApplication` (Spring Boot application entrypoint)
- RabbitMQ configuration: `RabbitMQConfig` (queue, exchange, routing key, Jackson JSON converter, RabbitTemplate)
- Domain/entity: `MessageEntity`
- Repository: `MessageRepository` (Spring Data JPA)
- DTO: `MessageRequest`
- Service: `MessageService` (publishes to RabbitMQ and persists messages; listens to queue and stores incoming messages)
- Controller: `MessageApiController` (POST /api/v1/messages to publish & save, GET /api/v1/messages to list)
- `application.yml` with H2 in-memory datasource and RabbitMQ default connection settings

Files created

- `pom.xml`
- `src/main/java/com/condigence/messagingservice/NsMessagingServiceApplication.java`
- `src/main/java/com/condigence/messagingservice/config/RabbitMQConfig.java`
- `src/main/java/com/condigence/messagingservice/entity/MessageEntity.java`
- `src/main/java/com/condigence/messagingservice/repo/MessageRepository.java`
- `src/main/java/com/condigence/messagingservice/payload/MessageRequest.java`
- `src/main/java/com/condigence/messagingservice/service/MessageService.java`
- `src/main/java/com/condigence/messagingservice/controller/MessageApiController.java`
- `src/main/resources/application.yml`

How to build & run (Windows - cmd)

1) Ensure Java 21 (JDK 21) and Maven are installed and on PATH.

2) Build the project (skip tests for a quick build):

```
cd d:\gitrepo\neer-seva\backendapp\springboot\ns-messaging-service
mvn -DskipTests package
```

3) Run the application:

```
mvn spring-boot:run
```

or

```
java -jar target/ns-messaging-service-0.0.1-SNAPSHOT.jar
```

Configuration notes

- RabbitMQ: application.yml targets `localhost:5672` with `guest/guest`. If your RabbitMQ uses different credentials or host, update `src/main/resources/application.yml`.
- H2 console available at: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:messagingdb`
  - User: `sa` (no password)

API examples

- Publish a message (this will persist on sender side and publish to RabbitMQ):

```
curl -X POST -H "Content-Type: application/json" -d "{\"content\":\"hello from curl\"}" http://localhost:8080/api/v1/messages
```

- List persisted messages:

```
curl http://localhost:8080/api/v1/messages
```

Behavior

- `POST /api/v1/messages` does two things:
  1. Saves a `MessageEntity` in H2 (sender side)
  2. Publishes the `MessageRequest` to RabbitMQ using exchange `messages.exchange` and routing key `messages.routingkey`.
- The application also contains a `@RabbitListener` on queue `messages.queue` that receives messages and persists them again (consumer side). This demonstrates end-to-end flow: publish -> RabbitMQ -> consumer -> DB.

Limitations / environment notes

- I created the Maven project and source files, but I couldn't run `mvn` in this environment (maven not installed in the runner). If `mvn` is missing locally, please install Maven or add the Maven Wrapper.

Next steps (optional)

- Remove or adapt existing Kafka classes if you intend to use RabbitMQ only.
- Add integration tests and a simple Docker Compose to run RabbitMQ for development.
- Add the Maven Wrapper (`mvn -N io.takari:maven:wrapper`) so users without Maven can build.

If you'd like, I can:
- Add a Docker Compose for RabbitMQ + app
- Remove Kafka-related files or keep both messaging systems
- Add a Maven Wrapper into the repo

## Docker Compose (RabbitMQ + app)

A `docker-compose.yml` and `Dockerfile` have been added to run RabbitMQ (management UI) and the application in containers.

Start both services with:

```bash
cd D:\gitrepo\neer-seva\backendapp\springboot\ns-messaging-service
docker-compose up --build
```

- RabbitMQ Management UI: http://localhost:15672 (guest/guest)
- App HTTP API: http://localhost:8080

This is useful if you don't want to install RabbitMQ or Maven locally.

## Maven Wrapper

Minimal `mvnw` and `mvnw.cmd` scripts and a `maven-wrapper.properties` file were added. The actual `maven-wrapper.jar` is not included here (it must be generated locally). To generate the wrapper and the jar locally run:

```bash
# Requires Maven installed locally (for this single command)
mvn -N io.takari:maven:wrapper
```

After that you can run builds using the wrapper scripts:

Windows:

```bat
mvnw.cmd -DskipTests package
```

Unix/macOS:

```bash
./mvnw -DskipTests package
```

If you prefer, install Maven on your machine and use `mvn` directly.
