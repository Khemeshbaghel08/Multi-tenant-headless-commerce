package com.example.demo.service;

import com.example.demo.dto.Pricing;
import com.example.demo.repository.PricingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class PricingService {

    private final List<PricingRule> rules;

    public PricingService(List<PricingRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparingInt(PricingRule::priority))
                .toList();
    }

    public BigDecimal calculate(Pricing pricing) {

        BigDecimal priced = pricing.price();

        for (PricingRule rule : rules) {
            if (rule.applies(pricing)) {
                priced = rule.apply(priced, pricing);
            }
        }
        return priced;
    }
}
