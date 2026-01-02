package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.enums.CartStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserIdAndTenantIdAndActiveTrue(String userId, String tenantId);
}
