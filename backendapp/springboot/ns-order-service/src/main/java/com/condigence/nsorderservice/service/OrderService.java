package com.condigence.nsorderservice.service;


import com.condigence.nsorderservice.connector.ExternalServiceClient;
import com.condigence.nsorderservice.dto.*;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.entity.OrderDetail;
import com.condigence.nsorderservice.exception.BadRequestException;
import com.condigence.nsorderservice.exception.BusinessException;
import com.condigence.nsorderservice.exception.ErrorCode;
import com.condigence.nsorderservice.exception.ResourceNotFoundException;
import com.condigence.nsorderservice.repository.OrderDetailRepository;
import com.condigence.nsorderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("OrderService")
public class OrderService {

    public static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final ExternalServiceClient externalClient;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderService(ExternalServiceClient externalClient, OrderRepository orderRepository,
                       OrderDetailRepository orderDetailRepository) {
        this.externalClient = externalClient;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
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
            Long shopId;
            if (shopDto == null) {
                logger.error("Shop information is missing in OrderDetailDTO");
                throw new BusinessException(ErrorCode.BUS_INVALID_REQUEST, "Shop information is missing in request");
            } else {
                shopId = shopDto.getId();
                if (shopId == null || shopId <= 0) {
                    logger.error("Invalid shop id in OrderDetailDTO: {}", shopId);
                    throw new BusinessException(ErrorCode.BUS_INVALID_REQUEST, "Invalid shop id: " + shopId);
                }
            }

            ShopDTO shopData = externalClient.getShop(shopId);

            if (shopData == null) {
                logger.error("Shop service returned null for id {}", shopId);
                throw new BusinessException(ErrorCode.BUS_SHOP_NOT_FOUND, "Shop not found for id " + shopId);
            }

            order.setOrderToVendorId(shopData.getUserId());

            order.setOrderToShopId(orderdetailDTO.getShop().getId());

            for (StockDTO stockItemDto : orderdetailDTO.getStockItems()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderItemId(stockItemDto.getItemId());
                orderDetail.setOrderItemQuantity(stockItemDto.getQuantity());
                // set parent relationship
                orderDetail.setOrder(order.getOrderId());
                // add to list
                orderDetailList.add(orderDetail);
            }
            // attach order details to order so they are cascaded on save
            order.setOrderDetail(orderDetailList);
            order.setOrderGrandTotal(grandTotal);
        }
        order.setOrderDate(java.time.LocalDate.now());
        order.setOrderTime(java.time.LocalTime.now());
        order.setOrderStatus("PENDING");
        order.setOrderDeliveryStatus("CONFIRMED");
        order.setEta("NOTSET");
        // persist and flush so DB assigns identity values immediately, including for details
        order = orderRepository.saveAndFlush(order);

        // Log saved order minimal info to avoid lazy-loading issues during JSON serialization
        logger.info("Order saved in db: id={} date={} status={}", order.getOrderId(), order.getOrderDate(), order.getOrderStatus());
        if (order.getOrderId() != null) {
            // Attempt to update stock via external client
            try {
                Boolean updated = externalClient.updateStockOnOrder(orderdetailDTO);
                if (Boolean.TRUE.equals(updated)) {
                    return true;
                } else {
                    // Unexpected non-true response; mark order and throw
                    logger.error("Stock service did not confirm update for order {}.", order.getOrderId());
                    order.setOrderStatus("STOCK_UPDATE_FAILED");
                    orderRepository.save(order);
                    throw new BusinessException(ErrorCode.BUS_INSUFFICIENT_STOCK,
                            "Stock update failed for order " + order.getOrderId());
                }
            } catch (ResourceNotFoundException rnfe) {
                // Delete created order as stock resource missing
                logger.warn("Stock update failed - Not Found for order {}: {}", order.getOrderId(), rnfe.getMessage());
                try {
                    orderRepository.deleteById(order.getOrderId());
                } catch (Exception ex) {
                    logger.error("Failed to delete order {} after stock not found: {}", order.getOrderId(), ex.getMessage());
                }
                // Wrap into business exception with specific code for stock not found
                throw new BusinessException(ErrorCode.BUS_STOCK_NOT_FOUND, rnfe.getMessage());
            } catch (BadRequestException bre) {
                logger.warn("Stock update returned BadRequest for order {}: {}", order.getOrderId(), bre.getMessage());
                try {
                    orderRepository.deleteById(order.getOrderId());
                } catch (Exception ex) {
                    logger.error("Failed to delete order {} after bad stock request: {}", order.getOrderId(), ex.getMessage());
                }
                // Treat as business insufficient stock or invalid stock-related request
                throw new BusinessException(ErrorCode.BUS_INSUFFICIENT_STOCK, bre.getMessage());
            } catch (Exception e) {
                logger.error("Stock update failed for order {}: {}", order.getOrderId(), e.getMessage());
                order.setOrderStatus("STOCK_UPDATE_FAILED");
                orderRepository.save(order);
                return false;
            }

        }
        return false;
    }



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
                itemData = externalClient.getItem(itemId);
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
                    ImageDTO imageDTO = externalClient.getImage(itemData.getImageId());
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
                customer = externalClient.getUser(order.getOrderFromCustId());
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
                vendor = externalClient.getUser(order.getOrderToVendorId());
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
                ImageDTO imageDTO = externalClient.getImage(vendor.getImageId());
                if (imageDTO != null) vendorDto.setPic(imageDTO.getPic());
            } catch (Exception e) {
                logger.debug("Vendor image fetch failed for imageId {}: {}", vendor.getImageId(), e.getMessage());
            }
        }

        // Populate address list for the customer using ExternalServiceClient
        try {
            List<AddressDTO> addresses = externalClient.getAddressesByUserId(vendor.getId());
            vendorDto.setAddressList(addresses);
        } catch (Exception e) {
            // Fail gracefully: log and leave address list null/empty
            logger.debug("Failed to fetch addresses for Vendor {}: {}", vendorDto.getVendorId(), e.getMessage());
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
                ImageDTO imageDTO = externalClient.getImage(customer.getImageId());
                if (imageDTO != null) custDto.setPic(imageDTO.getPic());
            } catch (Exception e) {
                logger.debug("Customer image fetch failed for imageId {}: {}", customer.getImageId(), e.getMessage());
            }
        }
        // Populate address list for the customer using ExternalServiceClient
        try {
            List<AddressDTO> addresses = externalClient.getAddressesByUserId(customer.getId());
            custDto.setAddressList(addresses);
        } catch (Exception e) {
            // Fail gracefully: log and leave address list null/empty
            logger.debug("Failed to fetch addresses for customer {}: {}", customer.getId(), e.getMessage());
        }
        return custDto;
    }

    // Helper: fetch shop data with safe error handling
    private ShopDTO fetchShopData(Long shopId, Long orderId) {
        if (shopId == null) return null;
        try {
            return externalClient.getShop(shopId);
        } catch (Exception e) {
            logger.debug("Error fetching shop {} for order {}: {}", shopId, orderId, e.getMessage());
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

//    private Image getPicById(long imageId) {
//        String imagePath = app.getLocation();
//        Image image = null;
//        ImageUtil imgutil = new ImageUtil();
//        try {
//            image = imageRepository.findById(imageId).get();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//        if (null != image) {
//            image.setPic(imgutil.getImageWithFileName(image.getName(), imagePath));
//        }
//
//        return image;
//
//    }

}
