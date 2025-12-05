package com.condigence.imageservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestConnector {
    private static final Logger logger = LoggerFactory.getLogger(RestConnector.class);
    private final RestTemplate restTemplate;

    public RestConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        logger.debug("RestConnector GET {}", url);
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        logger.debug("RestConnector exchange {} {}", method, url);
        try {
            return restTemplate.exchange(url, method, entity, responseType);
        } catch (RestClientException ex) {
            logger.error("Exchange failed {} {}: {}", method, url, ex.getMessage());
            throw ex;
        }
    }
}
