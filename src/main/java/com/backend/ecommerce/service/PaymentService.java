package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.PaymentStatus;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.api.secretKey}")
    private String secretKey;

    @Autowired
    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order processPayment(Long orderId, User user, String stripeToken) throws Exception {

        // Initialize Stripe right before use
        Stripe.apiKey = secretKey;

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You can only pay for your own orders!");
        }

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("This order is already paid!");
        }

        long amountInCents = order.getTotalPrice().multiply(new BigDecimal("100")).longValue();

        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setDescription("Payment for Order ID: " + order.getId())
                .setSource(stripeToken)
                .build();

        Charge charge = Charge.create(params);

        order.setPaymentStatus(PaymentStatus.COMPLETED);

        return orderRepository.save(order);
    }
}