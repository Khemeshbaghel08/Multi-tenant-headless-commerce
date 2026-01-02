package com.example.demo.repository;

import com.example.demo.entity.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface tenantRepository extends MongoRepository<Tenant, String> {
}
