package com.example.demo.rules;

import com.example.demo.dto.Pricing;
import com.example.demo.repository.PricingRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class InventoryPricing implements PricingRule {

    @Override
    public boolean applies(Pricing pricing) {
        return pricing.inventory() < 5;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, Pricing pricing) {
        BigDecimal markup = currentPrice.multiply(new BigDecimal("0.15"));
        return currentPrice.add(markup);
    }

    @Override
    public int priority() {
        return 3;
    }

}
