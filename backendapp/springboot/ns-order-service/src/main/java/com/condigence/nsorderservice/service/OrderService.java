package com.condigence.nsorderservice.service;


import com.condigence.nsorderservice.dto.*;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.entity.OrderDetail;
import com.condigence.nsorderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.condigence.nsorderservice.exception.BadRequestException;
import com.condigence.nsorderservice.exception.ResourceNotFoundException;

@Service("OrderService")
public class OrderService {

	public static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private OrderRepository orderRepository;


	public List<Order> getAllOrders() {

		List<Order> orders = new ArrayList<>();
		orders = (List<Order>) orderRepository.findAll();
		return orders;

	}

	@Transactional
	public boolean saveOrderDetail(OrderDetailDTO orderdetailDTO) {



		Order order = new Order();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		Long grandTotal = 0L;
		if (null != orderdetailDTO) {

			order.setOrderFromCustId(orderdetailDTO.getCustomer().getCustomerId());

			// Validate shop is present and has a valid id
			ShopDTO shopDto = orderdetailDTO.getShop();
			long shopId = -1L;
			if (shopDto == null) {
				logger.error("Shop information is missing in OrderDetailDTO");
				throw new BadRequestException("Shop information is missing in request");
			} else {
				shopId = shopDto.getId();
				if (shopId <= 0) {
					logger.error("Invalid shop id in OrderDetailDTO: {}", shopId);
					throw new BadRequestException("Invalid shop id: " + shopId);
				}
			}

			ShopDTO shopData = null;
			try {
				shopData = restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/shops/" + shopId, ShopDTO.class); // Working
			} catch (HttpClientErrorException.NotFound nf) {
				logger.error("Shop not found for id {}", shopId);
				throw new ResourceNotFoundException("Shop not found for id " + shopId);
			} catch (RestClientException rce) {
				logger.error("Error while fetching shop data for id {}: {}", shopId, rce.getMessage());
				throw new BadRequestException("Error while fetching shop data: " + rce.getMessage());
			}

			if (shopData == null) {
				logger.error("Shop service returned null for id {}", shopId);
				throw new ResourceNotFoundException("Shop not found for id " + shopId);
			}

			order.setOrderToVendorId(shopData.getUserId());

			order.setOrderToShopId(orderdetailDTO.getShop().getId());

			for (ItemDTO itemDto : orderdetailDTO.getItems()) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrderItemId(itemDto.getId());
				orderDetail.setOrderItemQuantity(itemDto.getQuantity());
				// set parent relationship
				orderDetail.setOrder(order);
				// add to list
				orderDetailList.add(orderDetail);
			}
			// TODO: compute grandTotal from items if price available
			order.setOrderGrandTotal(grandTotal);
			order.setOrderDetail(orderDetailList);

			// ensure bidirectional link: set order on each detail (defensive)
			for (OrderDetail od : order.getOrderDetail()) {
				if (od.getOrder() == null) od.setOrder(order);
			}

		}
		order.setOrderDate(java.time.LocalDate.now());
		order.setOrderTime(java.time.LocalTime.now());
		order.setOrderStatus("PENDING");
		order.setOrderDeliveryStatus("CONFIRMED");
		order.setEta("NOTSET");
		// persist and flush so DB assigns identity values immediately
		order = orderRepository.saveAndFlush(order);

