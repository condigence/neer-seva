package com.condigence.nsnotificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
@SuppressWarnings("unused")
public class KafkaProperties {

    /** bootstrap servers list, e.g. host.docker.internal:9099 or localhost:9099 */
    private String bootstrapServers; // removed hardcoded default to read purely from properties

    /** topic for notifications */
    private String topic = "ns-notification-topic";

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
