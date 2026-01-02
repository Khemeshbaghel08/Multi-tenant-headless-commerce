package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends MongoRepository<Order, String> {


}
