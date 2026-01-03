package com.example.demo.servicetest;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    void should_checkout_cart_and_lock_inventory() {

        Cart cart = new Cart();
        cart.setUserId("user1");
        cart.setTenantId("tenant1");
        cart.setActive(true);

        CartItem item = new CartItem();
        item.setProductId("product-1");
        item.setSku("sku-1");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(100));
        item.setTotalPrice(BigDecimal.valueOf(200));

        cart.getItems().add(item);

        when(cartRepository.findByUserIdAndTenantIdAndActiveTrue("user1", "tenant1")).thenReturn(Optional.of(cart));

        when(productRepository.reserveInventory(
                anyString(),
                anyString(),
                anyInt()
        )).thenReturn(1L);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.checkout("tenant1", "user1");

        assertNotNull(order);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, order.getItems().size());
    }
}


