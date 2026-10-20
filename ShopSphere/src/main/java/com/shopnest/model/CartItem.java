package com.shopnest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an item stored in a user's or visitor's shopping cart.
 * Maps to the 'cart_items' table in H2 database.
 * Tied to the visitor's HTTP session ID for seamless guest shopping.
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // HTTP session token identifying the visitor/customer cart
    @Column(nullable = false, length = 100)
    private String sessionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    public CartItem() {
        this.addedAt = LocalDateTime.now();
    }

    public CartItem(String sessionId, Product product, Integer quantity) {
        this.sessionId = sessionId;
        this.product = product;
        this.quantity = quantity;
        this.addedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.addedAt == null) {
            this.addedAt = LocalDateTime.now();
        }
    }

    // Calculate subtotal for this item (price * quantity)
    public Double getSubtotal() {
        if (product != null && product.getPrice() != null && quantity != null) {
            return product.getPrice() * quantity;
        }
        return 0.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
