package com.condigence.stockservice.service;

import com.condigence.stockservice.dto.OrderDetailDTO;
import com.condigence.stockservice.dto.StockItemDTO;
import com.condigence.stockservice.entity.Stock;
import com.condigence.stockservice.exception.InsufficientStockException;
import com.condigence.stockservice.exception.InvalidOrderException;
import com.condigence.stockservice.exception.StockNotFoundException;
import com.condigence.stockservice.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("stockServices")
public class StockService {
	public static final Logger logger = LoggerFactory.getLogger(StockService.class);

	@Autowired
	private StockRepository stockRepo;

	public Stock saveStock(Stock stock) {
		return stockRepo.save(stock);
	}

	public Optional<Stock> getStockById(long id) {
		return stockRepo.findById(id);
	}

	public void deleteStockById(long stockId) {
		stockRepo.deleteById(stockId);
	}

	// Note: method accepts parameters in (shopId, itemId) order to match its name.
	public Stock getStockByShopAndItemId(long shopId, long itemId) {
		// Repository expects (itemId, shopId)
		Optional<Stock> stock = stockRepo.getItemStockForShop(itemId, shopId);
		return stock.orElse(null);
	}
	
	
	public List<Stock> getStockByShopId(long id) {
		return stockRepo.getStockForShop(id);

	}

	public List<Stock> getAllStocks() {
		return stockRepo.findAll();
	}

	public void updateStockByOrder(OrderDetailDTO orderDetail) {
		if (orderDetail == null || orderDetail.getShop() == null || orderDetail.getShop().getId() == 0L) {
			throw new InvalidOrderException("Invalid order detail or shop");
		}
		Long shopId = orderDetail.getShop().getId();
		if (orderDetail.getStockItems() == null || orderDetail.getStockItems().isEmpty()) {
			throw new InvalidOrderException("No items in order detail");
		}

		for (StockItemDTO stockItem : orderDetail.getStockItems()) {
			if (stockItem == null) {
				throw new InvalidOrderException("Invalid item in order detail");
			}

			Long itemId = stockItem.getItemId();
			long orderedQty = stockItem.getQuantity();
			if (itemId == null || orderedQty <= 0) {
				throw new InvalidOrderException("Invalid item or quantity in order detail");
			}

			Stock stock = getStockByShopAndItemId(shopId, itemId);
			if (stock == null) {
				throw new StockNotFoundException("Stock not found for item " + itemId + " in shop " + shopId);
			}

			long currentQty = stock.getStockQuantity();
			if (currentQty < orderedQty) {
				throw new InsufficientStockException("Insufficient stock for item " + itemId);
			}

			stock.setStockQuantity((int) (currentQty - orderedQty));
			saveStock(stock);
		}
	}

}
