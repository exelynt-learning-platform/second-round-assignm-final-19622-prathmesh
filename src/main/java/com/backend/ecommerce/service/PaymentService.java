package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.PaymentStatus;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository, @Value("${stripe.api.secretKey}") String secretKey) {
        this.orderRepository = orderRepository;

        Stripe.apiKey = secretKey;
    }

    public Order processPayment(Long orderId, User user) throws Exception {

        // Step 1: Find the Order and check security rules
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You can only pay for your own orders!");
        }

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("This order is already paid!");
        }

        // Step 2: Convert the total price to Cents (Multiply by 100)
        long amountInCents = order.getTotalPrice().multiply(new BigDecimal("100")).longValue();

        // Step 3: Pack the box to send to Stripe!
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd") 
                .setDescription("Payment for Order ID: " + order.getId())
                .setSource("tok_visa") //  test token that fakes a successful Visa card
                .build();

        // Step 4: Actually send the request to Stripe's servers!
        Charge charge = Charge.create(params);

        order.setPaymentStatus(PaymentStatus.COMPLETED);

        return orderRepository.save(order);
    }
}