package com.jctech.emart.orderservice.view;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	
	private Long id;
	private String orderNumber; 
	private List<OrderLineItemView> orderLineItems;
}
