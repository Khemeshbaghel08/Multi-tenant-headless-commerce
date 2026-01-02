package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByTenantIdAndSku(String tenantId, String sku);

        List<Product> findByTenantId(String tenantId);
    @Query("""
      { 'tenantId': ?0, 'sku': ?1, 'inventory': { $gte: ?2 } }
    """)
    @Update("""
      { $inc: { 'inventory': -?2 } }
    """)
    long reserveInventory(String productId, String tenantId, int quantity);
}
