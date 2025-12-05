package com.condigence.stockservice.mapper;

import com.condigence.stockservice.dto.ShopDTO;
import com.condigence.stockservice.entity.Shop;
import com.condigence.stockservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapping utilities for Shop <-> ShopDTO.
 */
@Component
public class ShopMapper {

    private final ImageService imageService;

    @Autowired
    public ShopMapper(ImageService imageService) {
        this.imageService = imageService;
    }

    public ShopDTO toDto(Shop shop) {
        if (shop == null) {
            return null;
        }
        ShopDTO dto = new ShopDTO();
        dto.setId(shop.getShopId());
        dto.setName(shop.getShopName());
        dto.setType(shop.getShopType());
        dto.setUserId(shop.getUserId());
        dto.setBranch(shop.getShopBranch());
        if (shop.getImageId() != null) {
            dto.setImageId(shop.getImageId());
            imageService.setPicIfPresent(dto, shop.getImageId());
        }
        return dto;
    }
}

