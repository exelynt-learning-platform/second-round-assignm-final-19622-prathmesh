package com.backend.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAddDto {

    @NotEmpty(message = "Product Name Should NOT Empty")
    private String name;

    @NotEmpty(message = "Product Description Should NOT Empty")
    private String description;

    @NotNull(message = "Product Price Should NOT Empty")
    @Min(value = 0, message = "Price cannot be less than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    @NotNull(message = "Stock Quantity should NOT be empty")
    private Integer  stockQuantity;

    @NotEmpty(message = "Image Url Should NOT Empty")
    private String imageUrl;
}
