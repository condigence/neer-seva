package com.condigence.stockservice.client;

import com.condigence.stockservice.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.condigence.stockservice.util.AppProperties;

@Component
public class ImageClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    /**
     * Fetch image pic for given imageId using a resilience4j-backed circuit breaker.
     * Returns the pic bytes or null when not available or when the circuit prevents calls.
     */
    public byte[] fetchImagePic(Long imageId) {
        if (imageId == null) return null;

        String serviceName = "imageService"; // circuit name

        // Build the supplier that will fetch the image DTO
        try {
            return circuitBreakerFactory.create(serviceName).run(() -> {
                // try primary endpoint then fallback endpoint
                String[] urls = new String[] {
                        "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data",
                        "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId
                };
                for (String url : urls) {
                    try {
                        ImageDTO imageDTO = restTemplate.getForObject(url, ImageDTO.class);
                        if (imageDTO != null && imageDTO.getPic() != null) {
                            return imageDTO.getPic();
                        }
                    } catch (RestClientException ex) {
                        logger.debug("Image fetch failed for url {}: {}", url, ex.getMessage());
                        // try next url
                    } catch (Exception ex) {
                        logger.debug("Unexpected image fetch failure for url {}: {}", url, ex.getMessage());
                    }
                }
                return null;
            }, throwable -> {
                // fallback when circuit is open or call fails: return null
                logger.warn("Image fetch blocked by circuit or failed for imageId {}: {}", imageId, throwable == null ? "unknown" : throwable.getMessage());
                return null;
            });
        } catch (Exception ex) {
            logger.warn("Unexpected error in image fetch for imageId {}: {}", imageId, ex.getMessage());
            return null;
        }
    }
}
