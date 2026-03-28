package com.backend.ecommerce.controller;

import com.backend.ecommerce.dto.ProductAddDto;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //1. Add new Product
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductAddDto productAddDto) {

        Product savedProduct = productService.addProduct(productAddDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    //2. Get All Products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> savedProducts = productService.getAllProducts();

        return ResponseEntity.status(HttpStatus.OK)
                .body(savedProducts);
    }

    //3. Get Product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    // 4. Update Product by id
    @PutMapping("/{id}")
    public ResponseEntity<Product>  updateProduct(@PathVariable Long id, @Valid @RequestBody ProductAddDto productAddDto) {

        Product savedProduct = productService.updateProduct(id, productAddDto);

        return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
    }

    // 5. Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted Successfully");
    }
}
