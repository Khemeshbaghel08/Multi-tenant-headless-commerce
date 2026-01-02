package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

public record Pricing(BigDecimal price, int inventory, String tenantId, String sku){
}
