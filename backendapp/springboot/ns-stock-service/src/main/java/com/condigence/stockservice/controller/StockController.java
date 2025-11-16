package com.condigence.stockservice.controller;


import com.condigence.stockservice.bean.StockBean;
import com.condigence.stockservice.dto.*;
import com.condigence.stockservice.entity.Shop;
import com.condigence.stockservice.entity.Stock;
import com.condigence.stockservice.service.ShopService;
import com.condigence.stockservice.service.StockService;
import com.condigence.stockservice.util.AppProperties;
import com.condigence.stockservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api/v1/stocks")
public class StockController {

	public static final Logger logger = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private StockService stockService;

	@Autowired
	private ShopService shopService;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private com.condigence.stockservice.client.ImageClient imageClient;




	//////////// Shops //////////////////

	@GetMapping("/shops/{id}")
	public ResponseEntity<?> getShopsById(@PathVariable("id") Long id) {

		Optional<Shop> shop = shopService.getById(id);
		if (shop.isPresent()) {
			ShopDTO dto = new ShopDTO();
			dto.setId((shop.get().getShopId()));
			dto.setName(shop.get().getShopName());
			dto.setType(shop.get().getShopType());
			dto.setUserId(shop.get().getUserId());
			dto.setBranch(shop.get().getShopBranch());
			if(shop.get().getImageId() != null) {
				dto.setImageId(shop.get().getImageId());
				byte[] pic = imageClient.fetchImagePic(shop.get().getImageId());
				if (pic != null) dto.setPic(pic);
			}
			return ResponseEntity.ok(dto);
		} else {
			logger.error("Unable to Find. Shop with id {} not found.", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to Find. Shop with id " + id + " not found."));
		}
	}

	@GetMapping("/shops")
	public ResponseEntity<?> getAllShops() {
		List<ShopDTO> dtos = new ArrayList<>();
		List<Shop> shops = shopService.getAll();
		if (!shops.isEmpty()) {
			for (Shop s:shops) {
				ShopDTO dto = new ShopDTO();
				dto.setId((s.getShopId()));
				dto.setName(s.getShopName());
				dto.setType(s.getShopType());
				dto.setUserId(s.getUserId());
				dto.setBranch(s.getShopBranch());
				if(s.getImageId() != null) {
					dto.setImageId(s.getImageId());
					byte[] pic = imageClient.fetchImagePic(s.getImageId());
					if (pic != null) dto.setPic(pic);
				}

				dtos.add(dto);
			}

			return ResponseEntity.ok(dtos);
		} else {
			logger.error("Unable to Find Shops");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to Find Shops."));
		}
	}


	@GetMapping("/shops/vendor/{id}")
	public ResponseEntity<?> getShopsByVendorId(@PathVariable("id") Long id) {

		List<Shop> shops = shopService.getByVendorId(id);
		if (shops.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Shop not found."));
		}
		List<ShopDTO> dtos = new ArrayList<>();
		for (Shop shop : shops) {
			ShopDTO dto = new ShopDTO();
			dto.setId((shop.getShopId()));
			dto.setName(shop.getShopName());
			dto.setType(shop.getShopType());
			dto.setUserId(shop.getUserId());
			dto.setBranch(shop.getShopBranch());
			if (shop.getImageId() != null) {
				dto.setImageId(shop.getImageId());
				byte[] pic = imageClient.fetchImagePic(shop.getImageId());
				if (pic != null) dto.setPic(pic);
			}

			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);

	}

	@GetMapping("/by/shop/{id}")
	public ResponseEntity<?> getStocksByShopId(@PathVariable("id") Long id) {
		List<Stock> stocks = stockService.getStockByShopId(id);
		List<StockDTO> dtos = new ArrayList<>();

		for (Stock stock : stocks) {

			StockDTO dto = new StockDTO();
			UserDTO userDTO = new UserDTO();
			ItemDTO itemDTO = new ItemDTO();
			ShopDTO shopDTO = new ShopDTO();

			ItemDTO item = getItemById(stock.getItemId());

			dto.setId(stock.getStockId());
			dto.setQuantity(stock.getStockQuantity());


			// prepare Items to display
			itemDTO.setId(item.getId());
			itemDTO.setName(item.getName());
			itemDTO.setDispPrice(item.getDispPrice());
			itemDTO.setCapacity(item.getCapacity());
			itemDTO.setDiscount(item.getDiscount());
			itemDTO.setMrp(item.getMrp());
			itemDTO.setPrice(item.getPrice());
			itemDTO.setCode(item.getCode());

			if (item.getImageId() != null) {
				byte[] pic = imageClient.fetchImagePic(item.getImageId());
				if (pic != null) itemDTO.setPic(pic);
			}

			shopDTO.setId(stock.getShopId());
			userDTO.setId(stock.getStockCreatedByUser());
			dto.setItem(itemDTO);
			dto.setShop(shopDTO);
			dto.setUser(userDTO);
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	private ItemDTO getItemById(long itemId) {

		ItemDTO itemData = restTemplate.getForObject("http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/"+itemId, ItemDTO.class); // Working

		return itemData;
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
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to delete. Brand with id " + id + " not found."));
		}
	}

	@PostMapping(value = "/shops")
	public ResponseEntity<?> addBrands(@RequestBody ShopDTO dto) {
		logger.info("Entering addShop with shop Details >>>>>>>>  : {}", dto);
		Shop shop = shopService.save(dto);
		if (shop == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorType("Issue while saving Shop"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping(value = "/shops")
	public ResponseEntity<?> updateStock(@RequestBody ShopDTO dto) {
		logger.info("Updating Shop with id {}", dto.getId());
		Optional<Shop> shopOpt = shopService.getById(dto.getId());
		if (shopOpt.isEmpty()) {
			logger.error("Unable to update. Shop with id {} not found.", dto.getId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to update. Shop with id " + dto.getId() + " not found."));
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
	public ResponseEntity<?> addStock(@RequestBody StockBean s) {

		Stock stock = stockService.getStockByShopAndItemId(s.getItemId(), s.getShopId());
		if (stock != null) {
			stock.setStockQuantity(stock.getStockQuantity() + s.getQuantity());
			Stock saved = stockService.saveStock(stock);
			if (saved == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorType("Issue while saving stock"));
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
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorType("Issue while saving stock"));
			}
			return ResponseEntity.ok(saved);
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateStock(@RequestBody StockDTO stockDTO) {
		logger.info("Updating Stock  with id {}", stockDTO.getId());
		Optional<Stock> stockOpt = stockService.getStockById(stockDTO.getId());
		if (stockOpt.isEmpty()) {
			logger.error("Unable to update. Stock with id {} not found.", stockDTO.getId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to update. Stock with id " + stockDTO.getId() + " not found."));
		}
		Stock stock = stockOpt.get();
		stock.setStockQuantity(stock.getStockQuantity() + stockDTO.getQuantity());
		Stock saved = stockService.saveStock(stock);
		if (saved == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorType("Issue while saving stock"));
		}
		return ResponseEntity.ok(saved);
	}

	@PostMapping(value = "/update/on/order")
	public ResponseEntity<?> updateStockByOrder(@RequestBody OrderDetailDTO orderDetail) {
		logger.info("Updating Stock on orderDetails {}", orderDetail);

		// Validate request payload
		if (orderDetail == null || orderDetail.getShop() == null || orderDetail.getShop().getId() == 0L) {
			logger.error("Invalid order detail or shop in request: {}", orderDetail);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorType("Invalid order detail or shop"));
		}
		Long shopId = orderDetail.getShop().getId();
		if (orderDetail.getItems() == null || orderDetail.getItems().isEmpty()) {
			logger.error("No items present in orderDetail: {}", orderDetail);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorType("No items in order detail"));
		}

		// Validate and apply updates
		for (ItemDTO item : orderDetail.getItems()) {
			if (item == null || item.getId() == null) {
				logger.error("Invalid item in orderDetail: {}", item);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorType("Invalid item in order detail"));
			}

			Stock stock = stockService.getStockByShopAndItemId(item.getId(), shopId);
			if (stock == null) {
				logger.error("Stock not found for item {} in shop {}", item.getId(), shopId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Stock not found for item " + item.getId() + " in shop " + shopId));
			}

			long currentQty = stock.getStockQuantity();
			long reqQty = (item.getQuantity() == null ? 0L : item.getQuantity());
			if (reqQty <= 0) {
				logger.warn("Ignoring non-positive quantity for item {}: {}", item.getId(), reqQty);
				continue; // nothing to update for this item
			}
			if (currentQty < reqQty) {
				logger.error("Insufficient stock for item {} in shop {}: available={}, required={}", item.getId(), shopId, currentQty, reqQty);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorType("Insufficient stock for item " + item.getId()));
			}

			stock.setStockQuantity((int)(currentQty - reqQty));
			stockService.saveStock(stock);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getStockdetail(@PathVariable("id") Long id) {
		logger.info("Fetching Stock with id {}", id);
		Optional<Stock> stockOpt = stockService.getStockById(id);
		if (stockOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Stock with id " + id + " not found"));
		}
		Stock stock = stockOpt.get();
		StockDTO dto = new StockDTO();

		dto.setId(stock.getStockId());

		UserDTO userDTO = new UserDTO();
		ItemDTO itemDTO = new ItemDTO();
		ShopDTO shopDTO = new ShopDTO();
		ItemDTO item = getItemById(stock.getItemId());
		dto.setQuantity(stock.getStockQuantity());
		// prepare Items to display
		itemDTO.setId(item.getId());
		itemDTO.setName(item.getName());

		if (item.getImageId() != null) {
			byte[] pic = imageClient.fetchImagePic(item.getImageId());
			if (pic != null) itemDTO.setPic(pic);
		}

		Optional<Shop> shop = shopService.getById(stock.getShopId());
		if (shop.isPresent()) {
			shopDTO.setId(shop.get().getShopId());
			shopDTO.setName(shop.get().getShopName());
		}

		userDTO.setId(stock.getStockCreatedByUser());
		dto.setItem(itemDTO);
		dto.setShop(shopDTO);
		dto.setUser(userDTO);

		logger.info("Stock retrieved: {}", dto);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteStock(@PathVariable("id") Long id) {
		logger.info("Fetching & Deleting Stock with id {}", id);
		Optional<Stock> stockOpt = stockService.getStockById(id);
		if (stockOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Unable to delete. Stock with id " + id + " not found."));
		}
		stockService.deleteStockById(id);
		return ResponseEntity.noContent().build();
	}

//	private Image getPicById(long imageId) {
//		String imagePath = app.getLocation();
//		return imageService.getImage(imageId, imagePath);
//	}
	
	
	/////////////////////////////// Get ItemStock for vendor Shop
	
	
	@GetMapping("/items/by/vendor/{id}")
	public ResponseEntity<?> getStocksByVendorId(@PathVariable("id") Long id) {

		// Get All the Shop/s by vendor id. Assume only one shop per vendor
		List<Shop> shops = shopService.getByVendorId(id);
		// Get stock by shop id
		// get items by Shop id
		//prepare list and send Item dto
		List<ItemDTO> itemDtos = new ArrayList<>();
		if(shops.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Items Not Found! Please Contact Admin"));
		}

		List<Stock> stocks = stockService.getStockByShopId(shops.get(0).getShopId());
		for (Stock stock : stocks) {
			ItemDTO itemDTO = new ItemDTO();
			ItemDTO item = getItemById(stock.getItemId());
			// prepare Items to display
			itemDTO.setId(item.getId());
			itemDTO.setName(item.getName());
			itemDTO.setCode(item.getCode());
			itemDTO.setMrp(item.getMrp());
			itemDTO.setPrice(item.getPrice());
			itemDTO.setBrandId(item.getBrandId());
			itemDTO.setDispPrice(item.getDispPrice());
			
			if (item.getImageId() != null) {
				byte[] pic = imageClient.fetchImagePic(item.getImageId());
				if (pic != null) itemDTO.setPic(pic);
			}
		
			itemDtos.add(itemDTO);
		}
		System.out.println(itemDtos);
		return ResponseEntity.status(HttpStatus.OK).body(itemDtos);
	}

	@GetMapping("/items/by/shop/{id}")
	public ResponseEntity<?> getStocksItemByShopId(@PathVariable("id") Long id) {

		// Get All the stocks/s by shop id.
		List<Stock> stocks = stockService.getStockByShopId(id);
		// get items by Shop id
		//prepare list and send Item dto
		List<ItemDTO> itemDtos = new ArrayList<>();
		if(stocks.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorType("Items Not Found! Please Contact Admin"));
		}

		for (Stock stock : stocks) {
			if(stock.getStockQuantity()>0){
				ItemDTO itemDTO = new ItemDTO();
				ItemDTO item = getItemById(stock.getItemId());
				// prepare Items to display
				itemDTO.setId(item.getId());
				itemDTO.setName(item.getName());
				itemDTO.setCode(item.getCode());
				itemDTO.setMrp(item.getMrp());
				itemDTO.setPrice(item.getPrice());
				itemDTO.setBrandId(item.getBrandId());
				itemDTO.setDispPrice(item.getDispPrice());


				if (item.getImageId() != null) {
					byte[] pic = imageClient.fetchImagePic(item.getImageId());
					if (pic != null) itemDTO.setPic(pic);
				}

				itemDtos.add(itemDTO);
			}else{
				logger.warn("Stock is Empty, Please contact admin!");
			}

		}
		System.out.println(itemDtos);
		return ResponseEntity.status(HttpStatus.OK).body(itemDtos);
	}

	@GetMapping(value = "/quantity/by/shops/{shopId}/items/{itemId}")
	public ResponseEntity<Long> getStockQuantityByShopIdAndItemId(@PathVariable("shopId") Long shopId,
							@PathVariable("itemId") Long itemId) {
		if (shopId == null || itemId == null) {
			return ResponseEntity.badRequest().build();
		}
		Stock stock = stockService.getStockByShopAndItemId(itemId, shopId);
		if (stock == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok((long) stock.getStockQuantity());
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllStock() {
		List<Stock> stocks = stockService.getAllStocks();
		List<ItemDTO> dtos = new ArrayList<>();
		for (Stock stock : stocks) {
			ItemDTO itemDTO = new ItemDTO();
			ItemDTO item = getItemById(stock.getItemId());
			// prepare Items to display
			itemDTO.setId(item.getId());
			itemDTO.setName(item.getName());
			itemDTO.setDispPrice(item.getDispPrice());
			itemDTO.setCapacity(item.getCapacity());
			itemDTO.setDiscount(item.getDiscount());
			itemDTO.setMrp(item.getMrp());
			itemDTO.setPrice(item.getPrice());
			itemDTO.setCode(item.getCode());
            itemDTO.setStockId(stock.getStockId());

			if (item.getImageId() != null) {
				byte[] pic = imageClient.fetchImagePic(item.getImageId());
				if (pic != null) itemDTO.setPic(pic);
			}
			dtos.add(itemDTO);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

}
