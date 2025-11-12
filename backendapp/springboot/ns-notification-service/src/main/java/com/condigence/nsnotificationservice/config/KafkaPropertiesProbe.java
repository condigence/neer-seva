package com.condigence.nsnotificationservice.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class KafkaPropertiesProbe implements ApplicationRunner {

    private final KafkaProperties kafkaProperties;
    private final Environment environment;

    public KafkaPropertiesProbe(KafkaProperties kafkaProperties, Environment environment) {
        this.kafkaProperties = kafkaProperties;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Explicitly read properties via Environment#getProperty so static analysis picks up usage
        String envBootstrap = environment.getProperty("KAFKA_BOOTSTRAP_SERVERS");
        String propBootstrap = environment.getProperty("kafka.bootstrap-servers");
        String effectiveBootstrap = envBootstrap != null && !envBootstrap.isBlank() ? envBootstrap : (propBootstrap != null && !propBootstrap.isBlank() ? propBootstrap : kafkaProperties.getBootstrapServers());

        String propTopic = environment.getProperty("kafka.topic");
        String effectiveTopic = propTopic != null && !propTopic.isBlank() ? propTopic : kafkaProperties.getTopic();

        System.out.println("[StartupValidator] kafka.bootstrap-servers from env KAFKA_BOOTSTRAP_SERVERS=" + envBootstrap + ", property kafka.bootstrap-servers=" + propBootstrap + ", kafkaProperties.getBootstrapServers()=" + kafkaProperties.getBootstrapServers());
        System.out.println("[StartupValidator] kafka.topic from property kafka.topic=" + propTopic + ", kafkaProperties.getTopic()=" + kafkaProperties.getTopic());

        // Fail fast if no bootstrap servers configured
        if (effectiveBootstrap == null || effectiveBootstrap.isBlank()) {
            throw new IllegalStateException("Missing Kafka bootstrap servers. Provide 'kafka.bootstrap-servers' in application.properties or set env KAFKA_BOOTSTRAP_SERVERS.");
        }

        // Optionally, you could also validate that the topic is set
        if (effectiveTopic == null || effectiveTopic.isBlank()) {
            throw new IllegalStateException("Missing Kafka topic. Provide 'kafka.topic' in application.properties.");
        }

        // No exceptions -> print effective values (already partly printed above)
        System.out.println("[StartupValidator] Using Kafka bootstrap servers: " + effectiveBootstrap + ", topic: " + effectiveTopic);
    }
}
