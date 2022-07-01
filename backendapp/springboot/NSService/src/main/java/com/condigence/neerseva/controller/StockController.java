package com.condigence.neerseva.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.condigence.neerseva.bean.StockBean;
import com.condigence.neerseva.dto.ItemDTO;
import com.condigence.neerseva.dto.ShopDTO;
import com.condigence.neerseva.dto.StockDTO;
import com.condigence.neerseva.dto.UserDTO;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.entity.Item;
import com.condigence.neerseva.entity.Shop;
import com.condigence.neerseva.entity.Stock;
import com.condigence.neerseva.services.ImageService;
import com.condigence.neerseva.services.ItemService;
import com.condigence.neerseva.services.ShopService;
import com.condigence.neerseva.services.StockService;
import com.condigence.neerseva.services.UserService;
import com.condigence.neerseva.util.AppProperties;
import com.condigence.neerseva.util.CustomErrorType;

@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
@RestController
public class StockController {

	public static final Logger logger = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private StockService stockService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserService userService;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

	//////////// Shops //////////////////

	@GetMapping("/v1/shops/{id}")
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
				dto.setPic(getPicById(shop.get().getImageId()).getPic());
			}
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			logger.error("Unable to Find. Brand with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to Find. Shop with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/v1/shops/vendor/{id}")
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
					dto.setPic(getPicById(shop.getImageId()).getPic());
				}
				
				dtos.add(dto);
			}
			return ResponseEntity.status(HttpStatus.OK).body(dtos);

		} else {
			return new ResponseEntity(new CustomErrorType("Shop not found."), HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/v1/stocks/by/shop/{id}")
	public ResponseEntity<?> getStocksByShopId(@PathVariable("id") Long id) {
		List<Stock> stocks = stockService.getStockByShopId(id);
		List<StockDTO> dtos = new ArrayList<>();

		for (Stock stock : stocks) {

			StockDTO dto = new StockDTO();
			UserDTO userDTO = new UserDTO();
			ItemDTO itemDTO = new ItemDTO();
			ShopDTO shopDTO = new ShopDTO();

			Optional<Item> item = itemService.getItemById(stock.getItemId());

			dto.setId(stock.getStockId());
			dto.setQuantity(stock.getStockQuantity());
			// prepare Items to display
			itemDTO.setId(item.get().getId());
			itemDTO.setName(item.get().getName());

			if (item.get().getImageId() != null) {
				itemDTO.setPic(getPicById(item.get().getImageId()).getPic());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/shops/{id}")
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
	@PostMapping(value = "/v1/shops")
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
	@PutMapping(value = "/v1/shops")
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
	@PostMapping("/v1/stock")
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
	@PutMapping(value = "/v1/stock/{id}")
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/v1/stocks/{id}")
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
			Optional<Item> item = itemService.getItemById(stock.get().getItemId());
			dto.setQuantity(stock.get().getStockQuantity());
			// prepare Items to display
			itemDTO.setId(item.get().getId());
			itemDTO.setName(item.get().getName());
			
			if (item.get().getImageId() != null) {
				itemDTO.setPic(getPicById(item.get().getImageId()).getPic());
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
	@DeleteMapping(value = "/v1/stock/{id}")
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

	private Image getPicById(long imageId) {
		String imagePath = app.getLocation();
		return imageService.getImage(imageId, imagePath);
	}
	
	
	/////////////////////////////// Get ItemStock for vendor Shop
	
	
	@GetMapping("/v1/stock/items/by/vendor/{id}")
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
			Optional<Item> item = itemService.getItemById(stock.getItemId());
			// prepare Items to display
			itemDTO.setId(item.get().getId());
			itemDTO.setName(item.get().getName());
			itemDTO.setCode(item.get().getCode());
			itemDTO.setMrp(item.get().getMrp());
			itemDTO.setPrice(item.get().getPrice());
			itemDTO.setBrandId(item.get().getBrandId());
			itemDTO.setDispPrice(item.get().getDispPrice());
			
			if (item.get().getImageId() != null) {
				itemDTO.setPic(getPicById(item.get().getImageId()).getPic());
			}
		
			itemDtos.add(itemDTO);
		}
		System.out.println(itemDtos);
		return ResponseEntity.status(HttpStatus.OK).body(itemDtos);
	}

}
