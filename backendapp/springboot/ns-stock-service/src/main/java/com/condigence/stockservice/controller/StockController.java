package com.condigence.stockservice.controller;


import com.condigence.stockservice.bean.StockBean;
import com.condigence.stockservice.client.ProductClient;
import com.condigence.stockservice.dto.*;
import com.condigence.stockservice.entity.Shop;
import com.condigence.stockservice.entity.Stock;
import com.condigence.stockservice.mapper.ShopMapper;
import com.condigence.stockservice.mapper.StockMapper;
import com.condigence.stockservice.service.ImageService;
import com.condigence.stockservice.service.ShopService;
import com.condigence.stockservice.service.StockService;
import com.condigence.stockservice.service.StockQueryService;
import com.condigence.stockservice.util.AppProperties;
import com.condigence.stockservice.util.CustomErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api/v1/stocks")
@Tag(name = "Stocks", description = "Operations pertaining to stock and shops")
public class StockController {

    public static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockQueryService stockQueryService;

    @Autowired
    public void setApp(AppProperties app) {
        this.app = app;
    }

    private AppProperties app;

    //////////////// Shops //////////////////

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShopsById(@PathVariable("id") Long id) {

        Optional<ShopDTO> shopDtoOpt = shopService.getShopDtoById(id);
        if (shopDtoOpt.isPresent()) {
            return ResponseEntity.ok(shopDtoOpt.get());
        } else {
            logger.error("Unable to Find. Shop with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Unable to Find. Shop with id " + id + " not found."));
        }
    }

    @GetMapping("/shops/list")
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        List<ShopDTO> dtos = shopService.getAllShopDtos();
        if (!dtos.isEmpty()) {
            return ResponseEntity.ok(dtos);
        } else {
            logger.error("Unable to Find Shops");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dtos);
        }
    }

    @GetMapping("/shops")
    public ResponseEntity<Set<ShopDTO>> getAllShopsForStock() {
        Set<ShopDTO> dtos = stockQueryService.getAllShopsForStock();
        if (!dtos.isEmpty()) {
            return ResponseEntity.ok(dtos);
        } else {
            logger.error("Unable to Find Shops");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dtos);
        }
    }


    @GetMapping("/shops/vendor/{id}")
    public ResponseEntity<?> getShopsByVendorId(@PathVariable("id") Long id) {

        List<ShopDTO> dtos = shopService.getShopDtosByVendorId(id);
        if (dtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Shop not found."));
        }
        return ResponseEntity.ok(dtos);

    }

    @GetMapping("/by/shop/{id}")
    @Operation(summary = "Get all stock entries for a shop", description = "Returns a list of StockDTO for the given shop id, including item and quantity details where available.")
    public ResponseEntity<List<StockDTO>> getStocksByShopId(@PathVariable("id") Long id) {
        List<StockDTO> dtos = stockQueryService.getStocksByShopId(id);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @DeleteMapping(value = "/shops/{id}")
    public ResponseEntity<?> deleteShopById(@PathVariable("id") Long id) {
        logger.info("Fetching & Deleting Shop with id {}", id);
        Optional<Shop> shop = shopService.getById(id);
        if (shop.isPresent()) {
            shopService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Unable to delete. Brand with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Unable to delete. Brand with id " + id + " not found."));
        }
    }

    @PostMapping(value = "/shops")
    public ResponseEntity<?> addBrands(@RequestBody ShopDTO dto) {
        logger.info("Entering addShop with shop Details >>>>>>>>  : {}", dto);
        Shop shop = shopService.save(dto);
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomErrorType("Issue while saving Shop"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/shops")
    public ResponseEntity<?> updateStock(@RequestBody ShopDTO dto) {
        logger.info("Updating Shop with id {}", dto.getId());
        Optional<Shop> shopOpt = shopService.getById(dto.getId());
        if (shopOpt.isEmpty()) {
            logger.error("Unable to update. Shop with id {} not found.", dto.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Unable to update. Shop with id " + dto.getId() + " not found."));
        } else {
            Shop shop = shopOpt.get();
            shop.setShopName(dto.getName());
            shop.setImageId(dto.getImageId());
            shop.setShopBranch(dto.getBranch());
            shop.setShopType(dto.getType());
            shop.setUserId(dto.getUserId());
            shopService.update(shop);
            return ResponseEntity.ok(shop);
        }
    }

    //////////////////////// Shop End Here ////////////////////

    @PostMapping("/")
    @Operation(summary = "Add or increase stock", description = "Creates a new stock entry or increases quantity for an existing stock for a given shop and item.")
    public ResponseEntity<?> addStock(@RequestBody StockBean s) {

        Stock stock = stockService.getStockByShopAndItemId(s.getShopId(), s.getItemId());
        if (stock != null) {
            stock.setStockQuantity(stock.getStockQuantity() + s.getQuantity());
            Stock saved = stockService.saveStock(stock);
            if (saved == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CustomErrorType("Issue while saving stock"));
            }
            return ResponseEntity.ok(saved);

        } else {
            stock = new Stock();
            stock.setStockQuantity(s.getQuantity());
            stock.setStockCreatedByUser(s.getUserId());
            stock.setStockDateCreated(new Date());
            stock.setItemId(s.getItemId());
            stock.setShopId(s.getShopId());
            Stock saved = stockService.saveStock(stock);
            if (saved == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CustomErrorType("Issue while saving stock"));
            }
            return ResponseEntity.ok(saved);
        }
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update stock quantity", description = "Updates the quantity of an existing stock record by its id.")
    public ResponseEntity<?> updateStock(@RequestBody StockDTO stockDTO) {
        logger.info("Updating Stock  with id {}", stockDTO.getId());
        Optional<Stock> stockOpt = stockService.getStockById(stockDTO.getId());
        if (stockOpt.isEmpty()) {
            logger.error("Unable to update. Stock with id {} not found.", stockDTO.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Unable to update. Stock with id " + stockDTO.getId() + " not found."));
        }
        Stock stock = stockOpt.get();
        stock.setStockQuantity(stockDTO.getQuantity());
        Stock saved = stockService.saveStock(stock);
        if (saved == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomErrorType("Issue while saving stock"));
        }
        return ResponseEntity.ok(saved);
    }

    @PostMapping(value = "/update/on/order")
    @Operation(summary = "Update stock after order", description = "Updates stock levels based on an order detail payload.")
    public ResponseEntity<?> updateStockByOrder(@RequestBody OrderDetailDTO orderDetail) {
        logger.info("Updating Stock on orderDetails {}", orderDetail);
        stockService.updateStockByOrder(orderDetail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock details by id", description = "Fetches a single stock record with enriched item and shop information.")
    public ResponseEntity<?> getStockdetail(@PathVariable("id") Long id) {
        logger.info("Fetching Stock with id {}", id);
        Optional<Stock> stockOpt = stockService.getStockById(id);
        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Stock with id " + id + " not found"));
        }
        Stock stock = stockOpt.get();
        ItemDTO item = productClient.getItemById(stock.getItemId());
        ShopDTO shopDTO = shopService.getShopDtoById(stock.getShopId()).orElse(null);
        StockDTO dto = stockMapper.toDto(stock, item, shopDTO);

        logger.info("Stock retrieved: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable("id") Long id) {
        logger.info("Fetching & Deleting Stock with id {}", id);
        Optional<Stock> stockOpt = stockService.getStockById(id);
        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Unable to delete. Stock with id " + id + " not found."));
        }
        stockService.deleteStockById(id);
        return ResponseEntity.noContent().build();
    }


    /////////////////////////////// Get ItemStock for vendor Shop


    @GetMapping("/items/by/vendor/{id}")
    public ResponseEntity<?> getStocksByVendorId(@PathVariable("id") Long id) {

        // Get All the Shop/s by vendor id. Assume only one shop per vendor
        List<Shop> shops = shopService.getByVendorId(id);
        // Get stock by shop id
        // get items by Shop id
        //prepare list and send Item dto
        List<ItemDTO> itemDtos = new ArrayList<>();
        if (shops.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Items Not Found! Please Contact Admin"));
        }

        List<Stock> stocks = stockService.getStockByShopId(shops.get(0).getShopId());
        for (Stock stock : stocks) {
            ItemDTO item = productClient.getItemById(stock.getItemId());
            if (item == null) {
                continue;
            }
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setName(item.getName());
            itemDTO.setCode(item.getCode());
            itemDTO.setMrp(item.getMrp());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setBrandId(item.getBrandId());
            itemDTO.setDispPrice(item.getDispPrice());

            if (item.getImageId() != null) {
                imageService.setPicIfPresent(itemDTO, item.getImageId());
            }

            itemDtos.add(itemDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemDtos);
    }

    @GetMapping("/items/by/shop/{id}")
    public ResponseEntity<List<StockDTO>> getStocksItemByShopId(@PathVariable("id") Long id) {
        List<StockDTO> dtos = stockQueryService.getStocksItemByShopId(id);
        if (dtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dtos);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/quantity/by/shops/{shopId}/items/{itemId}")
    public ResponseEntity<Long> getStockQuantityByShopIdAndItemId(@PathVariable("shopId") Long shopId,
                                                                  @PathVariable("itemId") Long itemId) {
        if (shopId == null || itemId == null) {
            return ResponseEntity.badRequest().build();
        }
        Stock stock = stockService.getStockByShopAndItemId(shopId, itemId);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok((long) stock.getStockQuantity());
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockDTO>> getAllStock() {
        List<StockDTO> dtos = stockQueryService.getAllStockDtos();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

}
