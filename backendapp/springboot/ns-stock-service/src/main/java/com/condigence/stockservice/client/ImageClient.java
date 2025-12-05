package com.condigence.stockservice.client;

import com.condigence.stockservice.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageClient.class);

    private final ExternalServiceClient externalClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public ImageClient(ExternalServiceClient externalClient, CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.externalClient = externalClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    /**
     * Fetch image pic for given imageId using a resilience-backed circuit breaker.
     * Returns the pic bytes or null when not available or when the circuit prevents calls.
     */
    public byte[] fetchImagePic(Long imageId) {
        if (imageId == null) return null;

        String serviceName = "imageService"; // circuit name

        try {
            return circuitBreakerFactory.create(serviceName).run(() -> {
                String[] urls = new String[] {
                        "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data",
                        "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId
                };
                for (String url : urls) {
                    try {
                        ImageDTO imageDTO = externalClient.getForObject(url, ImageDTO.class);
                        if (imageDTO != null && imageDTO.getPic() != null) {
                            return imageDTO.getPic();
                        }
                    } catch (Exception ex) {
                        logger.debug("Image fetch failed for url {}: {}", url, ex.getMessage());
                    }
                }
                return null;
            }, throwable -> {
                logger.warn("Image fetch blocked by circuit or failed for imageId {}: {}", imageId, throwable == null ? "unknown" : throwable.getMessage());
                return null;
            });
        } catch (Exception ex) {
            logger.warn("Unexpected error in image fetch for imageId {}: {}", imageId, ex.getMessage());
            return null;
        }
    }
}
