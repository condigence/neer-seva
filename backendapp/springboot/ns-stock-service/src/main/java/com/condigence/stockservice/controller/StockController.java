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
import org.springframework.http.HttpHeaders;
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
				try {
					ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+shop.get().getImageId(), ImageDTO.class); // Working
					if (imageDTO != null) {
						dto.setPic(imageDTO.getPic());
					}
				} catch (Exception ex) {
					logger.warn("Unable to fetch image for imageId {}: {}", shop.get().getImageId(), ex.getMessage());
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			logger.error("Unable to Find. Brand with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to Find. Shop with id " + id + " not found."),
						HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/shops")
	public ResponseEntity<?> getAllShops() {
		List<ShopDTO> dtos = new ArrayList<>();
		List<Shop> shops = shopService.getAll();
		if (!shops.isEmpty()) {
			ShopDTO dto = null;
			for (Shop s:shops) {
				dto = new ShopDTO();
				dto.setId((s.getShopId()));
				dto.setName(s.getShopName());
				dto.setType(s.getShopType());
				dto.setUserId(s.getUserId());
				dto.setBranch(s.getShopBranch());
				if(s.getImageId() != null) {
					dto.setImageId(s.getImageId());
					try {
						ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+s.getImageId(), ImageDTO.class); // Working
						if (imageDTO != null) {
							dto.setPic(imageDTO.getPic());
						}
					} catch (Exception ex) {
						logger.warn("Unable to fetch image for imageId {}: {}", s.getImageId(), ex.getMessage());
					}
				}

				dtos.add(dto);
			}

			return ResponseEntity.status(HttpStatus.OK).body(dtos);
		} else {
			logger.error("Unable to Find Shops");
			return new ResponseEntity(new CustomErrorType("Unable to Find Shops."),
					HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping("/shops/vendor/{id}")
	public ResponseEntity<?> getShopsByVendorId(@PathVariable("id") Long id) {

		List<Shop> shops = shopService.getByVendorId(id);
		List<ShopDTO> dtos = new ArrayList<>();
		if (shops.size() != 0) {
			for (Shop shop : shops) {
				ShopDTO dto = new ShopDTO();
				dto.setId((shop.getShopId()));
				dto.setName(shop.getShopName());
				dto.setType(shop.getShopType());
				dto.setUserId(shop.getUserId());
				dto.setBranch(shop.getShopBranch());
				if(shop.getImageId() != null) {
					dto.setImageId(shop.getImageId());
					try {
						ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+shop.getImageId(), ImageDTO.class); // Working
						if (imageDTO != null) {
							dto.setPic(imageDTO.getPic());
						}
					} catch (Exception ex) {
						logger.warn("Unable to fetch image for imageId {}: {}", shop.getImageId(), ex.getMessage());
					}
				}

				dtos.add(dto);
			}
			return ResponseEntity.status(HttpStatus.OK).body(dtos);

		} else {
			return new ResponseEntity(new CustomErrorType("Shop not found."), HttpStatus.NOT_FOUND);
		}

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
				ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+item.getImageId(), ImageDTO.class); // Working
				//System.out.println(imageDTO);
				itemDTO.setPic(imageDTO.getPic());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/shops/{id}")
	public ResponseEntity<?> deleteShopById(@PathVariable("id") Long id) {
		logger.info("Fetching & Deleting Shop with id {}", id);
		Optional<Shop> shop = shopService.getById(id);
		if (shop.isPresent()) {
			shopService.deleteById(id);
		} else {
			logger.error("Unable to delete. Brand with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Brand with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Stock>(HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/shops")
	public ResponseEntity<?> addBrands(@RequestBody ShopDTO dto) {
		logger.info("Entering addShop with shop Details >>>>>>>>  : {}", dto);
		HttpHeaders headers = new HttpHeaders();
		Shop shop = shopService.save(dto);
		if (shop == null) {
			return new ResponseEntity(new CustomErrorType("Issue while saving Shop"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/shops")
	public ResponseEntity<?> updateStock(@RequestBody ShopDTO dto) {
		logger.info("Updating Shop with id {}", dto.getId());
		System.out.println(dto);
		Optional<Shop> shop = shopService.getById(dto.getId());
		// System.out.println(shop.get());
		if (!shop.isPresent()) {
			logger.error("Unable to update. Shop with id {} not found.", dto.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Shop with id " + dto.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		} else {
			shop.get().setShopName(dto.getName());
			shop.get().setImageId(dto.getImageId());
			shop.get().setShopBranch(dto.getBranch());
			shop.get().setShopType(dto.getType());
			shop.get().setUserId(dto.getUserId());
			shopService.update(shop.get());
			return new ResponseEntity<Shop>(shop.get(), HttpStatus.OK);
		}
	}

	//////////////////////// Shop End Here ////////////////////

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/")
	public ResponseEntity<?> addStock(@RequestBody StockBean s) {

		//logger.info("Inside addStock *****************" + s);
		HttpHeaders headers = new HttpHeaders();

		Stock stock = null;
		stock = stockService.getStockByShopAndItemId(s.getItemId(), s.getShopId());
		//logger.info("Inside addStock *****************" + stock);
		if (stock != null) {
			stock.setStockQuantity(stock.getStockQuantity() + s.getQuantity());
			stock = stockService.saveStock(stock);
			if (null == stock) {
				return new ResponseEntity(new CustomErrorType("Issue while saving stock"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			stock = new Stock();
			stock.setStockQuantity(s.getQuantity());
			stock.setStockCreatedByUser(s.getUserId());
			stock.setStockDateCreated(new Date());
			stock.setItemId(s.getItemId());
			stock.setShopId(s.getShopId());
			stockService.saveStock(stock);
		}

		return new ResponseEntity<Stock>(headers, HttpStatus.OK);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateStock(@RequestBody StockDTO stockDTO) {
		logger.info("Updating Stock  with id {}", stockDTO.getId());
		Optional<Stock> stock = stockService.getStockById(stockDTO.getId());
		if (!stock.isPresent()) {
			logger.error("Unable to update. Stock with id {} not found.", stock.get().getStockId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Stock with id " + stock.get().getItemId() + " not found."),
					HttpStatus.NOT_FOUND);
		}
		stock.get().setStockQuantity(stock.get().getStockQuantity() + stockDTO.getQuantity());

		//Stock stockSaved = stockService.saveStock(stock.get());
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Stock>(headers, HttpStatus.OK);
	}

	@PostMapping(value = "/update/on/order")
	public ResponseEntity<?> updateStockByOrder(@RequestBody OrderDetailDTO orderDetail) {
		logger.info("Updating Stock on orderDetails {}", orderDetail);
		for (ItemDTO item: orderDetail.getItems()) {
			Stock stock = stockService.getStockByShopAndItemId(item.getId(),orderDetail.getShop().getId());
			stock.setStockQuantity(stock.getStockQuantity() - item.getQuantity());
			stockService.saveStock(stock);
		}
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Stock>(headers, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/{id}")
	public ResponseEntity<?> getStockdetail(@PathVariable("id") Long id) {
		logger.info("Fetching Stock with id {}", id);
		Optional<Stock> stock = stockService.getStockById(id);

		System.out.println(stock);

		StockDTO dto = null;
		if (stock.isPresent()) {
			dto = new StockDTO();

			dto.setId(stock.get().getStockId());

			UserDTO userDTO = new UserDTO();
			ItemDTO itemDTO = new ItemDTO();
			ShopDTO shopDTO = new ShopDTO();
			ItemDTO item = getItemById(stock.get().getItemId());
			dto.setQuantity(stock.get().getStockQuantity());
			// prepare Items to display
			itemDTO.setId(item.getId());
			itemDTO.setName(item.getName());
			
			if (item.getImageId() != null) {
				//itemDTO.setPic(getPicById(item.getImageId()).getPic());
			}
			
			
			Optional<Shop> shop = shopService.getById(stock.get().getShopId());
			
			
			shopDTO.setId(shop.get().getShopId());
			shopDTO.setName(shop.get().getShopName());
			
			userDTO.setId(stock.get().getStockCreatedByUser());
			dto.setItem(itemDTO);
			dto.setShop(shopDTO);
			dto.setUser(userDTO);
			
		} else {
			logger.error("stock  with id {} not found.", dto);
			return new ResponseEntity(new CustomErrorType("Stock with id " + id + " not found"), HttpStatus.NOT_FOUND);

		}
		logger.info("Stock  with id {} is.", dto);
		return new ResponseEntity<StockDTO>(dto, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteStock(@PathVariable("id") Long id) {
		logger.info("Fetching & Deleting Stock with id {}", id);
		Optional<Stock> stock = stockService.getStockById(id);
		if (stock.isPresent()) {
			logger.error("Unable to delete. Stock with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Stock with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		} else {
			stockService.deleteStockById(id);
		}
		return new ResponseEntity<Stock>(HttpStatus.NO_CONTENT);
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
		List<ItemDTO> itemDtos = new ArrayList<ItemDTO>();
		if(shops.size() == 0) {
			//return ResponseEntity.status(HttpStatus.NO_CONTENT).body(itemDtos);
			return new ResponseEntity(new CustomErrorType("Items Not Found! Please Contact Admin"), HttpStatus.NOT_FOUND);
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
				ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+item.getImageId(), ImageDTO.class); // Working
				//System.out.println(imageDTO);
				itemDTO.setPic(imageDTO.getPic());
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
		List<ItemDTO> itemDtos = new ArrayList<ItemDTO>();
		if(stocks.size() == 0) {
			//return ResponseEntity.status(HttpStatus.NO_CONTENT).body(itemDtos);
			return new ResponseEntity(new CustomErrorType("Items Not Found! Please Contact Admin"), HttpStatus.NOT_FOUND);
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
					ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+item.getImageId(), ImageDTO.class); // Working
					itemDTO.setPic(imageDTO.getPic());
				}

				itemDtos.add(itemDTO);
			}else{
				logger.warn("Stock is Empty, Please contact admin!");
			}

		}
		System.out.println(itemDtos);
		return ResponseEntity.status(HttpStatus.OK).body(itemDtos);
	}

	@GetMapping(value = "/quantity/by/Shops/{id}/items/{id}")
	public ResponseEntity<Long> getStockQuantityByShopIdAndItemId() {
		return null;
	}

}