		// Log saved order minimal info to avoid lazy-loading issues during JSON serialization
		logger.info("Order saved in db: id={} date={} status={}", order.getOrderId(), order.getOrderDate(), order.getOrderStatus());
		 if (order.getOrderId() != null) {
		     // Attempt to update stock. If stock update fails because stock not found,
		     // remove the created order to avoid inconsistent state and return false.
		     try {
		        var response = restTemplate.postForEntity("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/update/on/order", orderdetailDTO, Boolean.class);
		        if (response.getStatusCode().is2xxSuccessful()) {
		            return true;
		        } else {
		            logger.error("Stock service did not confirm update for order {}. status={} body={}", order.getOrderId(), response.getStatusCode(), response.getBody());
		            // Construct a more specific not-found message with shop and item details
                    StringBuilder itemDetails = new StringBuilder();
                    if (orderdetailDTO != null && orderdetailDTO.getItems() != null) {
                        orderdetailDTO.getItems().forEach(i -> itemDetails.append("[id:").append(i.getId()).append(",qty:").append(i.getQuantity()).append("]"));
                    }
                    Long shopId = null;
                    if (orderdetailDTO != null && orderdetailDTO.getShop() != null) shopId = orderdetailDTO.getShop().getId();
                    String msg = String.format("Stock not available for order %d; shopId=%s items=%s", order.getOrderId(), shopId, itemDetails.toString());
                    throw new ResourceNotFoundException(msg);
                }
            } catch (HttpClientErrorException.NotFound nf) {
                 logger.warn("Stock update failed - Not Found for order {}: {}.", order.getOrderId(), nf.getResponseBodyAsString());
                 throw new ResourceNotFoundException("Stock not found: " + nf.getResponseBodyAsString());
              } catch (HttpClientErrorException.BadRequest br) {
                 logger.warn("Stock update returned BadRequest for order {}: {}.", order.getOrderId(), br.getResponseBodyAsString());
                 throw new BadRequestException("Stock service returned bad request: " + br.getResponseBodyAsString());
             } catch (HttpClientErrorException hce) {
                 // other 4xx errors
                 logger.warn("Stock update returned client error for order {}: {}.", order.getOrderId(), hce.getResponseBodyAsString());
                 throw new BadRequestException("Stock service client error: " + hce.getResponseBodyAsString());
             } catch (RestClientException rce) {
                  logger.error("Stock update failed for order {}: {}", order.getOrderId(), rce.getMessage());
                  // mark order for manual attention and persist
                  order.setOrderStatus("STOCK_UPDATE_FAILED");
                  orderRepository.save(order);
                  return false;
              }

          }
          return false;
	 }

