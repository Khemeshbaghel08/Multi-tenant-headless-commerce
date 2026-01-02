package com.example.demo.service;

import com.example.demo.dto.Pricing;
import com.example.demo.entity.Product;
import com.example.demo.entity.TenantContext;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Component
@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PricingService pricingService;


    public Product create(String tenantId, Product product) {
        product.setTenantId(tenantId);
        return productRepository.save(product);
    }


    public List<Product> getAll(String tenantId) {
        return productRepository.findByTenantId(tenantId);
    }


    public Product getPricedProduct(Product product) {

        Pricing context = new Pricing(
                product.getPrice(),
                product.getInventory(),
                TenantContext.get(),
                product.getSku()
        );

        BigDecimal finalPrice = pricingService.calculate(context);
        product.setPrice(finalPrice);

        return product;
    }
}

