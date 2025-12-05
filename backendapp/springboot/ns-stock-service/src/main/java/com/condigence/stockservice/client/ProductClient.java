package com.condigence.stockservice.client;

import com.condigence.stockservice.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Client wrapper around the product (item) service.
 */
@Component
public class ProductClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    private final ExternalServiceClient externalClient;

    @Autowired
    public ProductClient(ExternalServiceClient externalClient) {
        this.externalClient = externalClient;
    }

    public ItemDTO getItemById(long itemId) {
        String url = "http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/" + itemId;
        try {
            return externalClient.getForObject(url, ItemDTO.class);
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching item {} from product service: {}", itemId, ex.getMessage());
            return null;
        }
    }
}
