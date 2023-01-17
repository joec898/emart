package com.jctech.emart.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jctech.emart.orderservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
