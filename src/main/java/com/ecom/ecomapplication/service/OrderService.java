package com.ecom.ecomapplication.service;

import com.ecom.ecomapplication.dto.OrderItemDto;
import com.ecom.ecomapplication.dto.OrderResponse;
import com.ecom.ecomapplication.model.*;
import com.ecom.ecomapplication.repository.OrderRepository;
import com.ecom.ecomapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();

        BigDecimal totalPrice = cartItems
                .stream()
                .map(item -> item
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems
                .stream()
                .map(item -> new OrderItem(null, item.getProduct(), item.getQuantity(), item.getPrice(), order))
                .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemDto> orderItems = order
                .getItems()
                .stream()
                .map(orderItem -> new OrderItemDto(orderItem.getId(), orderItem
                        .getProduct()
                        .getId(), orderItem.getQuantity(), orderItem.getPrice(), orderItem
                        .getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()))))
                .toList();

        return new OrderResponse(order.getId(), order.getTotalAmount(), order.getOrderStatus(), orderItems, order.getCreatedAt());
    }
}
