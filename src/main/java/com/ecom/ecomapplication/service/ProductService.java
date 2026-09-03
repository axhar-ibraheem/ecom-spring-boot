package com.ecom.ecomapplication.service;

import com.ecom.ecomapplication.dto.ProductRequest;
import com.ecom.ecomapplication.dto.ProductResponse;
import com.ecom.ecomapplication.model.Product;
import com.ecom.ecomapplication.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> fetchAllProducts() {
        return productRepository
                .findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Transactional
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository
                .findById(id)
                .map(existingProduct -> {
                    updateProductFields(existingProduct, productRequest);
                    return toResponse(existingProduct);
                });
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        return productRepository
                .findById(id)
                .map(product -> {
                    product.setActive(false);
                    return true;
                })
                .orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository
                .searchActiveProducts(keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Product toEntity(ProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        return product;
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());

        return response;
    }

    private void updateProductFields(Product product, ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

}
