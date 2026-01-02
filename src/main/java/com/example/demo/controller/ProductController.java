package com.example.demo.controller;

import com.example.demo.dto.JwtPrincipal;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping
    public Product create(@RequestBody Product product) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        JwtPrincipal principal =
                (JwtPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();


        return productService.create(principal.tenantId(), product);
    }

    @GetMapping
    public List<Product> getAll() {
        JwtPrincipal principal =
                (JwtPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return productService.getAll(principal.tenantId());
    }
}
