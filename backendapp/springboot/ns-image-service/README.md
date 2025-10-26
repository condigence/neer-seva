# ns-image-service (NeerSeva Image Service)

This service stores and serves images for the NeerSeva project.

Requirements
- Java 21 (the project compiles to release 21)
- Maven (the wrapper is included)

Build
```bat
cd /d D:\gitrepo\neer-seva\backendapp\springboot\ns-image-service
mvnw.cmd -DskipTests package
```

Run (local, without Eureka)
```bat
# run with the local profile which disables Eureka and uses an in-memory H2 DB
java -jar -Dspring.profiles.active=local target\ns-image-service-0.0.1-SNAPSHOT.jar
```

Run (with Eureka)
- Ensure an Eureka server is running and reachable. You can include the Eureka client dependency at build time by activating the Maven profile:
```bat
# builds with the eureka dependency included
mvnw.cmd -Peureka -DskipTests package
# then run normally
java -jar target\ns-image-service-0.0.1-SNAPSHOT.jar
```

Tests
```bat
mvnw.cmd test
```

Profiles
- local: `application-local.properties` disables Eureka and configures an in-memory H2 DB for local runs and tests.
- eureka: a Maven profile that adds the Eureka client dependency at build time. Disabled by default to avoid pulling older transitive Eureka libs unless needed.

Notes
- I migrated JPA imports to Jakarta and replaced the old Feign starter with OpenFeign.
- If you want me to also bump Spring Boot and Spring Cloud to the latest patch versions and address transitive CVEs, say so and I'll apply and test those upgrades next.
