package com.jctech.emart.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jctech.emart.productservice.exception.ObjectNotFoundException;
import com.jctech.emart.productservice.model.Product;
import com.jctech.emart.productservice.repository.ProductRepository;
import com.jctech.emart.productservice.view.ProductRequest;
import com.jctech.emart.productservice.view.ProductResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

	private final ProductRepository productRepo;

	public ProductResponse createProduct(ProductRequest req) {
		Product p = Product.builder()
				.name(req.getName())
				.description(req.getDescription())
				.price(req.getPrice())
				.build();
		log.info("Product {} is saved", p.getId());
		return mapToProductResponse(productRepo.save(p));
	}

	public List<ProductResponse> getProducts() {
		List<Product> products = productRepo.findAll();
		if (products.isEmpty()) {
			throw new ObjectNotFoundException("No Products found");
		}
		return products.stream().map(this::mapToProductResponse).toList();
	}

	private ProductResponse mapToProductResponse(Product prod) {
		return ProductResponse.builder()
				.id(prod.getId())
				.name(prod.getName())
				.description(prod.getDescription())
				.price(prod.getPrice()).build();
	}

}
