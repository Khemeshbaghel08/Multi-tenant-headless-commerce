package com.example.demo.service;

import com.example.demo.dto.CartPricingResponse;
import com.example.demo.dto.Pricing;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.enums.CartStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PricingService pricingService;

    public Cart addItem(String userId, String tenantId, String sku, int quantity) {
        Product product = productRepository
                .findByTenantIdAndSku(tenantId, sku)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        if (product.getInventory() < quantity) {
            throw new RuntimeException("Insufficient inventory");
        }

        Cart cart = cartRepository
                .findByUserIdAndTenantIdAndActiveTrue(userId, tenantId)
                .orElseGet(() -> createCart(userId, tenantId));

        cart.getItems().add(new CartItem(sku, quantity));
        return cartRepository.save(cart);
    }

    private Cart createCart(String userId, String tenantId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTenantId(tenantId);
        return cartRepository.save(cart);
    }

    public CartPricingResponse getCartPricing(String userId, String tenantId) {
        Cart cart = cartRepository
                .findByUserIdAndTenantIdAndActiveTrue(userId, tenantId)
                .orElseThrow(() -> new RuntimeException("No active cart"));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = productRepository
                    .findByTenantIdAndSku(tenantId, item.getSku())
                    .orElseThrow();


            Pricing pricing = new Pricing(
                    product.getPrice(),
                    product.getInventory(),
                    tenantId,
                    product.getSku()
            );

            BigDecimal finalPrice = pricingService.calculate(pricing);

            total = total.add(finalPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return new CartPricingResponse(cart.getId(), total);
    }


}
