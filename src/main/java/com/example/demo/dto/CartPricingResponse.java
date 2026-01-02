package com.example.demo.dto;

import java.math.BigDecimal;

public record CartPricingResponse(String cartId, BigDecimal amount) {
}
