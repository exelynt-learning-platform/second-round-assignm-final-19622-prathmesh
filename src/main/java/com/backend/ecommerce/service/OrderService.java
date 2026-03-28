package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.CheckoutDto;
import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.OrderRepository;
import com.backend.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired // <-- Satisfies Review #4 constructor injection bug
    public OrderService(OrderRepository orderRepository, CartService cartService,
                        CartRepository cartRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
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
            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemTotal);
            order.getItems().add(orderItem);

            // Deduct inventory
            int newStock = product.getStockQuantity() - cartItem.getQuantity();
            product.setStockQuantity(newStock);
            productRepository.save(product);
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