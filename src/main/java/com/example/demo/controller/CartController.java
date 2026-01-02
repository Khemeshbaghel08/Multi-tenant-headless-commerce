package com.example.demo.controller;

import com.example.demo.dto.AddCartRequest;
import com.example.demo.dto.CartPricingResponse;
import com.example.demo.dto.JwtPrincipal;
import com.example.demo.entity.Cart;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/items")
    public Cart addItem(@RequestBody AddCartRequest request, Authentication auth) {
        JwtPrincipal principal = (JwtPrincipal) auth.getPrincipal();

        return cartService.addItem(
                principal.userId(),
                principal.tenantId(),
                request.sku(),
                request.quantity()
        );
    }

    @GetMapping("/price")
    public CartPricingResponse price(Authentication auth) {
        JwtPrincipal principal = (JwtPrincipal) auth.getPrincipal();

        return cartService.getCartPricing(principal.userId(), principal.tenantId());
    }
}
