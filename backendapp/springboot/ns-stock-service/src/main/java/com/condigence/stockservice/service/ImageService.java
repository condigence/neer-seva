package com.condigence.stockservice.service;

import com.condigence.stockservice.client.ImageClient;
import com.condigence.stockservice.dto.ItemDTO;
import com.condigence.stockservice.dto.ShopDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Helper service for dealing with images on DTOs.
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageClient imageClient;

    @Autowired
    public ImageService(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    /**
     * Best-effort population of an item's pic field from the image service.
     */
    public void setPicIfPresent(ItemDTO dto, Long imageId) {
        if (dto == null || imageId == null) {
            return;
        }
        try {
            byte[] pic = imageClient.fetchImagePic(imageId);
            if (pic != null) {
                dto.setPic(pic);
            }
        } catch (Exception ex) {
            logger.warn("Failed to fetch image {}: {}", imageId, ex.getMessage());
        }
    }

    public void setPicIfPresent(ShopDTO dto, Long imageId) {
        if (dto == null || imageId == null) {
            return;
        }
        try {
            byte[] pic = imageClient.fetchImagePic(imageId);
            if (pic != null) {
                dto.setPic(pic);
            }
        } catch (Exception ex) {
            logger.warn("Failed to fetch image {}: {}", imageId, ex.getMessage());
        }
    }
}
