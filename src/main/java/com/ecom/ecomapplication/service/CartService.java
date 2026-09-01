package com.ecom.ecomapplication.service;

import com.ecom.ecomapplication.dto.CartItemRequest;
import com.ecom.ecomapplication.model.CartItem;
import com.ecom.ecomapplication.model.Product;
import com.ecom.ecomapplication.model.User;
import com.ecom.ecomapplication.repository.CartItemRepository;
import com.ecom.ecomapplication.repository.ProductRepository;
import com.ecom.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {

        Optional<Product> productOptional = productRepository.findById(request.getProductId());
        if (productOptional.isEmpty())
            return false;

        Product product = productOptional.get();
        if (product.getStockQuantity() < request.getQuantity())
            return false;

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if (userOptional.isEmpty())
            return false;

        User user = userOptional.get();

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());

        }
        cartItemRepository.save(cartItem);
        return true;
    }
}
