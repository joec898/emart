package com.jctech.emart.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jctech.emart.orderservice.event.OrderPlacedEvent;
import com.jctech.emart.orderservice.exception.ObjectNotFoundException;
import com.jctech.emart.orderservice.exception.UnsatisfiedRequestException;
import com.jctech.emart.orderservice.model.Order;
import com.jctech.emart.orderservice.model.OrderLineItem;
import com.jctech.emart.orderservice.repository.OrderRepository;
import com.jctech.emart.orderservice.view.InventoryResponse;
import com.jctech.emart.orderservice.view.OrderLineItemView;
import com.jctech.emart.orderservice.view.OrderRequest;
import com.jctech.emart.orderservice.view.OrderResponse;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepo;
	private final WebClient.Builder webClientBuilder;
	private final Tracer tracer;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
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
	    
	    // tracer is used here to start a new trace from orderService
	    // when inventoryService.isInStock() is called in a different thread of orderService.placeOrder
	    // This create a new span from order to inventoryService, avoiding broken link in tracing among threads
	    // note: webClientBuilder.build().get() is in new thread
	    Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
	    
	    //try(Tracer.SpanInScope isLookup = tracer.withSpanI(inventoryServiceLookup.start())){
	    try(SpanInScope isLookup = tracer.withSpanInScope(inventoryServiceLookup.start())){
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
				
				this.kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
				
				return mapToOrderResponse(order);
			} else {
				throw new UnsatisfiedRequestException("Product is not in stock, try again later please");
			}
			
	    } finally {
	    	inventoryServiceLookup.flush();
	    	//inventoryServiceLookup.end();
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
	
