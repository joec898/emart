package com.jctech.emart.inventoryservice.model;

import javax.persistence.*; 

import lombok.*;

@Entity
@Table(name = "mart_inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
