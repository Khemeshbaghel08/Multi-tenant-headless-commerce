package com.example.demo.repository;

import com.example.demo.dto.Pricing;

import java.math.BigDecimal;

public interface PricingRule {

    int priority();
    boolean applies(Pricing pricing);
    BigDecimal apply(BigDecimal currentPrice, Pricing pricing);
}
