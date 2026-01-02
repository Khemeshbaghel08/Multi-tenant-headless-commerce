package com.example.demo.servicetest;

import com.example.demo.dto.Pricing;
import com.example.demo.rules.FlatDiscount;
import com.example.demo.rules.InventoryPricing;
import com.example.demo.rules.PercentDiscount;
import com.example.demo.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTests {

    @Mock
    PricingService pricingService;

    @BeforeEach
    void setup() {
        pricingService = new PricingService(List.of(new PercentDiscount(new BigDecimal("10")), new FlatDiscount(new BigDecimal("100")), new InventoryPricing()));
    }

    @Test
    void should_apply_all_pricing_rules() {
        Pricing pricing = new Pricing(new BigDecimal("1000"), 2, "tenant1", "SKU-1");

        BigDecimal finalPrice = pricingService.calculate(pricing);
        assertEquals(new BigDecimal("920.00"), finalPrice.setScale(2, RoundingMode.HALF_UP));

    }
}
