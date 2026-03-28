package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.ProductAddDto;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //1. Add new Product
    public Product addProduct(ProductAddDto productAddDto) {
        Product product = new Product();

        product.setName(productAddDto.getName());
        product.setDescription(productAddDto.getDescription());
        product.setPrice(productAddDto.getPrice());
        product.setStockQuantity(productAddDto.getStockQuantity());
        product.setImageUrl(productAddDto.getImageUrl());

        return productRepository.save(product);
    }

    //2. Get All Products
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    //3. Get Product by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // 4. Update Product
    public Product updateProduct(Long id, ProductAddDto productDetails) {

        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());

        return productRepository.save(product);
    }

    // 5. Delete Product
    public void deleteProduct(Long id) {
        Product product = getProductById(id);

        productRepository.delete(product);
    }
}