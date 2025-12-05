package com.condigence.imageservice.service;

import com.condigence.imageservice.util.RestConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);

    private final RestConnector restConnector;
    private final String externalPingUrl;

    public ExternalService(RestConnector restConnector, @Value("${external.ping.url:}") String externalPingUrl) {
        this.restConnector = restConnector;
        this.externalPingUrl = externalPingUrl;
    }

    public ResponseEntity<String> pingExternal() {
        if (externalPingUrl == null || externalPingUrl.isBlank()) {
            logger.debug("external.ping.url not configured");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("external.ping.url not configured");
        }
        try {
            return restConnector.get(externalPingUrl, String.class);
        } catch (Exception ex) {
            logger.warn("External ping failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("ping failed: " + ex.getMessage());
        }
    }
}
