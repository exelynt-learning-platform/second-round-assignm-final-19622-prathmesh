package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.CheckoutDto;
import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    public Order createOrder(User user, CheckoutDto checkoutDto) {

        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Your cart is empty!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(checkoutDto.getShippingAddress());
        order.setPaymentStatus(PaymentStatus.PENDING);
        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemTotal);

            order.getItems().add(orderItem);
        }
        order.setTotalPrice(calculatedTotal);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return savedOrder;
    }

    public java.util.List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}