package com.jctech.emart.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jctech.emart.inventoryservice.service.InventoryService;
import com.jctech.emart.inventoryservice.view.InventoryRequest;
import com.jctech.emart.inventoryservice.view.InventoryResponse;
import com.jctech.emart.inventoryservice.exception.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	// @GetMapping{"/skuCode"} is for;
	// http://localhost:8082/api/inventory/iphone-13,iphone13-red
	// @GetMapping is for:
	// http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
		 
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
		return inventoryService.isInStock(skuCode);
	}
	
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> getInventory(){
		return inventoryService.getInventories();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public InventoryResponse addInventory(@RequestBody InventoryRequest inventoryReq) {
		return inventoryService.addInventory(inventoryReq);
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
