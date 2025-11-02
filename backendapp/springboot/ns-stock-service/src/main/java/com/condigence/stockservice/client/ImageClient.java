package com.condigence.stockservice.client;

import com.condigence.stockservice.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import com.condigence.stockservice.util.AppProperties;

@Component
public class ImageClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    // Simple in-memory circuit breaker state (configurable)
    private int failureCount = 0;
    private Instant openUntil = Instant.EPOCH;

    /**
     * Fetch image pic for given imageId. Implements a small circuit-breaker + fallback.
     * Returns the pic bytes or null when not available.
     */
    public synchronized byte[] fetchImagePic(Long imageId) {
        if (imageId == null) return null;

        // read config
        AppProperties.Image.Circuit circuit = appProperties.getImage().getCircuit();
        int failureThreshold = Math.max(1, circuit.getFailureThreshold());
        long openMillis = Math.max(0L, circuit.getOpenMillis());
        boolean resetOnSuccess = circuit.isResetOnSuccess();

        // If the circuit is open and not yet half-open, short-circuit
        Instant now = Instant.now();
        if (now.isBefore(openUntil)) {
            logger.warn("Circuit open for image service until {}. Skipping remote call for imageId {}", openUntil, imageId);
            return null;
        }

        String[] urls = new String[] {
                "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data",
                "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId
        };
        for (String url : urls) {
            try {
                ImageDTO imageDTO = restTemplate.getForObject(url, ImageDTO.class);
                if (imageDTO != null && imageDTO.getPic() != null) {
                    // success -> reset failures if configured
                    if (resetOnSuccess) {
                        failureCount = 0;
                    }
                    return imageDTO.getPic();
                }
            } catch (RestClientException ex) {
                logger.debug("Image fetch failed for url {}: {}", url, ex.getMessage());
                // count failure and potentially open circuit
                failureCount++;
                if (failureCount >= failureThreshold) {
                    openUntil = Instant.now().plusMillis(openMillis);
                    logger.warn("Opening circuit for image service until {} after {} failures", openUntil, failureCount);
                }
            } catch (Exception ex) {
                logger.debug("Unexpected image fetch failure for url {}: {}", url, ex.getMessage());
                failureCount++;
                if (failureCount >= failureThreshold) {
                    openUntil = Instant.now().plusMillis(openMillis);
                    logger.warn("Opening circuit for image service until {} after {} failures", openUntil, failureCount);
                }
            }
        }
        logger.warn("Unable to fetch image for imageId {}", imageId);
        return null;
    }
}
