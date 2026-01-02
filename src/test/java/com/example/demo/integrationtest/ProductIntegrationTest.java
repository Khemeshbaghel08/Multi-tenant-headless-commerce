package com.example.demo.integrationtest;

import com.example.demo.baseintegrationtest.BaseIntegrationTest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.ApiError;
import com.example.demo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProductIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    private String login() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-tenant-id", "tenant1");
        ResponseEntity<LoginResponse> res = rest.postForEntity("/auth/login?email=user1@nike.com&password=123456", new HttpEntity<>(headers), LoginResponse.class);

        return res.getBody().accessToken();
    }

    @Test
    void create_product_with_jwt() {

        String token = login();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Product product = new Product();
        product.setId(null);
        product.setTenantId(null);
        product.setSku("SKU-1");
        product.setName("Nike Shoe");
        product.setPrice(new BigDecimal("1000"));
        product.setInventory(10);

        ResponseEntity<Product> res = rest.postForEntity("/product", new HttpEntity<>(product, headers), Product.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("SKU-1", res.getBody().getSku());
    }

    @Test
    void checkout_fails_when_inventory_insufficient() {

        String token = login();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<ApiError> res = rest.exchange("/orders/checkout", HttpMethod.POST, new HttpEntity<>(headers), ApiError.class);

        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }
}
