package com.backend.ecommerce.controller;

import com.backend.ecommerce.dto.CheckoutDto;
import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.OrderService;
import com.backend.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, UserRepository userRepository,  PaymentService paymentService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@Valid @RequestBody CheckoutDto checkoutDto, Principal principal) {

        // 1. Get the email
        String email = principal.getName();

        // 2. Find the user in the userRepository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User NOT Found!!!"));

        Order order = orderService.createOrder(user, checkoutDto);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<java.util.List<Order>> getOrderHistory(Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User NOT Found!!!"));


        List<Order> orders= orderService.getOrdersByUser(user);

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Order> payForOrder(@PathVariable Long orderId, Principal principal) throws Exception {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User NOT Found!!!"));

        Order paidOrder = paymentService.processPayment(orderId, user);

        return ResponseEntity.ok(paidOrder);

    }
}