package com.jctech.emart.orderservice.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jctech.emart.orderservice.exception.ObjectNotFoundException;
import com.jctech.emart.orderservice.exception.UnsatisfiedRequestException;
import com.jctech.emart.orderservice.service.OrderService;
import com.jctech.emart.orderservice.view.OrderRequest;
import com.jctech.emart.orderservice.view.OrderResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name="inventory", fallbackMethod="fallbackMethod")
	@TimeLimiter(name="inventory")
	public CompletableFuture<OrderResponse> placeOrder(@RequestBody OrderRequest req) {
		return CompletableFuture.supplyAsync(() -> orderService.placeOrderVerified(req));
	} 
	
	public OrderResponse createOrder(@RequestBody OrderRequest req) {
		return orderService.placeOrderVerified(req);
	}
	
	@GetMapping
	public List<OrderResponse> getAllOrders(){
		return orderService.getOrders();
	}
	
	@ExceptionHandler
	private ResponseEntity<String> ExceptionsHandler(Exception ex) {
		System.out.println(ex.getMessage());
		if (ex instanceof ObjectNotFoundException) { 
			 return	new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); 
		} else if (ex instanceof UnsatisfiedRequestException) { 
			 return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE); 
		} 	
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);  
	}
	
	public CompletableFuture<ResponseEntity<String>> failbackMethod(OrderRequest req, RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(() -> 
		    new ResponseEntity<String>("Oops! Something went wrong, please put order after some time"
				                            ,HttpStatus.INTERNAL_SERVER_ERROR)); 
	}

}
