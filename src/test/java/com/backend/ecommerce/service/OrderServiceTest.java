package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.CheckoutDto;
import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // This tells Java to turn on the "Stunt Doubles"
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder_WhenCartIsEmpty_ShouldThrowException() {

        User fakeUser = new User();
        fakeUser.setId(1L);

        Cart emptyCart = new Cart();
        emptyCart.setItems(new ArrayList<>()); // Explicitly making the basket empty

        CheckoutDto fakeDto = new CheckoutDto();
        fakeDto.setShippingAddress("123 Test Street");

        // "If the worker asks for this user's cart, hand them the empty one!"
        when(cartService.getOrCreateCart(fakeUser)).thenReturn(emptyCart);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(fakeUser, fakeDto);
        });

        assertEquals("Your cart is empty!", exception.getMessage());
    }
}