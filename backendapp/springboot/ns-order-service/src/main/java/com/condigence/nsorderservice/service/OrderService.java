package com.condigence.nsorderservice.service;


import com.condigence.nsorderservice.dto.*;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.entity.OrderDetail;
import com.condigence.nsorderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public boolean saveOrderDetail(OrderDetailDTO orderdetailDTO) {

		logger.info("Order detail in order Service " + getJsonString(orderdetailDTO));

		Order order = new Order();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		Long grandTotal = 0L;
		if (null != orderdetailDTO) {

			order.setOrderFromCustId(orderdetailDTO.getCustomer().getCustomerId());

			ShopDTO shopData = restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/shops/"+orderdetailDTO.getShop().getId(), ShopDTO.class); // Working

			order.setOrderToVendorId(shopData.getUserId());

			order.setOrderToShopId(orderdetailDTO.getShop().getId());

			for (ItemDTO itemDto : orderdetailDTO.getItems()) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrderItemId(itemDto.getId());
				orderDetail.setOrderItemQuantity(itemDto.getQuantity());
				// orderDetail.setOrderItemPrice(itemDto.getPrice());
				// orderDetail.setOrderSubTotal(orderDetail.getOrderItemQuantity() *
				// orderDetail.getOrderItemPrice());
				// TODO:
				// orderDetail.setOrderTotalamount(orderDetail.getOrderSubTotal() +
				// orderDetail.getOrderServiceCharge() + orderDetail.getOrderGST() -
				// orderDetail.getOrderDiscount());
				// grandTotal = grandTotal + orderDetail.getOrderTotalamount();
				orderDetailList.add(orderDetail);
			}
			// TODO:
			order.setOrderGrandTotal(grandTotal);
			order.setOrderDetail(orderDetailList);

		}
		order.setOrderDate(java.time.LocalDate.now());
		order.setOrderTime(java.time.LocalTime.now());
		order.setOrderStatus("PENDING");
		order.setOrderDeliveryStatus("CONFIRMED");
		order.setEta("NOTSET");
		order = orderRepository.save(order);

		logger.info("Order  saved in db is ************" + order.toString());
		if (order.getOrderId() != null) {
			// TODO : Update Stock as well
			 restTemplate.postForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/update/on/order",orderdetailDTO, Boolean.class); // Working
			return true;
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
			// TODO :

			ItemDTO itemDTO = new ItemDTO();

			//TODO:
			// Product Service call


			ItemDTO itemData = restTemplate.getForObject("http://NS-PRODUCT-SERVICE/neerseva/api/v1/products/items/"+orderDetail.getOrderItemId(), ItemDTO.class); // Working
			itemDTO.setId(itemData.getId());
			ItemDTO itemDto = new ItemDTO();
			// itemDto.setItemCode(item.getCode());
			itemDto.setId(itemData.getId());
			itemDto.setName(itemData.getName());
			//itemDto.setPic(getPicById(item.getImageId()).getPic());
			itemDto.setQuantity(orderDetail.getOrderItemQuantity());
			// itemDto.setItemPrice(item.getItemPrice() *
			// orderDetail.getOrderItemQuantity());
			itemDtos.add(itemDto);

			// itemList.add(item);
			//System.out.println("itemDtos  :$$$$ " + itemDtos);
		}
		//System.out.println("order by customer id :$$$$ " + order);
		
		//User customer = userRepo.findById(order.getOrderFromCustId()).get();
		UserDTO customer = restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/"+order.getOrderFromCustId(), UserDTO.class); // Working
		//logger.info("Customer is " + customer);
		UserDTO vendor = null;
		if(order.getOrderToVendorId() != null){
			logger.info("vendor Id " + order.getOrderToVendorId());
			//User vendor = userRepo.findById(order.getOrderToVendorId()).get();
			vendor = restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/"+order.getOrderToVendorId(), UserDTO.class); // Working
			//logger.info("vendor is " + vendor);
		}


		OrderDTO orderDtoObj = createOrderDTOObject(order, itemDtos, customer, vendor);
		
		//logger.info("OrderDto Object is " + getJsonString(orderDtoObj));
		return orderDtoObj;

	}

	private OrderDTO createOrderDTOObject(Order order, List<ItemDTO> itemList, UserDTO customer, UserDTO vendor) {
		// TODO Auto-generated method stub

		OrderDTO orderDto = new OrderDTO();
		// orderDto.(order.getEta());
		orderDto.setOrderDate(order.getOrderDate());
		orderDto.setOrderDeliveryStatus(order.getOrderDeliveryStatus());
		orderDto.setOrderId(order.getOrderId());
		orderDto.setOrderStatus(order.getOrderStatus());
		orderDto.setOrderTime(order.getOrderTime());

		VendorDTO vendorDto = new VendorDTO();
		if(vendor!= null){
			vendorDto.setName(vendor.getName());
			vendorDto.setVendorId(vendor.getId());
			vendorDto.setContact(vendor.getContact());
			vendorDto.setEmail(vendor.getEmail());
			if (vendor.getImageId() != null) {
				vendorDto.setImageId(vendor.getImageId());
				ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+vendor.getImageId(), ImageDTO.class); // Working
				//System.out.println(imageDTO);
				vendorDto.setPic(imageDTO.getPic());
			}
		}


		ShopDTO shopData = restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/shops/"+order.getOrderToShopId(), ShopDTO.class); // Working

