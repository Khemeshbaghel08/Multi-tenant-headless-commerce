package com.example.demo.integrationtest;

import com.example.demo.baseintegrationtest.BaseIntegrationTest;
import com.example.demo.dto.CartPricingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CartIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ProductIntegrationTest productIntegrationTest;

    @Test
    void cart_applies_dynamic_pricing() {

        String token = productIntegrationTest.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        rest.postForEntity("/cart/items", new HttpEntity<>(Map.of("sku", "SKU-1", "qty", 2), headers), Void.class);
        ResponseEntity<CartPricingResponse> cart = rest.exchange("/cart", HttpMethod.GET, new HttpEntity<>(headers), CartPricingResponse.class);

        assertEquals(new BigDecimal("920.00"), cart.getBody().amount());
    }



}
