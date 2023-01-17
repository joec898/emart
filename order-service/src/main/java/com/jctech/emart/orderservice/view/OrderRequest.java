package com.jctech.emart.orderservice.view;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

	private String orderNumber;

	private List<OrderLineItemView> orderLineItems;
}
