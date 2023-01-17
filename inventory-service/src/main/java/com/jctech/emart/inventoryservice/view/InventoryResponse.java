package com.jctech.emart.inventoryservice.view;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {  
    private Long id;
    private String skuCode;
    private Integer quantity;
    private boolean isInStock;
}
