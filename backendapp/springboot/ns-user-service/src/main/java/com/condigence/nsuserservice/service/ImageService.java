package com.condigence.nsuserservice.service;

import com.condigence.nsuserservice.dto.ImageDTO;
import com.condigence.nsuserservice.util.AppProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Dedicated service for fetching user images from the Image microservice.
 * Provides caching (simple in-memory TTL) + circuit breaker + fallback.
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    // Simple in-memory cache: imageId -> pic (byte[])
    private final ConcurrentHashMap<Long, CachedImage> cache = new ConcurrentHashMap<>();
    private final long ttlMs = TimeUnit.MINUTES.toMillis(5); // 5 minute TTL

    private static class CachedImage {
        final byte[] pic; final long ts;
        CachedImage(byte[] p, long t){ this.pic = p; this.ts = t; }
    }

    @Autowired
    public ImageService(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }

    /**
     * Public entry point used by controllers. Returns null if not available.
     */
    public byte[] getImage(Long imageId) {
        if (imageId == null) return null;
        // Check cache first
        CachedImage ci = cache.get(imageId);
        if (ci != null && !isExpired(ci)) {
            return ci.pic;
        }
        try {
            byte[] fresh = fetchImageRemote(imageId); // circuit breaker protected
            if (fresh != null) {
                cache.put(imageId, new CachedImage(fresh, System.currentTimeMillis()));
            }
            return fresh;
        } catch (Exception ex) {
            logger.warn("Image fetch failed for id {}: {}", imageId, ex.getMessage());
            return fallbackBytes(imageId); // may be null
        }
    }

    private boolean isExpired(CachedImage ci) {
        return (System.currentTimeMillis() - ci.ts) > ttlMs;
    }

    /**
     * Circuit breaker protected remote call. Throws if blank to trigger fallback.
     */
    @CircuitBreaker(name = "imageService", fallbackMethod = "circuitFallback")
    protected byte[] fetchImageRemote(Long imageId) {
        String url = "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data";
        logger.debug("Calling Image Service: {}", url);
        ImageDTO imageDTO;
        try {
            imageDTO = restTemplate.getForObject(url, ImageDTO.class);
        } catch (HttpClientErrorException.NotFound nf) {
            logger.info("Image not found for id {}", imageId);
            return null; // treat missing image as non-error; no fallback needed
        } catch (RestClientException rce) {
            logger.warn("RestClientException contacting image service for id {}: {}", imageId, rce.getMessage());
            throw rce; // propagate to circuit breaker fallback
        }
        if (imageDTO == null || imageDTO.getPic() == null) {
            logger.warn("ImageDTO null or empty pic for id {}", imageId);
            throw new RestClientException("Empty image data for id " + imageId);
        }
        return imageDTO.getPic();
    }

    /**
     * Circuit breaker fallback signature must match (Long, Throwable).
     */
    @SuppressWarnings("unused")
    protected byte[] circuitFallback(Long imageId, Throwable t) {
        logger.warn("Circuit breaker fallback for imageId {} due to: {}", imageId, t == null ? "unknown" : t.getMessage());
        // Try cached stale value
        CachedImage ci = cache.get(imageId);
        if (ci != null) {
            return ci.pic; // even if expired, better than nothing
        }
        return fallbackBytes(imageId);
    }

    /**
     * Decode configured fallback image (base64) if present, else null.
     */
    private byte[] fallbackBytes(Long imageId) {
        String fb = appProperties.getFallbackImage();
        if (fb == null || fb.trim().isEmpty()) return null;
        try {
            return Base64.getDecoder().decode(fb);
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid base64 fallback image configured: {}", ex.getMessage());
            return null;
        }
    }
}