//	public boolean updateOrder(OrderDTO orderDTO) {
//		// TODO Auto-generated method stub
//		Order order = orderRepo.findById(orderDTO.getOrderId()).get();
//		if (null != order) {
//			order.setEta(orderDTO.getEta());
//			order.setOrderDeliveryStatus(orderDTO.getOrderDeliveryStatus());
//			order.setOrderStatus(orderDTO.getOrderStatus());
//			orderRepo.save(order);
//			List<OrderDetail> orderDetailList = order.getOrderDetail();
//			for (OrderDetail orderDetail : orderDetailList) {
//				Item item = itemRepo.findById(orderDetail.getOrderItemId()).get();
//				logger.info("Item is *******11111111111****" + item.toString());
//				int remainingItemInStock = item.getItemQuantity() - orderDetail.getOrderItemQuantity();
//				logger.info("item.getItemQuantity()***********" + item.getItemQuantity());
//				logger.info(" orderDetail.getOrderItemQuantity()**********" + orderDetail.getOrderItemQuantity());
//				logger.info("remaining Quantity****" + remainingItemInStock);
//				item.setItemQuantity(remainingItemInStock);
//				Item item1 = itemRepo.save(item);
//				logger.info("Item is ***********" + item1.toString());
//			}
//			return true;
//		}
//		return false;
//	}

	public List<OrderDTO> getOrderByVendorId(long vendorId) {
		List<OrderDTO> OrderDtoList = new ArrayList<OrderDTO>();
		List<Order> orderList = orderRepository.getByorderToVendorId(vendorId);
		logger.info("Order based on Vendor Detail is " + orderList);

		for (Order order : orderList) {

			OrderDTO orderDtoObj = getOrderDto(order);
			OrderDtoList.add(orderDtoObj);
		}
		return OrderDtoList;
	}

	public List<OrderDTO> getOrderBycustomerId(long custId) {
		List<OrderDTO> OrderDtoList = new ArrayList<OrderDTO>();
		//List<Order> orderList = orderRepository.findFirst5ByOrderFromCustIdOrderByOrderDateDesc(custId);
		List<Order> orderList = orderRepository.getByOrderFromCustId(custId);
		logger.info("Order based on customer Detail is " + orderList);

		for (Order order : orderList) {

			OrderDTO orderDtoObj = getOrderDto(order);
			OrderDtoList.add(orderDtoObj);
		}
		return OrderDtoList;
	}

	private OrderDTO getOrderDto(Order order) {
		// List<Item> itemList = new ArrayList<>();

		List<ItemDTO> itemDtos = new ArrayList<>();

		for (OrderDetail orderDetail : order.getOrderDetail()) {
			// fetch item data from product service

			Long itemId = orderDetail.getOrderItemId();
			if (itemId == null) {
				logger.warn("OrderDetail {} for Order {} has null itemId; skipping item.", orderDetail.getOrderDetailId(), order.getOrderId());
				continue;
			}

			ItemDTO itemData = null;
			try {
				itemData = restTemplate.getForObject("http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/" + itemId, ItemDTO.class); // Working
			} catch (Exception e) {
				logger.warn("Failed to fetch item {} from product service: {}", itemId, e.getMessage());
			}
			if (itemData == null) {
				logger.warn("Product service returned null for itemId {}. Skipping item.", itemId);
				continue;
			}

			ItemDTO itemDto = new ItemDTO();
			itemDto.setId(itemData.getId());
			itemDto.setName(itemData.getName());
			// populate item pic via image service (if available)
			if (itemData.getImageId() != null) {
				try {
					ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + itemData.getImageId() + "/data", ImageDTO.class);
					if (imageDTO != null) {
						itemDto.setPic(imageDTO.getPic());
					}
				} catch (Exception e) {
					logger.debug("Unable to fetch image for item {} (imageId={}): {}", itemData.getId(), itemData.getImageId(), e.getMessage());
				}
			}
			itemDto.setQuantity(orderDetail.getOrderItemQuantity());
			itemDtos.add(itemDto);

			//System.out.println("itemDtos  :$$$$ " + itemDtos);
		}

		// fetch customer only if present
		UserDTO customer = null;
		if (order.getOrderFromCustId() != null) {
			try {
				customer = restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/" + order.getOrderFromCustId(), UserDTO.class); // Working
			} catch (Exception e) {
				logger.warn("Failed to fetch customer {}: {}", order.getOrderFromCustId(), e.getMessage());
			}
		} else {
			logger.warn("Order {} has null orderFromCustId", order.getOrderId());
		}
		//logger.info("Customer is " + customer);
		UserDTO vendor = null;
		if(order.getOrderToVendorId() != null){
			logger.info("vendor Id " + order.getOrderToVendorId());
			try {
				vendor = restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/"+order.getOrderToVendorId(), UserDTO.class); // Working
			} catch (Exception e) {
				logger.warn("Failed to fetch vendor {}: {}", order.getOrderToVendorId(), e.getMessage());
			}
		}


		OrderDTO orderDtoObj = createOrderDTOObject(order, itemDtos, customer, vendor);
		
		//logger.info("OrderDto Object is " + getJsonString(orderDtoObj));
		return orderDtoObj;

	}

	private OrderDTO createOrderDTOObject(Order order, List<ItemDTO> itemList, UserDTO customer, UserDTO vendor) {
		OrderDTO orderDto = new OrderDTO();
		if (order == null) {
			logger.warn("createOrderDTOObject called with null order");
			return orderDto;
		}

		// Ensure orderId is propagated
		orderDto.setOrderId(order.getOrderId());

		// basic timestamps and statuses
		orderDto.setOrderDate(order.getOrderDate());
		orderDto.setOrderTime(order.getOrderTime());
		orderDto.setOrderStatus(order.getOrderStatus());
		orderDto.setOrderDeliveryStatus(order.getOrderDeliveryStatus());
		// ETA
		orderDto.setEta(order.getEta());

		// grand total (safe conversion)
		orderDto.setOrderGrandTotal(convertGrandTotal(order.getOrderGrandTotal()));

		// build nested DTOs
		VendorDTO vendorDto = buildVendorDto(vendor);
		CustomerDTO custDto = buildCustomerDto(customer);
		ShopDTO shopData = fetchShopData(order.getOrderToShopId(), order.getOrderId());

		OrderDetailDTO orderDetaildto = buildOrderDetailDto(custDto, vendorDto, itemList, shopData);
		orderDto.setOrderDetail(orderDetaildto);

		// Log final DTO JSON for debugging
		logger.info("OrderDTO built: {}", getJsonString(orderDto));
		return orderDto;
	}

	// Helper: convert Long grand total to Integer safely
	private Integer convertGrandTotal(Long grandTotal) {
		if (grandTotal == null) return null;
		if (grandTotal > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return grandTotal.intValue();
	}

	// Helper: build VendorDTO from UserDTO
	private VendorDTO buildVendorDto(UserDTO vendor) {
		VendorDTO vendorDto = new VendorDTO();
		if (vendor == null) return vendorDto;
		vendorDto.setName(vendor.getName());
		vendorDto.setVendorId(vendor.getId());
		vendorDto.setContact(vendor.getContact());
		vendorDto.setEmail(vendor.getEmail());
		if (vendor.getImageId() != null) {
			vendorDto.setImageId(vendor.getImageId());
			try {
				ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + vendor.getImageId()+"/data", ImageDTO.class);
				if (imageDTO != null) vendorDto.setPic(imageDTO.getPic());
			} catch (Exception e) {
				logger.debug("Vendor image fetch failed for imageId {}: {}", vendor.getImageId(), e.getMessage());
			}
		}
		return vendorDto;
	}

	// Helper: build CustomerDTO from UserDTO
	private CustomerDTO buildCustomerDto(UserDTO customer) {
		CustomerDTO custDto = new CustomerDTO();
		if (customer == null) return custDto;
		custDto.setCustomerId(customer.getId());
		custDto.setName(customer.getName());
		custDto.setEmail(customer.getEmail());
		custDto.setContact(customer.getContact());
		if (customer.getImageId() != null) {
			custDto.setImageId(customer.getImageId());
			try {
				ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + customer.getImageId()+"/data", ImageDTO.class);
				if (imageDTO != null) custDto.setPic(imageDTO.getPic());
			} catch (Exception e) {
				logger.debug("Customer image fetch failed for imageId {}: {}", customer.getImageId(), e.getMessage());
			}
		}
		return custDto;
	}

	// Helper: fetch shop data with safe error handling
	private ShopDTO fetchShopData(Long shopId, Long orderId) {
		if (shopId == null) return null;
		try {
			return restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/shops/" + shopId, ShopDTO.class);
		} catch (HttpClientErrorException.NotFound nf) {
			logger.debug("Shop not found for id {} while building DTO for order {}", shopId, orderId);
		} catch (RestClientException rce) {
			logger.debug("Error fetching shop {} for order {}: {}", shopId, orderId, rce.getMessage());
		}
		return null;
	}

	// Helper: build OrderDetailDTO
	private OrderDetailDTO buildOrderDetailDto(CustomerDTO customer, VendorDTO vendor, List<ItemDTO> items, ShopDTO shop) {
		OrderDetailDTO detail = new OrderDetailDTO();
		detail.setCustomer(customer);
		detail.setVendor(vendor);
		detail.setItems(items);
		detail.setShop(shop);
		return detail;
	}

	public String getJsonString(Object obj) {
		ObjectMapper Obj = new ObjectMapper();
		// register Java Time module to support LocalDate/LocalTime
		Obj.registerModule(new JavaTimeModule());
		Obj.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String jsonStr = "";
		try {
			jsonStr = Obj.writeValueAsString(obj);
		} catch (IOException e) {
			logger.error("Failed to serialize object to JSON: {}", e.getMessage());
		}
		return jsonStr;
	}

	public OrderDTO save(OrderDTO dto, Integer id) {
		Order order = orderRepository.findById(Long.valueOf(id))
				.orElseThrow(() -> new IllegalArgumentException("Order not found for id: " + id));
		order.setOrderDeliveryStatus(dto.getOrderDeliveryStatus());

		Order newOrder = orderRepository.save(order);

		OrderDTO updatedOrder = new OrderDTO();
		updatedOrder.setOrderId(newOrder.getOrderId());
		updatedOrder.setOrderDeliveryStatus(newOrder.getOrderDeliveryStatus());

		return updatedOrder;

	}

//	private Image getPicById(long imageId) {
//		String imagePath = app.getLocation();
//		Image image = null;
//		ImageUtil imgutil = new ImageUtil();
//		try {
//			image = imageRepository.findById(imageId).get();
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		if (null != image) {
//			image.setPic(imgutil.getImageWithFileName(image.getName(), imagePath));
//		}
//
//		return image;
//
//	}

}
