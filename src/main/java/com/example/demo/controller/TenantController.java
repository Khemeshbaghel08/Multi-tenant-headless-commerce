package com.example.demo.controller;

import com.example.demo.entity.Tenant;
import com.example.demo.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public ResponseEntity<?> createTenant(@RequestBody Tenant tenant) {
        Tenant savedTenant = tenantService.SaveItem(tenant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTenant);
    }
    @GetMapping
    public List<Tenant> getAll() {
        return tenantService.findAll();
    }
}
