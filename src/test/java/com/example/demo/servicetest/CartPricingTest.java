package com.example.demo.servicetest;


import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.enums.CartStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.PricingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class CartPricingTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private CartService cartService;

    @Test
    void should_create_single_active_cart_per_user() {

        Product product1 = new Product();
        product1.setSku("SKU-1");
        product1.setInventory(10);

        Product product2 = new Product();
        product2.setSku("SKU-2");
        product2.setInventory(10);

        when(productRepository.findByTenantIdAndSku("tenant1", "SKU-1")).thenReturn(Optional.of(product1));

        when(productRepository.findByTenantIdAndSku("tenant1", "SKU-2")).thenReturn(Optional.of(product2));

        Cart cart = new Cart();
        cart.setActive(true);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserIdAndTenantIdAndActiveTrue(
                "user1", "tenant1"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(cart));

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addItem("user1", "tenant1", "SKU-1", 1);
        cartService.addItem("user1", "tenant1", "SKU-2", 1);

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
        assertEquals(2, cart.getItems().size());
    }
}
