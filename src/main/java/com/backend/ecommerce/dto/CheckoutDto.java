package com.backend.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckoutDto {

    @NotEmpty(message = "Shipping Address Should not be Empty!")
    private String shippingAddress;
}
