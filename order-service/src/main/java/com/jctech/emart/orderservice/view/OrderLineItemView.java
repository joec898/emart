package com.jctech.emart.orderservice.view;

import java.math.BigDecimal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemView {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
