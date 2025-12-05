package com.condigence.stockservice.service;

import com.condigence.stockservice.client.ProductClient;
import com.condigence.stockservice.dto.ItemDTO;
import com.condigence.stockservice.dto.ShopDTO;
import com.condigence.stockservice.dto.StockDTO;
import com.condigence.stockservice.entity.Stock;
import com.condigence.stockservice.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Facade for common stock queries that return DTOs.
 */
@Service
public class StockQueryService {

    private final StockService stockService;
    private final ShopService shopService;
    private final ProductClient productClient;
    private final StockMapper stockMapper;

    @Autowired
    public StockQueryService(StockService stockService,
                             ShopService shopService,
                             ProductClient productClient,
                             StockMapper stockMapper) {
        this.stockService = stockService;
        this.shopService = shopService;
        this.productClient = productClient;
        this.stockMapper = stockMapper;
    }

    public Set<ShopDTO> getAllShopsForStock() {
        List<Stock> stocks = stockService.getAllStocks();
        if (stocks.isEmpty()) {
            return new HashSet<>();
        }
        return shopService.getShopDtosFromStocks(stocks);
    }

    public List<StockDTO> getStocksByShopId(Long shopId) {
        List<Stock> stocks = stockService.getStockByShopId(shopId);
        List<StockDTO> dtos = new ArrayList<>();
        for (Stock stock : stocks) {
            ItemDTO item = productClient.getItemById(stock.getItemId());
            ShopDTO shopDTO = shopService.getShopDtoById(stock.getShopId()).orElse(null);
            StockDTO dto = stockMapper.toDto(stock, item, shopDTO);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    public List<StockDTO> getStocksItemByShopId(Long shopId) {
        List<StockDTO> dtos = new ArrayList<>();
        List<Stock> stocks = stockService.getStockByShopId(shopId);
        if (stocks == null || stocks.isEmpty()) {
            return dtos;
        }

        for (Stock stock : stocks) {
            if (stock == null) continue;
            if (stock.getStockQuantity() <= 0) {
                continue;
            }
            ItemDTO item = productClient.getItemById(stock.getItemId());
            ShopDTO shopDTO = shopService.getShopDtoById(stock.getShopId()).orElse(null);
            StockDTO dto = stockMapper.toDto(stock, item, shopDTO);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    public List<StockDTO> getAllStockDtos() {
        List<Stock> stocks = stockService.getAllStocks();
        List<StockDTO> dtos = new ArrayList<>();
        for (Stock stock : stocks) {
            ItemDTO item = productClient.getItemById(stock.getItemId());
            ShopDTO shopDTO = shopService.getShopDtoById(stock.getShopId()).orElse(null);
            StockDTO dto = stockMapper.toDto(stock, item, shopDTO);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }
}

