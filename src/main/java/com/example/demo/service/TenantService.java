package com.example.demo.service;


import com.example.demo.entity.Tenant;
import com.example.demo.repository.tenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private tenantRepository tenantRepository;


    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    public Tenant SaveItem(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

}
