package com.shopnest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a Product in the ShopNest catalog.
 * Maps directly to the 'products' table in the H2 database.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique Stock Keeping Unit (e.g. SN-ELEC-001)
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Product price in Indian Rupees (INR ₹)
    @Column(nullable = false)
    private Double price;

    // Categories: Electronics, Fashion, Home Appliances, Books, Accessories
    @Column(nullable = false, length = 80)
    private String category;

    // Available units in warehouse
    @Column(nullable = false)
    private Integer stockQuantity;

    // URL or relative path to product image
    @Column(length = 500)
    private String imageUrl;

    // Product rating out of 5.0
    private Double rating = 4.5;

    // Flag for showcasing on home page
    private Boolean featured = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Product() {
        this.createdAt = LocalDateTime.now();
    }

    public Product(String sku, String name, String description, Double price, 
                   String category, Integer stockQuantity, String imageUrl, 
                   Double rating, Boolean featured) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.rating = rating != null ? rating : 4.5;
        this.featured = featured != null ? featured : false;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Helper method to check stock status
    public boolean isInStock() {
        return this.stockQuantity != null && this.stockQuantity > 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
