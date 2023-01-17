package com.jctech.emart.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jctech.emart.productservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
