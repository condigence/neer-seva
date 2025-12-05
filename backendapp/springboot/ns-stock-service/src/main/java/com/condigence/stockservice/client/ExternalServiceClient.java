package com.condigence.stockservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T getForObject(String url, Class<T> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (RestClientException ex) {
            logger.warn("Rest call failed for url {}: {}", url, ex.getMessage());
            return null;
        } catch (Exception ex) {
            logger.warn("Unexpected error while calling {}: {}", url, ex.getMessage());
            return null;
        }
    }
}

