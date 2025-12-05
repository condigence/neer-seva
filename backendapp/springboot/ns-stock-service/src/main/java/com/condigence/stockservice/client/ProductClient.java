package com.condigence.stockservice.client;

import com.condigence.stockservice.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Client wrapper around the product (item) service.
 *
 * Currently uses the same hard-coded URL pattern that was in StockController
 * to avoid changing behaviour. This can be externalised to configuration later.
 */
@Component
public class ProductClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ItemDTO getItemById(long itemId) {
        String url = "http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/" + itemId;
        try {
            return restTemplate.getForObject(url, ItemDTO.class);
        } catch (RestClientException ex) {
            logger.error("Failed to fetch item {} from product service: {}", itemId, ex.getMessage());
            return null;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching item {} from product service: {}", itemId, ex.getMessage());
            return null;
        }
    }
}
