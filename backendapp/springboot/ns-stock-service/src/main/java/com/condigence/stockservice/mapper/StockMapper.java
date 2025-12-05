package com.condigence.stockservice.mapper;

import com.condigence.stockservice.dto.ItemDTO;
import com.condigence.stockservice.dto.ShopDTO;
import com.condigence.stockservice.dto.StockDTO;
import com.condigence.stockservice.entity.Stock;
import com.condigence.stockservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapping utilities for Stock -> StockDTO.
 */
@Component
public class StockMapper {

    private final ImageService imageService;

    @Autowired
    public StockMapper(ImageService imageService) {
        this.imageService = imageService;
    }

    public StockDTO toDto(Stock stock, ItemDTO item, ShopDTO shopDTO) {
        if (stock == null) {
            return null;
        }
        StockDTO dto = new StockDTO();
        dto.setId(stock.getStockId());
        dto.setQuantity(stock.getStockQuantity());
        if (shopDTO != null) {
            dto.setShopId(shopDTO.getId());
        } else {
            dto.setShopId(stock.getShopId());
        }

        if (item != null) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setName(item.getName());
            itemDTO.setDispPrice(item.getDispPrice());
            itemDTO.setCapacity(item.getCapacity());
            itemDTO.setDiscount(item.getDiscount());
            itemDTO.setMrp(item.getMrp());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setCode(item.getCode());
            itemDTO.setBrandId(item.getBrandId());
            itemDTO.setImageId(item.getImageId());
            if (item.getImageId() != null) {
                imageService.setPicIfPresent(itemDTO, item.getImageId());
            }
            dto.setItem(itemDTO);
        }

        return dto;
    }
}

