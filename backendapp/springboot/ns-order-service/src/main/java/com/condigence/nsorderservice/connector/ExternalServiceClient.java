package com.condigence.nsorderservice.connector;

import com.condigence.nsorderservice.dto.*;
import com.condigence.nsorderservice.exception.BadRequestException;
import com.condigence.nsorderservice.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ShopDTO getShop(Long shopId) {
        try {
            return restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/shops/" + shopId, ShopDTO.class);
        } catch (HttpClientErrorException.NotFound nf) {
            String body = safeBody(nf);
            logger.debug("Shop not found: {}", body);
            throw new ResourceNotFoundException("Shop not found: " + body);
        } catch (HttpClientErrorException hce) {
            String body = safeBody(hce);
            logger.warn("Error fetching shop {}: {}", shopId, body);
            throw new BadRequestException("Error fetching shop: " + body);
        } catch (RestClientException rce) {
            logger.error("RestClientException fetching shop {}: {}", shopId, rce.getMessage());
            throw new BadRequestException("Error fetching shop: " + rce.getMessage());
        }
    }

    public ItemDTO getItem(Long itemId) {
        try {
            return restTemplate.getForObject("http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/" + itemId, ItemDTO.class);
        } catch (HttpClientErrorException.NotFound nf) {
            String body = safeBody(nf);
            throw new ResourceNotFoundException("Item not found: " + body);
        } catch (RestClientException rce) {
            throw new BadRequestException("Error fetching item: " + rce.getMessage());
        }
    }

    public ImageDTO getImage(Long imageId) {
        try {
            return restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data", ImageDTO.class);
        } catch (Exception e) {
            logger.debug("Failed to fetch image {}: {}", imageId, e.getMessage());
            return null;
        }
    }

    public UserDTO getUser(Long userId) {
        try {
            return restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/" + userId, UserDTO.class);
        } catch (Exception e) {
            logger.debug("Failed to fetch user {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public Boolean updateStockOnOrder(OrderDetailDTO orderDetailDTO) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/update/on/order", orderDetailDTO, Boolean.class);
            if (response.getStatusCode().is2xxSuccessful()) return Boolean.TRUE;
            String body = response.hasBody() ? String.valueOf(response.getBody()) : "";
            throw new BadRequestException("Stock update failed: " + body);
        } catch (HttpClientErrorException.NotFound nf) {
            String body = safeBody(nf);
            logger.warn("Stock not found: {}", body);
            throw new ResourceNotFoundException(bodyContainsMessage(body) ? extractMessage(body) : "Stock not found: " + body);
        } catch (HttpClientErrorException.BadRequest br) {
            String body = safeBody(br);
            logger.warn("Stock service returned BadRequest: {}", body);
            throw new BadRequestException(bodyContainsMessage(body) ? extractMessage(body) : "Stock service returned bad request: " + body);
        } catch (HttpClientErrorException hce) {
            String body = safeBody(hce);
            logger.warn("Stock update client error: {}", body);
            throw new BadRequestException(body);
        } catch (RestClientException rce) {
            logger.error("Stock update RestClientException: {}", rce.getMessage());
            throw new BadRequestException("Stock service error: " + rce.getMessage());
        }
    }

    private String safeBody(HttpClientErrorException hce) {
        try {
            return hce.getResponseBodyAsString();
        } catch (Exception e) {
            return hce.getMessage();
        }
    }

    private boolean bodyContainsMessage(String body) {
        return body != null && (body.contains("errorMessage") || body.contains("message") || body.contains("error"));
    }

    private String extractMessage(String json) {
        try {
            var map = objectMapper.readValue(json, java.util.Map.class);
            Object em = map.get("errorMessage");
            if (em == null) em = map.get("message");
            if (em == null) em = map.get("error");
            return em != null ? em.toString() : json;
        } catch (Exception e) {
            return json;
        }
    }
}

