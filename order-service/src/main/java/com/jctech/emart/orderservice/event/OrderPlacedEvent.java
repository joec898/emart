package com.jctech.emart.orderservice.event;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
	private String orderNumber;
}
