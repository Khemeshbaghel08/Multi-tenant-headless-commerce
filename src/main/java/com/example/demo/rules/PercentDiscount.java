package com.example.demo.rules;

import com.example.demo.dto.Pricing;
import com.example.demo.repository.PricingRule;
import com.example.demo.service.PricingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentDiscount implements PricingRule {

    private final BigDecimal percent;

    public PercentDiscount(
            @Value("${pricing.percent-discount.value}") BigDecimal percent
    ) {
        this.percent = percent;
    }

    @Override
    public boolean applies(Pricing pricing) {
        return true;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, Pricing pricing) {
        BigDecimal discount = currentPrice
                .multiply(percent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return currentPrice.subtract(discount);
    }

    @Override
    public int priority() {
        return 1;
    }
}