//		vendorDto.setAddressList(getUserAddress(vendor.getAddress()));

		CustomerDTO custDto = new CustomerDTO();
		custDto.setContact(customer.getContact());
		custDto.setCustomerId(customer.getId());
		custDto.setEmail(customer.getEmail());
		custDto.setName(customer.getName());
		if (customer.getImageId() != null) {
			custDto.setImageId(customer.getImageId());
			//custDto.setPic(getPicById(customer.getImageId()).getPic());
			ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/"+customer.getImageId(), ImageDTO.class); // Working
			//System.out.println(imageDTO);
			custDto.setPic(imageDTO.getPic());
		}
//		custDto.setAddressList(getUserAddress(customer.getAddress()));

		// List<ItemDTO> itemDtoList = getItemList(itemList);

		OrderDetailDTO orderDetaildto = new OrderDetailDTO();
		orderDetaildto.setCustomer(custDto);
		orderDetaildto.setVendor(vendorDto);
		orderDetaildto.setItems(itemList);
		orderDetaildto.setShop(shopData);
		orderDto.setOrderDetail(orderDetaildto);
		return orderDto;
	}

//	private List<ItemDTO> getItemList(List<Item> itemList) {
//		List<ItemDTO> itemDtoList = new ArrayList<>();
//		for (Item item : itemList) {
//
//			System.out.println(item.getId());
//			ItemDTO itemDto = new ItemDTO();
//			// itemDto.setItemCode(item.getCode());
//			itemDto.setId(item.getId());
//		    itemDto.setName(item.getName());
//			//itemDto.setItemQuantity(item.);
//			// itemDto.setItemPrice(item.getItemPrice() *
//			// orderDetail.getOrderItemQuantity());
//			itemDtoList.add(itemDto);
//		}
//		return itemDtoList;
//	}

//	private List<AddressDTO> getUserAddress(List<Address> addressList) {
//		// TODO Auto-generated method stub
//		List<AddressDTO> addressDtoList = new ArrayList<>();
//		for (Address address : addressList) {
//			AddressDTO addressDto = new AddressDTO();
//			String locality = address.getLocality();
//			addressDto.setAddress(locality);
//			long id = address.getAddressId();
//			addressDto.setAddressId(id);
//			addressDtoList.add(addressDto);
//		}
//		return addressDtoList;
//	}

	public String getJsonString(Object obj) {
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = Obj.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	public OrderDTO save(OrderDTO dto, Integer id) {
		Optional<Order> order = orderRepository.findById(Long.valueOf(id));
		order.get().setOrderDeliveryStatus(dto.getOrderDeliveryStatus());

		Order newOrder = orderRepository.save(order.get());

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
