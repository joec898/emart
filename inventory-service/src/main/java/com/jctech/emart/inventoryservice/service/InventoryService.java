package com.jctech.emart.inventoryservice.service;

import java.util.List;

 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jctech.emart.inventoryservice.model.Inventory;
import com.jctech.emart.inventoryservice.repository.InventoryRepository;
import com.jctech.emart.inventoryservice.view.InventoryRequest;
import com.jctech.emart.inventoryservice.view.InventoryResponse;
import com.jctech.emart.inventoryservice.exception.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepo;
	
	@SneakyThrows
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCodes) {
		log.debug("Checking inventory...");
		return this.inventoryRepo.findBySkuCodeIn(skuCodes)
				.stream()
				.map(inventory ->
				   InventoryResponse.builder()
				   .skuCode(inventory.getSkuCode())
				   .isInStock(inventory.getQuantity()>0)
				   .build() 
				 ).toList();
	}
	
	public InventoryResponse addInventory(InventoryRequest req) {
		Inventory inventory = Inventory.builder()
				.skuCode(req.getSkuCode())
				.quantity(req.getQuantity())
				.build();
		
		return toInventoryResponse(this.inventoryRepo.save(inventory));
	}
	
	public List<InventoryResponse> getInventories() {
		List<Inventory> inventories = this.inventoryRepo.findAll();
		if(inventories.isEmpty()) {
			throw new ObjectNotFoundException("Not orders found.");
		}
		return inventories.stream().map(this::toInventoryResponse).toList();
	}
	
	private InventoryResponse toInventoryResponse(Inventory inventory) {
		return InventoryResponse.builder()
				.id(inventory.getId())
				.skuCode(inventory.getSkuCode())
				.quantity(inventory.getQuantity())
				.build();
	}
}
