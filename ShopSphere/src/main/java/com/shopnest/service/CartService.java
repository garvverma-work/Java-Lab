package com.shopnest.service;

import com.shopnest.dto.CartSummaryDto;
import com.shopnest.model.CartItem;
import com.shopnest.model.Product;
import com.shopnest.repository.CartItemRepository;
import com.shopnest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service managing shopping cart operations: adding, updating quantities,
 * removing items, calculating totals, and syncing with H2 database.
 */
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // Retrieve entire cart summary for the session
    public CartSummaryDto getCartSummary(String sessionId) {
        List<CartItem> items = cartItemRepository.findBySessionIdOrderByAddedAtDesc(sessionId);
        return new CartSummaryDto(items);
    }

    // Add product to cart (or increment quantity if already present)
    @Transactional
    public void addToCart(String sessionId, Long productId, int quantity) {
        if (quantity <= 0) {
            quantity = 1;
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (!product.isInStock()) {
            throw new IllegalStateException("Sorry, " + product.getName() + " is currently out of stock.");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findBySessionIdAndProductId(sessionId, productId);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + quantity;
            // Prevent adding more than available inventory
            if (newQuantity > product.getStockQuantity()) {
                newQuantity = product.getStockQuantity();
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            int initialQuantity = Math.min(quantity, product.getStockQuantity());
            CartItem newItem = new CartItem(sessionId, product, initialQuantity);
            cartItemRepository.save(newItem);
        }
    }

    // Update quantity for a specific product in cart
    @Transactional
    public void updateQuantity(String sessionId, Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(sessionId, productId);
            return;
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findBySessionIdAndProductId(sessionId, productId);
        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            // Cap at available stock
            if (newQuantity > product.getStockQuantity()) {
                newQuantity = product.getStockQuantity();
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
    }

    // Remove single product from cart
    @Transactional
    public void removeFromCart(String sessionId, Long productId) {
        cartItemRepository.deleteBySessionIdAndProductId(sessionId, productId);
    }

    // Clear entire cart for session
    @Transactional
    public void clearCart(String sessionId) {
        cartItemRepository.deleteAllBySessionId(sessionId);
    }

    // Count distinct items in visitor's cart for the navbar badge
    public int getCartItemCount(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return 0;
        }
        List<CartItem> items = cartItemRepository.findBySessionIdOrderByAddedAtDesc(sessionId);
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }
}
