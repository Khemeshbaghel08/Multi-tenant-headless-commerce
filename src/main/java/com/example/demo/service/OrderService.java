package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.enums.CartStatus;
import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order checkout(String tenantId, String userId) {

        Cart cart = cartRepository
                .findByUserIdAndTenantIdAndActiveTrue(userId, tenantId)
                .orElseThrow(() -> new RuntimeException("No active cart"));

        for (CartItem item : cart.getItems()) {

            long updated = productRepository.reserveInventory(
                    tenantId,
                    item.getSku(),
                    item.getQuantity()
            );

            if (updated == 0) {
                throw new RuntimeException(
                        "Insufficient inventory for SKU " + item.getSku()
                );
            }

        }

        Order order = new Order();
        order.setTenantId(tenantId);
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> new OrderItem(
                        i.getProductId(),
                        i.getSku(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getTotalPrice()
                ))
                .toList();

        order.setItems(orderItems);
        order.setTotalAmount(cart.getPrice());
        Order savedOrder = orderRepository.save(order);

        cart.setStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(cart);

        return savedOrder;
    }
}
