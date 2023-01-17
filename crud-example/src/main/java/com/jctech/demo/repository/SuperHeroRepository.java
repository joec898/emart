package com.jctech.demo.repository;

import com.jctech.demo.model.SuperHero;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SuperHeroRepository extends MongoRepository<SuperHero, String> {

}
