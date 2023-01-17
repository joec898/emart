package com.jctech.emart.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jctech.emart.productservice.exception.ObjectNotFoundException;
import com.jctech.emart.productservice.service.ProductService;
import com.jctech.emart.productservice.view.ProductRequest;
import com.jctech.emart.productservice.view.ProductResponse;

import lombok.RequiredArgsConstructor;
  
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse addProduct(@RequestBody ProductRequest req) {
		return productService.createProduct(req);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponse> getAllProducts() {
		return productService.getProducts();
	}
	
	@ExceptionHandler
	private ResponseEntity<String> ExceptionsHandler(Exception ex) {
		System.out.println(ex.getMessage());
		if (ex instanceof ObjectNotFoundException) { 
			 return	new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); 
		}	
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);  
	}

}
