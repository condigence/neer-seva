package com.condigence.neerseva.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.neerseva.dto.CustomerDTO;
import com.condigence.neerseva.dto.ItemDTO;
import com.condigence.neerseva.dto.OrderDTO;
import com.condigence.neerseva.dto.OrderDetailDTO;
import com.condigence.neerseva.dto.VendorDTO;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.entity.Item;
import com.condigence.neerseva.entity.Order;
import com.condigence.neerseva.entity.OrderDetail;
import com.condigence.neerseva.entity.User;
import com.condigence.neerseva.repository.ImageRepository;
import com.condigence.neerseva.repository.ItemRepository;
import com.condigence.neerseva.repository.OrderRepository;
import com.condigence.neerseva.repository.UserRepository;
import com.condigence.neerseva.util.AppProperties;
import com.condigence.neerseva.util.ImageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("OrderService")
public class OrderService {

	public static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private ItemRepository itemRepo;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

	public List<Order> getAllOrders() {

		List<Order> orders = new ArrayList<>();
		orders = (List<Order>) orderRepository.findAll();
		return orders;

	}

	public boolean saveOrderDetail(OrderDetailDTO orderdetailDTO) {

		logger.info("Order detail in order Service " + getJsonString(orderdetailDTO));

		Order order = new Order();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		Long grandTotal = 5000L;
		if (null != orderdetailDTO) {

			order.setOrderFromCustId(orderdetailDTO.getCustomer().getCustomerId());

			order.setOrderToVendorId(orderdetailDTO.getVendor().getVendorId());

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
		List<Order> orderList = orderRepository.findFirst5ByOrderFromCustIdOrderByOrderDateDesc(custId);
		logger.info("Order based on customer Detail is " + orderList);

		for (Order order : orderList) {

			OrderDTO orderDtoObj = getOrderDto(order);
			OrderDtoList.add(orderDtoObj);
		}
		return OrderDtoList;
	}

	private OrderDTO getOrderDto(Order order) {
		// List<Item> itemList = new ArrayList<>();

		List<ItemDTO> itemdtos = new ArrayList<>();

		for (OrderDetail orderDetail : order.getOrderDetail()) {
			// TODO :

			ItemDTO itemDTO = new ItemDTO();

			// System.out.println(itemRepo.findById(orderDetail.getOrderItemId()));
			Item item = itemRepo.findById(orderDetail.getOrderItemId()).get();
			itemDTO.setId(item.getId());
			ItemDTO itemDto = new ItemDTO();
			// itemDto.setItemCode(item.getCode());
			itemDto.setId(item.getId());
			itemDto.setName(item.getName());
			itemDto.setPic(getPicById(item.getImageId()).getPic());
			itemDto.setQuantity(orderDetail.getOrderItemQuantity());
			// itemDto.setItemPrice(item.getItemPrice() *
			// orderDetail.getOrderItemQuantity());
			itemdtos.add(itemDto);

			// itemList.add(item);
		}
		System.out.println("order by customer id :$$$$ " + order);
		
		User customer = userRepo.findById(order.getOrderFromCustId()).get();
		logger.info("Customer is " + customer);
		
		logger.info("vendor Id " + order.getOrderToVendorId());
		User vendor = userRepo.findById(order.getOrderToVendorId()).get();
		logger.info("vendor is " + vendor);

		OrderDTO orderDtoObj = createOrderDTOObject(order, itemdtos, customer, vendor);
		
		logger.info("OrderDto Object is " + getJsonString(orderDtoObj));
		return orderDtoObj;

	}

	private OrderDTO createOrderDTOObject(Order order, List<ItemDTO> itemList, User customer, User vendor) {
		// TODO Auto-generated method stub

		OrderDTO orderDto = new OrderDTO();
		// orderDto.(order.getEta());
		orderDto.setOrderDate(order.getOrderDate());
		orderDto.setOrderDeliveryStatus(order.getOrderDeliveryStatus());
		orderDto.setOrderId(order.getOrderId());
		orderDto.setOrderStatus(order.getOrderStatus());
		orderDto.setOrderTime(order.getOrderTime());

		VendorDTO vendorDto = new VendorDTO();
		vendorDto.setName(vendor.getName());
		vendorDto.setVendorId(vendor.getId());
		vendorDto.setContact(vendor.getContact());
		vendorDto.setEmail(vendor.getEmail());
		if (vendor.getImageId() != null) {
			vendorDto.setImageId(vendor.getImageId());
			 vendorDto.setPic(getPicById(vendor.getImageId()).getPic());
		}

//		vendorDto.setAddressList(getUserAddress(vendor.getAddress()));

		CustomerDTO custDto = new CustomerDTO();
		custDto.setContact(customer.getContact());
		custDto.setCustomerId(customer.getId());
		custDto.setEmail(customer.getEmail());
		custDto.setName(customer.getName());
		if (customer.getImageId() != null) {
			custDto.setImageId(customer.getImageId());
			custDto.setPic(getPicById(customer.getImageId()).getPic());
		}
//		custDto.setAddressList(getUserAddress(customer.getAddress()));

		// List<ItemDTO> itemDtoList = getItemList(itemList);

		OrderDetailDTO orderDetaildto = new OrderDetailDTO();
		orderDetaildto.setCustomer(custDto);
		orderDetaildto.setVendor(vendorDto);
		orderDetaildto.setItems(itemList);

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

	private Image getPicById(long imageId) {
		String imagePath = app.getLocation();
		Image image = null;
		ImageUtil imgutil = new ImageUtil();
		try {
			image = imageRepository.findById(imageId).get();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		if (null != image) {
			image.setPic(imgutil.getImageWithFileName(image.getName(), imagePath));
		}
	
		return image;

	}

}
