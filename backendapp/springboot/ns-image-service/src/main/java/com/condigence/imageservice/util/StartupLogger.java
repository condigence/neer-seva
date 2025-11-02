package com.condigence.imageservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final Environment env;
    private final AppProperties appProperties;

    public StartupLogger(Environment env, AppProperties appProperties) {
        this.env = env;
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) {
        String[] profiles = env.getActiveProfiles();
        if (profiles == null || profiles.length == 0) {
            log.info("[StartupLogger] No active Spring profiles (using defaults)");
        } else {
            log.info("[StartupLogger] Active profiles: {}", String.join(",", profiles));
        }

        String dsUrl = env.getProperty("spring.datasource.url");
        String flyway = env.getProperty("spring.flyway.enabled");
        String eureka = env.getProperty("eureka.client.enabled");

        log.info("[StartupLogger] spring.datasource.url={}", dsUrl);
        log.info("[StartupLogger] spring.flyway.enabled={}", flyway);
        log.info("[StartupLogger] eureka.client.enabled={}", eureka);
        log.info("[StartupLogger] app.location={}", appProperties.getLocation());
        log.info("[StartupLogger] app.location(resolved)={}", appProperties.getResolvedLocation());
    }
}
