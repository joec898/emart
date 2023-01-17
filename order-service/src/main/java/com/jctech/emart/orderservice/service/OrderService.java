package com.jctech.emart.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jctech.emart.orderservice.exception.ObjectNotFoundException;
import com.jctech.emart.orderservice.exception.UnsatisfiedRequestException;
import com.jctech.emart.orderservice.model.Order;
import com.jctech.emart.orderservice.model.OrderLineItem;
import com.jctech.emart.orderservice.repository.OrderRepository;
import com.jctech.emart.orderservice.view.InventoryResponse;
import com.jctech.emart.orderservice.view.OrderLineItemView;
import com.jctech.emart.orderservice.view.OrderRequest;
import com.jctech.emart.orderservice.view.OrderResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepo;

	private final WebClient.Builder webClientBuilder;
	
	public List<OrderResponse> getOrders(){
		List<Order> orders = orderRepo.findAll();
		if(orders.isEmpty()) {
			throw new ObjectNotFoundException("Not orders found.");
		}
		return orders.stream().map(this::mapToOrderResponse).toList();
	}
	
	public OrderResponse placeOrder(OrderRequest req) {
		Order order = this.requestToOrder(req); 
		orderRepo.save(order); 
		return mapToOrderResponse(order);
	}
	
	public OrderResponse placeOrderVerified(OrderRequest req) {
		Order order = this.requestToOrder(req);
		
		List<String> skuCodes = 
				order.getOrderLineItems().stream().map(OrderLineItem::getSkuCode).toList();
	    
		String inventoryServiceUrl = "http://localhost:8083/api/inventory";
	    inventoryServiceUrl = "http://inventory-service/api/inventory";
	    
		InventoryResponse[] inventoryResponseArr = 
				webClientBuilder.build().get()
					.uri(inventoryServiceUrl,
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
					.retrieve()
					.bodyToMono(InventoryResponse[].class)
					.block();
		
		boolean allProductsInStock =
				Arrays.stream(inventoryResponseArr).allMatch(InventoryResponse::isInStock);
		
		if(allProductsInStock) {
			orderRepo.save(order);
			return mapToOrderResponse(order);
		} else {
			throw new UnsatisfiedRequestException("Product is not in stock, try again later please");
		}
		
	}
	
	private Order requestToOrder(OrderRequest req) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItem> items = req.getOrderLineItems()
				.stream().map(this::mapToOrderLineItem).toList();
		
		order.setOrderLineItems(items);
		return order;
	}
	
	private OrderLineItem mapToOrderLineItem(OrderLineItemView view) {
		OrderLineItem item = new OrderLineItem();
		item.setSkuCode(view.getSkuCode());
		item.setPrice(view.getPrice());
		item.setQuantity(view.getQuantity());
		return item;
	}
	
	private OrderLineItemView mapToOrderLineItemView(OrderLineItem item) {
		OrderLineItemView view = new OrderLineItemView();
		view.setId(item.getId());
		view.setSkuCode(item.getSkuCode());
		view.setPrice(item.getPrice());
		view.setQuantity(item.getQuantity());
		return view;
	}
	
	private OrderResponse mapToOrderResponse(Order order) {
		OrderResponse resp = new OrderResponse();
		resp.setId(order.getId());
		resp.setOrderNumber(order.getOrderNumber());
		resp.setOrderLineItems(
		  order.getOrderLineItems()
		      .stream()
		      .map(this::mapToOrderLineItemView).toList()
		);
		return resp;
	}
}
	
