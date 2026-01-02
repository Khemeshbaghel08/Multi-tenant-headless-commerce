package com.example.demo.rules;

import com.example.demo.dto.Pricing;
import com.example.demo.repository.PricingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FlatDiscount implements PricingRule {

    private final BigDecimal amount;

    public FlatDiscount(
            @Value("${pricing.flat-discount.amount}") BigDecimal amount
    ) {
        this.amount = amount;
    }


    @Override
    public boolean applies(Pricing pricing) {
        return true;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, Pricing pricing) {
        return currentPrice.subtract(amount).max(BigDecimal.ZERO);
    }

    @Override
    public int priority() {
        return 2;
    }
}
