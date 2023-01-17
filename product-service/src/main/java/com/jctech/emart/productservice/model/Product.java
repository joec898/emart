package com.jctech.emart.productservice.model;

import java.math.BigDecimal;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private String id;
	private String name;
	private String description;
	private BigDecimal price;
	
//	public Product(String id, String name, String description, BigDecimal price) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.description = description;
//		this.price = price;
//	}
//	
//	 
}
