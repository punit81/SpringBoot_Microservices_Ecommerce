package com.orderservice.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.orderservice.dto.OrderLineItemResponseDto;
import com.orderservice.dto.OrderRequestDto;
import com.orderservice.dto.OrderResponseDto;
import com.orderservice.model.Order;
import com.orderservice.model.OrderLineItem;
import com.orderservice.repo.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	//private final Executor orderExecutor;
    public void createOrder(OrderRequestDto requestDto) {
    	Order order = Order.builder()
    							.id(UUID.randomUUID().toString())
    							.orderNumber(requestDto.getOrderNumber())
    							.orderLineItems(requestDto.getOrderLineItems().stream()
										.map(dto -> OrderLineItem.builder()
												.skuCode(dto.getSkuCode())
												.id(UUID.randomUUID().toString())
												.productName(dto.getProductName())
												.build())
										.toList())
								.build();
    	List<String>skuCodes=order.getOrderLineItems().stream()
				.map(OrderLineItem::getSkuCode)
				.toList();
    	Map<String, Boolean> inventoryResponse = webClientBuilder.build().get()
    							.uri("lb://INVENTORYSERVICE/api/inventories/availableitems",
										uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
    							.retrieve().bodyToMono(new ParameterizedTypeReference<Map<String,Boolean>>(){}).block();
    	boolean allProductsAvaliable=inventoryResponse.values().stream().allMatch(Boolean::booleanValue);
    	if(!allProductsAvaliable) {
			throw new RuntimeException("Product is not available in inventory, please try again later");
		}
    	order.getOrderLineItems().forEach(item -> {
    		item.setOrder(order);
    	});
    	orderRepository.save(order);
    }
	
    
    public OrderResponseDto getOrderById(String id) {
    	return orderRepository.findById(id)
				.map(this::mapToOrderResponseDto)
				.orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<OrderResponseDto> getAllOrders(){
    	List<Order> orders=(List<Order>) orderRepository.findAll();
    	
    	return orders.stream().map(this::mapToOrderResponseDto).toList();
    }
    
    public OrderResponseDto mapToOrderResponseDto(Order order) {
		return OrderResponseDto.builder()
				.id(order.getId())
				.orderNumber(order.getOrderNumber())
				.orderLineItems(order.getOrderLineItems().stream().map(this::mapToOrderLineItemResponseDto).toList())
				.build();
    }
    public OrderLineItemResponseDto mapToOrderLineItemResponseDto(OrderLineItem orderLineItem) {
    	return OrderLineItemResponseDto.builder()
				.id(orderLineItem.getId())
				.skuCode(orderLineItem.getSkuCode())
				.productName(orderLineItem.getProductName())
				.build();
    }
    
    public void updateOrder(String id, OrderRequestDto requestDto) {
    	Order order = orderRepository.findById(id).orElse(null);
		if (order == null) {
			throw new RuntimeException("Order not found");
		}
		else {
			order.setOrderNumber(requestDto.getOrderNumber());
			List<OrderLineItem> orderLineItems = requestDto.getOrderLineItems().stream()
					.map(dto -> OrderLineItem.builder()
							.skuCode(dto.getSkuCode())
							.id(UUID.randomUUID().toString())
							.productName(dto.getProductName())
							.order(order)
							.build())
					.toList();
			order.setOrderLineItems(orderLineItems);
			orderRepository.save(order);
		}
    }
    
    //delete order
    public void deleteOrder(String id) {
    	orderRepository.deleteById(id);
    }
}
