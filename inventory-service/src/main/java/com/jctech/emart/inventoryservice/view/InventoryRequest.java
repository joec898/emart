package com.jctech.emart.inventoryservice.view;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequest {  
    private String skuCode;
    private Integer quantity; 
}
