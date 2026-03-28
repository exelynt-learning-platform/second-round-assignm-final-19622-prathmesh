package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.CartItemAddDto;
import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository,  ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getOrCreateCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }

        Cart cart = new Cart();

        cart.setUser(user);

        return cartRepository.save(cart);
    }

    public Cart addItemToCart(User user, CartItemAddDto dto) {
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean itemExists = false;

        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getProduct().getId().equals(product.getId())) {

                int requestedTotalQuantity = cartItem.getQuantity() + dto.getQuantity();

                if (requestedTotalQuantity > product.getStockQuantity()) {
                    throw new RuntimeException("Not enough stock! Only " + product.getStockQuantity() + " available.");
                }

                cartItem.setQuantity(requestedTotalQuantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {

            if (dto.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException("Not enough stock! Only " + product.getStockQuantity() + " available.");
            }

            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(dto.getQuantity());
            newItem.setCart(cart);

            cart.getItems().add(newItem);
        }

        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    private void recalculateTotal(Cart cart) {
        BigDecimal total = java.math.BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            // 1. Get the price of a single item
            BigDecimal price = item.getProduct().getPrice();

            // 2. Multiply it by how many the user has in their cart
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            // 3. Add that to the running total
            total = total.add(itemTotal);
        }

        // 4. Update the cart total price field
        cart.setTotalPrice(total);
    }

    public Cart removeItemFromCart(User user, Long productId) {

        Cart cart = getOrCreateCart(user);

        cart.getItems().removeIf(item ->
                item.getProduct().getId().equals(productId));

        recalculateTotal(cart);

        return cartRepository.save(cart);
    }
}
